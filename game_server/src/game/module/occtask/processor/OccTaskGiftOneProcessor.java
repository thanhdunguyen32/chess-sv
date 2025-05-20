package game.module.occtask.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.occtask.bean.OccTask;
import game.module.occtask.dao.OccTaskCache;
import game.module.occtask.dao.OccTaskDaoHelper;
import game.module.occtask.logic.MyOccTaskTemplateCache;
import game.module.template.MyOccTaskTemplate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskGiftOne.id, accessLimit = 200)
public class OccTaskGiftOneProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskGiftOneProcessor.class);

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
        WsMessageMission.C2SOccTaskGiftOne reqmsg = WsMessageMission.C2SOccTaskGiftOne.parse(request);
        logger.info("occ task gift1!player={},req={}", playerId, reqmsg);
        //param check
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask == null || occTask.getIndex() != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskGiftOne.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //reward is get
        int level_index = reqmsg.level_index;
        if (occTask.getTaskStatus() != null && occTask.getTaskStatus().containsKey(level_index) && occTask.getTaskStatus().get(level_index) == 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskGiftOne.msgCode, 1462);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //progress not finish
        MyOccTaskTemplate.OccTaskConfig2 config2 = MyOccTaskTemplateCache.getInstance().getConfig2();
        MyOccTaskTemplate.OccTaskTemplate1 occTaskTemplate1 = config2.getList().get(level_index);
        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), occTaskTemplate1.getMark());
        if (markCount < occTaskTemplate1.getLimit()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskGiftOne.msgCode, 1463);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //save bean
        Map<Integer, Integer> taskStatus = occTask.getTaskStatus();
        if (taskStatus == null) {
            taskStatus = new HashMap<>();
            occTask.setTaskStatus(taskStatus);
        }
        taskStatus.put(level_index, 2);
        OccTaskDaoHelper.asyncUpdateOccTask(occTask);
        //award
        List<WsMessageBase.RewardInfo> respAwardList = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateConfig : occTaskTemplate1.getRewards()) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_OCC_TASK);
            respAwardList.add(new WsMessageBase.RewardInfo(rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT()));
        }
        //ret
        WsMessageMission.S2COccTaskGiftOne respmsg = new WsMessageMission.S2COccTaskGiftOne();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
