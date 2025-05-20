package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.template.ManorTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageManor.C2SManorMop.id, accessLimit = 200)
public class ManorMopProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorMopProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageManor.C2SManorMop reqmsg = WsMessageManor.C2SManorMop.parse(request);
        logger.info("manor mop!player={},req={}", playerId, reqmsg);
        int times = reqmsg.times;
        //粮草是否够
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.FOOD, times)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorMop.msgCode, 1359);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //boss die
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField manorField = manorBean.getManorField();
        if (manorField.getBossState() != 2 || manorField.getBossLastDamage() == null || manorField.getBossLastDamage() == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorMop.msgCode, 1388);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //血量扣除
        int bossOldHp = manorField.getBossNowHp();
        int lastDamage = manorField.getBossLastDamage();
        manorField.setBossNowHp(bossOldHp - lastDamage * times);
        //木材奖励
        List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, 20, LogConstants.MODULE_MANOR);
        dropItems.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.WOOD, 20));
        if (bossOldHp > 0 && manorField.getBossNowHp() <= 0) {
            manorField.setBossNowHp(0);
            manorField.setBossState(1);
            //drop
            Integer manorLevel = manorBean.getLevel();
            ManorTemplate.ManorEnemyTemplate bossTemplate = ManorTemplateCache.getInstance().getBossTemplate(manorLevel - 1);
            for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getREWARD()) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
                dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
        }
        //save bean
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.FOOD, -times, LogConstants.MODULE_MANOR);
        //ret
        WsMessageManor.S2CManorMop respmsg = new WsMessageManor.S2CManorMop();
        respmsg.reward = dropItems;
        respmsg.nowhp = manorField.getBossNowHp();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
