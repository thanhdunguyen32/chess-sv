package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.SurrenderPersuade;
import game.module.manor.dao.SurrenderPersuadeCache;
import game.module.manor.dao.SurrenderPersuadeDaoHelper;
import game.module.manor.dao.SurrenderTemplateCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.SurrenderTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageManor;

@MsgCodeAnn(msgcode = WsMessageManor.C2SSrenderPersua.id, accessLimit = 200)
public class SurrenderPersuadeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(SurrenderPersuadeProcessor.class);

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
        WsMessageManor.C2SSrenderPersua reqmsg = WsMessageManor.C2SSrenderPersua.parse(request);
        logger.info("surrender persuade,player={},req={}", playerId, reqmsg);
        int chapterid = reqmsg.gsid;
        //check
        SurrenderTemplate surrenderTemplate = SurrenderTemplateCache.getInstance().getSurrenderTemplate(chapterid);
        if (surrenderTemplate == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CSrenderPersua.msgCode, 1449);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        SurrenderPersuade surrenderPersuade = SurrenderPersuadeCache.getInstance().getSurrenderPersuade(playerId);
        if (surrenderPersuade == null || surrenderPersuade.getSurrendermap() == null || !surrenderPersuade.getSurrendermap().containsKey(chapterid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CSrenderPersua.msgCode, 1449);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is already get
        Integer myLoyal = surrenderPersuade.getSurrendermap().get(chapterid);
        if (myLoyal <= 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CSrenderPersua.msgCode, 1450);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Integer costCoin = surrenderTemplate.getCAST();
        //cost
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.GOLD, costCoin)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CSrenderPersua.msgCode, 1449);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        WsMessageManor.S2CSrenderPersua respmsg = new WsMessageManor.S2CSrenderPersua();
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, -costCoin, LogConstants.MODULE_MANOR);
        //reduce loyal
        int[] loyalRange = surrenderTemplate.getLOYAL().getRANGE();
        myLoyal -= RandomUtils.nextInt(loyalRange[0], loyalRange[1] + 1);
        myLoyal = Math.max(myLoyal, 0);
        boolean isSuccess = false;
        if (myLoyal <= 0) {
            isSuccess = true;
        } else {
            int successRate = (int)((100 - myLoyal) * 0.6f);
            isSuccess = RandomUtils.nextInt(0, 100) < successRate;
            if(isSuccess){
                myLoyal = 0;
            }
        }
        respmsg.loyal = myLoyal;
        //
        if (isSuccess) {
            for (RewardTemplateSimple rewardTemplateSimple : surrenderTemplate.getSRENDER()) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
                respmsg.reward = new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
        }
        //save bean
        surrenderPersuade.getSurrendermap().put(chapterid, myLoyal);
        SurrenderPersuadeDaoHelper.asyncUpdateSurrenderPersuade(surrenderPersuade);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
