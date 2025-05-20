package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.template.AffairsTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageHall;

import java.util.Date;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairAcce.id, accessLimit = 200)
public class AffairAcceProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AffairAcceProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageAffair.C2SAffairAcce reqmsg = WsMessageAffair.C2SAffairAcce.parse(request);
        int playerId = playingRole.getId();
        logger.info("affair acce!player={},req={}", playerId, reqmsg);
        int affair_index = reqmsg.affair_index;
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null || playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null ||
                affair_index <0 || affair_index >= playerAffairs.getDbAffairs().getAffairList().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAcce.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is progress
        AffairBean affairBean = playerAffairs.getDbAffairs().getAffairList().get(affair_index);
        if (affairBean.getStatus() != AffairConstants.AFFAIR_STATUS_PROGRESS) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAcce.msgCode, 1420);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is finish
        Integer affairTemplateId = affairBean.getTemplateId();
        Date startTime = affairBean.getStartTime();
        AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(affairTemplateId);
        Integer affairStar = affairsTemplate.getSTAR();
        int costHour = AffairConstants.AFFAIR_COST_HOUR[affairStar - 1];
        Date now = new Date();
        //已经结束，不需要加速
        if (DateUtils.addHours(startTime, costHour).before(now)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAcce.msgCode, 1421);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        if (playingRole.getPlayerBean().getMoney() < affairsTemplate.getCOST()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAcce.msgCode, 1422);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        if (affairsTemplate.getCOST() > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -affairsTemplate.getCOST(), LogConstants.MODULE_AFFAIR);
        }
        //
        affairBean.setStatus(AffairConstants.AFFAIR_STATUS_FINISH);
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //ret
        WsMessageAffair.S2CAffairAcce respmsg = new WsMessageAffair.S2CAffairAcce();
        respmsg.affair_index = affair_index;
        respmsg.item_list = AffairListProcessor.buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
