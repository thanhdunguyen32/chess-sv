package game.module.occtask.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
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
import org.apache.commons.lang3.RandomUtils;
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
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskGiftAll.id, accessLimit = 200)
public class OccTaskGiftAllProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskGiftAllProcessor.class);

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
        logger.info("occ task gift all!player={}", playerId);
        //param check
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask == null || occTask.getIndex() != 2 || occTask.getTaskStatus() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskGiftAll.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //progress not finish
        MyOccTaskTemplate.OccTaskConfig2 config2 = MyOccTaskTemplateCache.getInstance().getConfig2();
        for (int level_index = 0; level_index < config2.getList().size(); level_index++) {
            if (!occTask.getTaskStatus().containsKey(level_index) || occTask.getTaskStatus().get(level_index) != 2) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskGiftAll.msgCode, 1464);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //save bean
        occTask.setIndex(3);
        List<MyOccTaskTemplate.OccTaskRewardItem> config3Rewards = MyOccTaskTemplateCache.getInstance().getConfig3().getREWARDS();
        occTask.setRewardId(RandomUtils.nextInt(0, config3Rewards.size()));
        OccTaskDaoHelper.asyncUpdateOccTask(occTask);
        //award
        List<WsMessageBase.RewardInfo> respAwardList = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateConfig : config2.getReward()) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_OCC_TASK);
            respAwardList.add(new WsMessageBase.RewardInfo(rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT()));
        }
        //ret
        WsMessageMission.S2COccTaskGiftAll respmsg = new WsMessageMission.S2COccTaskGiftAll();
        respmsg.rewards = respAwardList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
