package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorGetBox.id, accessLimit = 200)
public class ManorFieldGetBoxProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorFieldGetBoxProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageManor.C2SManorGetBox reqmsg = WsMessageManor.C2SManorGetBox.parse(request);
        logger.info("manor field get box,playerId={},req={}", playerId, reqmsg);
        int enemy_index = reqmsg.index;
        //can open
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        if (enemy_index >= manorBean.getManorField().getEnemys().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1386);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is open
        ManorBean.DbManorEnemy dbManorEnemy = manorBean.getManorField().getEnemys().get(enemy_index);
        if (dbManorEnemy.getHasBoxOpen() == null || dbManorEnemy.getHasBoxOpen()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1387);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : dbManorEnemy.getBoxItem()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //save bean
        dbManorEnemy.setHasBoxOpen(true);
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //ret
        WsMessageManor.S2CManorGetBox respmsg = new WsMessageManor.S2CManorGetBox();
        respmsg.items = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
