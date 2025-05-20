package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.occtask.logic.OccTaskManager;
import game.module.template.AffairsTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Collections;
import java.util.Date;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairAward.id, accessLimit = 200)
public class AffairAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairAwardProcessor.class);

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
        WsMessageAffair.C2SAffairAward reqmsg = WsMessageAffair.C2SAffairAward.parse(request);
        int playerId = playingRole.getId();
        logger.info("affair award!player={},req={}", playerId, reqmsg);
        int affair_index = reqmsg.affair_index;
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null || playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null ||
                affair_index < 0 || affair_index >= playerAffairs.getDbAffairs().getAffairList().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is finish
        AffairBean affairBean = playerAffairs.getDbAffairs().getAffairList().get(affair_index);
        Integer affairTemplateId = affairBean.getTemplateId();
        Date startTime = affairBean.getStartTime();
        AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(affairTemplateId);
        Integer affairStar = affairsTemplate.getSTAR();
        int costHour = AffairConstants.AFFAIR_COST_HOUR[affairStar - 1];
        Date now = new Date();
        if (affairBean.getStatus() != AffairConstants.AFFAIR_STATUS_FINISH && (startTime == null || DateUtils.addHours(startTime, costHour).after(now))) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairAward.msgCode, 1420);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        playerAffairs.getDbAffairs().getAffairList().remove(affair_index);
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //award
        RewardTemplateSimple affairReward = affairBean.getReward();
        AwardUtils.changeRes(playingRole, affairReward.getGSID(), affairReward.getCOUNT(), LogConstants.MODULE_AFFAIR);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.AFFAIR_PMARK, 1, LogConstants.MODULE_AFFAIR);
        AwardUtils.changeRes(playingRole, MissionConstants.MARK_AFFAIR, 1, LogConstants.MODULE_AFFAIR);
        ActivityMonthManager.getInstance().affairFinish(playingRole, affairStar);
        OccTaskManager.getInstance().affairAddOccTaskMark(playingRole);
        //ret
        WsMessageAffair.S2CAffairAward respmsg = new WsMessageAffair.S2CAffairAward();
        respmsg.affair_index = affair_index;
        respmsg.rewards = Collections.singletonList(new WsMessageBase.IORewardItem(affairReward.getGSID(), affairReward.getCOUNT()));
        respmsg.item_list = AffairListProcessor.buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
