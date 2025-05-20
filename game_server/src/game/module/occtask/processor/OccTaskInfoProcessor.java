package game.module.occtask.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.item.logic.ItemManager;
import game.module.occtask.bean.OccTask;
import game.module.occtask.dao.OccTaskCache;
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
import ws.WsMessageMission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMission.C2SOccTaskInfo.id, accessLimit = 200)
public class OccTaskInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(OccTaskInfoProcessor.class);

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
        logger.info("get occ task info!player={}", playerId);
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        WsMessageMission.S2COccTaskInfo respmsg = new WsMessageMission.S2COccTaskInfo();
        if (occTask == null || occTask.getIndex()>3) {
            respmsg.index = 0;
            respmsg.occtype = 0;
            respmsg.rewards = new ArrayList<>();
            List<RewardTemplateSimple> config0 = MyOccTaskTemplateCache.getInstance().getConfig0();
            for (RewardTemplateSimple rewardTemplateSimple : config0) {
                respmsg.rewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
        } else {
            respmsg.index = occTask.getIndex();
            respmsg.occtype = occTask.getOcctype();
            switch (occTask.getIndex()) {
                case 2:
                    MyOccTaskTemplate.OccTaskConfig2 config2 = MyOccTaskTemplateCache.getInstance().getConfig2();
                    respmsg.reward = new ArrayList<>();
                    for (RewardTemplateSimple rewardTemplateSimple : config2.getReward()) {
                        respmsg.reward.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                    }
                    respmsg.list = new ArrayList<>();
                    List<MyOccTaskTemplate.OccTaskTemplate1> list = config2.getList();
                    int idx = 0;
                    for (MyOccTaskTemplate.OccTaskTemplate1 occTaskTemplate1 : list) {
                        WsMessageBase.IOOccTask1 ioOccTask1 = new WsMessageBase.IOOccTask1();
                        ioOccTask1.rewards = new ArrayList<>();
                        for (RewardTemplateSimple rewardTemplateSimple : occTaskTemplate1.getRewards()) {
                            ioOccTask1.rewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                        }
                        ioOccTask1.intro = occTaskTemplate1.getIntro();
                        ioOccTask1.mark = occTaskTemplate1.getMark();
                        ioOccTask1.limit = occTaskTemplate1.getLimit();
                        ioOccTask1.page = occTaskTemplate1.getPage();
                        //
                        ioOccTask1.status = 0;
                        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), occTaskTemplate1.getMark());
                        if (markCount >= occTaskTemplate1.getLimit()) {
                            ioOccTask1.status = 1;
                        }
                        if (occTask.getTaskStatus() != null && occTask.getTaskStatus().containsKey(idx) && occTask.getTaskStatus().get(idx) == 2) {
                            ioOccTask1.status = 2;
                        }
                        respmsg.list.add(ioOccTask1);
                        idx++;
                    }
                    break;
                case 3:
                    MyOccTaskTemplate.OccTaskConfig3 config3 = MyOccTaskTemplateCache.getInstance().getConfig3();
                    respmsg.refcost = new WsMessageBase.RewardInfo(config3.getRefcost().getGSID(), config3.getRefcost().getCOUNT());
                    respmsg.prewards = config3.getPrewards();
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
                    break;
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
