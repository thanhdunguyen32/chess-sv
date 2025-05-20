package game.module.occtask.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.occtask.bean.OccTask;
import game.module.occtask.dao.OccTaskCache;
import game.module.occtask.dao.OccTaskDaoHelper;
import game.module.occtask.logic.MyOccTaskTemplateCache;
import game.module.occtask.logic.OccTaskManager;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageMission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskFree.id, accessLimit = 200)
public class OccTaskFreeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskFreeProcessor.class);

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
        int playerId = playingRole.getId();
        logger.info("occ task free!player={}", playerId);
        //param check
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask != null && occTask.getIndex() > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskFree.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        if (occTask == null) {
            occTask = OccTaskManager.getInstance().createOccTask(playerId);
            OccTaskCache.getInstance().addOccTask(occTask);
        } else {
            occTask.setIndex(1);
        }
        if (occTask.getId() == null) {
            OccTaskDaoHelper.asyncInsertOccTask(occTask);
        } else {
            OccTaskDaoHelper.asyncUpdateOccTask(occTask);
        }
        //award
        List<RewardTemplateSimple> config0 = MyOccTaskTemplateCache.getInstance().getConfig0();
        List<WsMessageBase.RewardInfo> respAwardList = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateConfig : config0) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_OCC_TASK);
            respAwardList.add(new WsMessageBase.RewardInfo(rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT()));
        }
        //ret
        WsMessageMission.S2COccTaskFree respmsg = new WsMessageMission.S2COccTaskFree();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
