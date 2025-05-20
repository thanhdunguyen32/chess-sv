package game.module.occtask.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
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
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageMission;

import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskPackRPay.id, accessLimit = 200)
public class OccTaskPackRPayProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskPackRPayProcessor.class);

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
        logger.info("occ task yuanbao pay!player={}", playerId);
        //param check
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask == null || occTask.getIndex() != 3) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskPackRPay.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //money enough
        int rewardIndex = occTask.getRewardId();
        MyOccTaskTemplate.OccTaskConfig3 config3 = MyOccTaskTemplateCache.getInstance().getConfig3();
        MyOccTaskTemplate.OccTaskRewardItem occTaskRewardItem = config3.getREWARDS().get(rewardIndex);
        if (occTaskRewardItem.getTYPE() != 1 || !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB,
                occTaskRewardItem.getVALUE())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskPackRPay.msgCode, 1466);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //save bean
        occTask.setIndex(4);
        OccTaskDaoHelper.asyncUpdateOccTask(occTask);
        //award
        for (RewardTemplateSimple rewardTemplateConfig : occTaskRewardItem.getITEMS()) {
            AwardUtils.changeRes(playingRole, rewardTemplateConfig.getGSID(), rewardTemplateConfig.getCOUNT(), LogConstants.MODULE_OCC_TASK);
        }
        //ret
        WsMessageMission.S2COccTaskPackRPay respmsg = new WsMessageMission.S2COccTaskPackRPay();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
