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
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskPackRefresh.id, accessLimit = 200)
public class OccTaskPackRefreshProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskPackRefreshProcessor.class);

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
        logger.info("occ task pack refresh!player={}", playerId);
        //param check
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask == null || occTask.getIndex() != 3) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskPackRefresh.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //yuanbao enough
        MyOccTaskTemplate.OccTaskConfig3 config3 = MyOccTaskTemplateCache.getInstance().getConfig3();
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), config3.getRefcost().getGSID(), config3.getRefcost().getCOUNT())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMission.S2COccTaskPackRefresh.msgCode, 1465);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<MyOccTaskTemplate.OccTaskRewardItem> config3Rewards = config3.getREWARDS();
        occTask.setRewardId(RandomUtils.nextInt(0, config3Rewards.size()));
        OccTaskDaoHelper.asyncUpdateOccTask(occTask);
        //cost
        AwardUtils.changeRes(playingRole, config3.getRefcost().getGSID(), -config3.getRefcost().getCOUNT(), LogConstants.MODULE_OCC_TASK);
        //ret
        WsMessageMission.S2COccTaskPackRefresh respmsg = new WsMessageMission.S2COccTaskPackRefresh();
        respmsg.refcost = new WsMessageBase.RewardInfo(config3.getRefcost().getGSID(), config3.getRefcost().getCOUNT());
        Integer rewardId = occTask.getRewardId();
        MyOccTaskTemplate.OccTaskRewardItem occTaskRewardItem = config3.getREWARDS().get(rewardId);
        respmsg.packinfo = new WsMessageBase.IOOcctaskPackinfo();
        respmsg.packinfo.ID = occTaskRewardItem.getID();
        respmsg.packinfo.TYPE = occTaskRewardItem.getTYPE();
        respmsg.packinfo.VALUE = occTaskRewardItem.getVALUE();
        respmsg.packinfo.WEIGHT = occTaskRewardItem.getWEIGHT();
        respmsg.packinfo.ITEMS = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : occTaskRewardItem.getITEMS()) {
            respmsg.packinfo.ITEMS.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
