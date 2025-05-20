package game.module.occtask.logic;

import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.occtask.bean.OccTask;
import game.module.occtask.dao.OccTaskCache;
import game.module.template.MyOccTaskTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerBean;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OccTaskManager {

    private static Logger logger = LoggerFactory.getLogger(OccTaskManager.class);

    public OccTask createOccTask(int playerId) {
        OccTask occTask = new OccTask();
        occTask.setPlayerId(playerId);
        occTask.setIndex(1);
        occTask.setOcctype(0);
        return occTask;
    }

    static class SingletonHolder {
        static OccTaskManager instance = new OccTaskManager();
    }

    public static OccTaskManager getInstance() {
        return SingletonHolder.instance;
    }

    public long getOccTaskEndTime(PlayerBean playerBean) {
        int playerId = playerBean.getId();
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask != null && occTask.getIndex() > 3) {
            return 1;
        }
        Date createTime = playerBean.getCreateTime();
        Date endTime = DateUtils.addDays(createTime, 20);
        return endTime.getTime();
    }

    public WsMessageBase.IOOcctask getOccTaskInfo(PlayerBean playerBean) {
        int playerId = playerBean.getId();
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        WsMessageBase.IOOcctask ioOcctask = new WsMessageBase.IOOcctask();
        if (occTask == null || occTask.getIndex() > 3) {
            ioOcctask.index = 0;
            ioOcctask.occtype = 0;
            ioOcctask.rewards = new ArrayList<>();
            List<RewardTemplateSimple> config0 = MyOccTaskTemplateCache.getInstance().getConfig0();
            for (RewardTemplateSimple rewardTemplateSimple : config0) {
                ioOcctask.rewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
        } else {
            ioOcctask.index = occTask.getIndex();
            ioOcctask.occtype = occTask.getOcctype();
            switch (occTask.getIndex()) {
                case 2:
                    MyOccTaskTemplate.OccTaskConfig2 config2 = MyOccTaskTemplateCache.getInstance().getConfig2();
                    ioOcctask.reward = new ArrayList<>();
                    for (RewardTemplateSimple rewardTemplateSimple : config2.getReward()) {
                        ioOcctask.reward.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                    }
                    ioOcctask.list = new ArrayList<>();
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
                        int markCount = ItemManager.getInstance().getCount(playerBean, occTaskTemplate1.getMark());
                        if (markCount >= occTaskTemplate1.getLimit()) {
                            ioOccTask1.status = 1;
                        }
                        if (occTask.getTaskStatus() != null && occTask.getTaskStatus().containsKey(idx) && occTask.getTaskStatus().get(idx) == 2) {
                            ioOccTask1.status = 2;
                        }
                        ioOcctask.list.add(ioOccTask1);
                        idx++;
                    }
                    break;
                case 3:
                    MyOccTaskTemplate.OccTaskConfig3 config3 = MyOccTaskTemplateCache.getInstance().getConfig3();
                    ioOcctask.refcost = new WsMessageBase.RewardInfo(config3.getRefcost().getGSID(), config3.getRefcost().getCOUNT());
                    ioOcctask.prewards = config3.getPrewards();
                    Integer rewardId = occTask.getRewardId();
                    MyOccTaskTemplate.OccTaskRewardItem occTaskRewardItem = config3.getREWARDS().get(rewardId);
                    ioOcctask.packinfo = new WsMessageBase.IOOcctaskPackinfo();
                    ioOcctask.packinfo.ID = occTaskRewardItem.getID();
                    ioOcctask.packinfo.TYPE = occTaskRewardItem.getTYPE();
                    ioOcctask.packinfo.VALUE = occTaskRewardItem.getVALUE();
                    ioOcctask.packinfo.WEIGHT = occTaskRewardItem.getWEIGHT();
                    ioOcctask.packinfo.ITEMS = new ArrayList<>();
                    for (RewardTemplateSimple rewardTemplateSimple : occTaskRewardItem.getITEMS()) {
                        ioOcctask.packinfo.ITEMS.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                    }
                    break;
            }
        }
        return ioOcctask;
    }

    public void pvpAddOccTaskMark(PlayingRole playingRole) {
        addOccTaskMark(playingRole, 0, 1);
    }

    public void lookStarAddOccTaskMark(PlayingRole playingRole, int times) {
        addOccTaskMark(playingRole, 1, times);
    }

    public void affairAddOccTaskMark(PlayingRole playingRole) {
        addOccTaskMark(playingRole, 2, 1);
    }

    public void buyCoinAddOccTaskMark(PlayingRole playingRole) {
        addOccTaskMark(playingRole, 3, 1);
    }

    public void addOccTaskMark(PlayingRole playingRole, int levelIndex, int addNum) {
        int playerId = playingRole.getId();
        OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
        if (occTask == null || occTask.getIndex() != 2) {
            return;
        }
        MyOccTaskTemplate.OccTaskTemplate1 occTaskTemplate1 = MyOccTaskTemplateCache.getInstance().getConfig2().getList().get(levelIndex);
        Integer occMark = occTaskTemplate1.getMark();
        AwardUtils.changeRes(playingRole, occMark, addNum, LogConstants.MODULE_OCC_TASK);
    }

}
