package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.ArrayList;

@MsgCodeAnn(msgcode = WsMessageManor.C2SSrenderBehead.id, accessLimit = 200)
public class SurrenderBeheadProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(SurrenderBeheadProcessor.class);

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
        WsMessageManor.C2SSrenderBehead reqmsg = WsMessageManor.C2SSrenderBehead.parse(request);
        logger.info("surrender behead,player={}", playerId);
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
        //do
        WsMessageManor.S2CSrenderBehead respmsg = new WsMessageManor.S2CSrenderBehead();
        respmsg.awards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : surrenderTemplate.getKILL()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
            respmsg.awards.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //save bean
        surrenderPersuade.getSurrendermap().put(chapterid, 0);
        SurrenderPersuadeDaoHelper.asyncUpdateSurrenderPersuade(surrenderPersuade);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
