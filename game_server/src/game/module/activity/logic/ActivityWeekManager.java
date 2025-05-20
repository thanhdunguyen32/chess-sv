package game.module.activity.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity.bean.ActCxry;
import game.module.activity.bean.ActTnqw;
import game.module.activity.bean.ZhdBean;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.*;
import game.module.award.logic.AwardUtils;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mail.logic.MailManager;
import game.module.template.*;
import game.module.user.dao.PlayerOtherDao;
import game.session.PlayerOnlineCacheMng;
import lion.math.RandomDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ActivityWeekManager {

    private static Logger logger = LoggerFactory.getLogger(ActivityWeekManager.class);

    static class SingletonHolder {
        static ActivityWeekManager instance = new ActivityWeekManager();
    }

    public static ActivityWeekManager getInstance() {
        return SingletonHolder.instance;
    }

    public static final int[] MJBG_HANG_REWARD = {30041, 18};

    public static final int[] GJDL_HANG_REWARD = {30043, 5};
    private Date weekEndTime;

    public Date getWeekActivityEndTime() {
        Date now = new Date();
        if (weekEndTime == null || weekEndTime.before(now)) {
            Date openTime = GameServer.getInstance().getServerConfig().getOpenTime();
            int diffDay = (int) ((now.getTime() - openTime.getTime()) / 24 / 3600000);
            int diffWeek = diffDay / 7;
            weekEndTime = DateUtils.addDays(openTime, (diffWeek + 1) * 7);
        }
        return weekEndTime;
    }

    /**
     * 观星达标活动
     */
    public void gxdbActivity(PlayingRole playingRole, int doCount) {
        ZhdBean gxdbAct = ActivityWeekCache.getInstance().getZhd("GXDB");
        Date now = new Date();
        if (gxdbAct == null || gxdbAct.getStartTime().after(now) || gxdbAct.getEndTime().before(now)) {
            return;
        }
        //is max loop
        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ActivityConstants.GXDB_PROGRESS_MARK);
        ZhdDjjfTemplate gxdbTemplate = ActivityWeekTemplateCache.getInstance().getGxdbTemplate();
        int loopMaxNum = gxdbTemplate.getMissions().get(gxdbTemplate.getMissions().size() - 1).getNUM();
        int loopCount = markCount / loopMaxNum;
        if (loopCount >= gxdbTemplate.getLooplimit()) {
            return;
        }
        int oldMarkCountPerLoop = markCount % loopMaxNum;
        //add mark
        AwardUtils.changeRes(playingRole, ActivityConstants.GXDB_PROGRESS_MARK, doCount, LogConstants.MODULE_ACTIVITY);
        //mission finish
        int markCountPerLoop = oldMarkCountPerLoop + doCount;
        String mailTitle = "Phần thưởng hoạt động tiêu chuẩn ngắm sao"; //"观星达标活动奖励"
        String mailContent = "Bạn đã hoàn thành hoạt động tiêu chuẩn ngắm sao: %1$s, phần thưởng như sau:"; //"您完成了观星达标活动：%1$s，奖励如下：";
        for (ZhdDjjfTemplate.ZhdDjjfMission zhdDjjfMission : gxdbTemplate.getMissions()) {
            if (oldMarkCountPerLoop < zhdDjjfMission.getNUM() && markCountPerLoop >= zhdDjjfMission.getNUM()) {
                mailContent = String.format(mailContent, zhdDjjfMission.getNAME());
                sendReward(mailTitle, mailContent, playingRole.getId(), zhdDjjfMission);
                break;
            }
        }
    }

    public void djjfActivity(PlayingRole playingRole, int doCount) {
        ZhdBean djjfAct = ActivityWeekCache.getInstance().getZhd("DJJF");
        Date now = new Date();
        if (djjfAct == null || djjfAct.getStartTime().after(now) || djjfAct.getEndTime().before(now)) {
            return;
        }
        //is max loop
        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ActivityConstants.DJJF_PROGRESS_MARK);
        ZhdDjjfTemplate djjfTemplate = ActivityWeekTemplateCache.getInstance().getDjjfTemplate();
        int loopMaxNum = djjfTemplate.getMissions().get(djjfTemplate.getMissions().size() - 1).getNUM();
        int loopCount = markCount / loopMaxNum;
        if (loopCount >= djjfTemplate.getLooplimit()) {
            return;
        }
        int oldMarkCountPerLoop = markCount % loopMaxNum;
        //add mark
        AwardUtils.changeRes(playingRole, ActivityConstants.DJJF_PROGRESS_MARK, doCount, LogConstants.MODULE_ACTIVITY);
        //mission finish
        int markCountPerLoop = oldMarkCountPerLoop + doCount;
        String mailTitle = "Phần thưởng hoạt động điểm danh"; //"点将得积分活动奖励"
        String mailContent = "Bạn đã hoàn thành hoạt động điểm danh: %1$s, phần thưởng như sau:"; //"您完成了点将得积分活动：%1$s，奖励如下：";
        for (ZhdDjjfTemplate.ZhdDjjfMission zhdDjjfMission : djjfTemplate.getMissions()) {
            if (oldMarkCountPerLoop < zhdDjjfMission.getNUM() && markCountPerLoop >= zhdDjjfMission.getNUM()) {
                mailContent = String.format(mailContent, zhdDjjfMission.getNAME());
                sendReward(mailTitle, mailContent, playingRole.getId(), zhdDjjfMission);
                break;
            }
        }
    }

    public void zmjfActivity(PlayingRole playingRole, int doCount) {
        ZhdBean zmjfAct = ActivityWeekCache.getInstance().getZhd("ZMJF");
        Date now = new Date();
        if (zmjfAct == null || zmjfAct.getStartTime().after(now) || zmjfAct.getEndTime().before(now)) {
            return;
        }
        //is max loop
        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ActivityConstants.ZMJF_PROGRESS_MARK);
        ZhdDjjfTemplate zmjfTemplate = ActivityWeekTemplateCache.getInstance().getZmjfTemplate();
        int loopMaxNum = zmjfTemplate.getMissions().get(zmjfTemplate.getMissions().size() - 1).getNUM();
        int loopCount = markCount / loopMaxNum;
        if (loopCount >= zmjfTemplate.getLooplimit()) {
            return;
        }
        int oldMarkCountPerLoop = markCount % loopMaxNum;
        //add mark
        AwardUtils.changeRes(playingRole, ActivityConstants.ZMJF_PROGRESS_MARK, doCount, LogConstants.MODULE_ACTIVITY);
        //mission finish
        int markCountPerLoop = oldMarkCountPerLoop + doCount;
        String mailTitle = "Phần thưởng hoạt động điểm danh"; //"招募得积分活动奖励"
        String mailContent = "Bạn đã hoàn thành hoạt động điểm danh: %1$s, phần thưởng như sau:"; //"您完成了招募得积分活动：%1$s，奖励如下：";
        for (ZhdDjjfTemplate.ZhdDjjfMission zhdDjjfMission : zmjfTemplate.getMissions()) {
            if (oldMarkCountPerLoop < zhdDjjfMission.getNUM() && markCountPerLoop >= zhdDjjfMission.getNUM()) {
                mailContent = String.format(mailContent, zhdDjjfMission.getNAME());
                sendReward(mailTitle, mailContent, playingRole.getId(), zhdDjjfMission);
                break;
            }
        }
    }

    public void djrwActivity(PlayingRole playingRole, int gsid) {
        ZhdBean djrwAct = ActivityWeekCache.getInstance().getZhd("DJRW");
        Date now = new Date();
        if (djrwAct == null || djrwAct.getStartTime().after(now) || djrwAct.getEndTime().before(now)) {
            return;
        }
        List<ZhdDjrwTemplate> djrwTemplates = ActivityWeekTemplateCache.getInstance().getDjrwTemplates();
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        if (!heroTemplate.getSTAR().equals(5)) {
            return;
        }
        Integer camp = heroTemplate.getCAMP();
        ZhdDjrwTemplate zhdDjrwTemplate = djrwTemplates.get(camp - 1);
        List<ZhdDjrwTemplate.ZhdDjrwCheck> chk = zhdDjrwTemplate.getChk();
        int markId = chk.get(0).getMARK();
        int needNum = chk.get(0).getNUM();
        AwardUtils.changeRes(playingRole, markId, 1, LogConstants.MODULE_ACTIVITY);
        int curNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), markId);
        //reward mark
        ZhdDjrwTemplate.ZhdDjrwCheck zhdDjrwCheck = djrwTemplates.get(djrwTemplates.size() - 1).getChk().get(camp - 1);
        int rewardMark = zhdDjrwCheck.getMARK();
        int rewardMarkNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), rewardMark);
        if (curNum >= needNum && rewardMarkNum <= 0) {
            sendDjjwReward(playingRole.getId(), zhdDjrwTemplate);
            AwardUtils.changeRes(playingRole, rewardMark, 1, LogConstants.MODULE_ACTIVITY);
            //完成所有任务check
            int allFinishRewardNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ActivityConstants.DJRW_ALL_REWARD_MARK);
            if (allFinishRewardNum == 0) {
                boolean isAllFinish = true;
                ZhdDjrwTemplate zhdDjrwTemplateFinal = djrwTemplates.get(djrwTemplates.size() - 1);
                for (ZhdDjrwTemplate.ZhdDjrwCheck zhdDjrwCheck1 : zhdDjrwTemplateFinal.getChk()) {
                    int markId1 = zhdDjrwCheck1.getMARK();
                    int markId1Num = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), markId1);
                    if (markId1Num < zhdDjrwCheck1.getNUM()) {
                        isAllFinish = false;
                        break;
                    }
                }
                if (isAllFinish) {
                    sendDjjwReward(playingRole.getId(), zhdDjrwTemplateFinal);
                    AwardUtils.changeRes(playingRole, ActivityConstants.DJRW_ALL_REWARD_MARK, 1, LogConstants.MODULE_ACTIVITY);
                }
            }
        }
    }

    private void sendDjjwReward(int playerId, ZhdDjrwTemplate zhdDjrwTemplate) {
        String mailTitle = "Phần thưởng hoạt động điểm danh"; //"点将任务活动奖励"
        String mailContent = "Bạn đã hoàn thành hoạt động điểm danh: %1$s, phần thưởng như sau:"; //"您完成了点将任务活动：%1$s，奖励如下：";
        mailContent = String.format(mailContent, zhdDjrwTemplate.getName());
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : zhdDjrwTemplate.getItems()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
    }

    private void sendReward(String mailTitle, String mailContent, int playerId, ZhdDjjfTemplate.ZhdDjjfMission zhdDjjfMission) {
        mailContent = String.format(mailContent, zhdDjjfMission.getNAME());
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : zhdDjjfMission.getITEMS()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
    }

    public void resetWeekActMark() {
        logger.info("week reset start-------------------------");
        List<Integer> resetGsids = new ArrayList<>();
        resetGsids.add(ActivityConstants.GXDB_PROGRESS_MARK);
        resetGsids.add(ActivityConstants.DJJF_PROGRESS_MARK);
        resetGsids.add(ActivityConstants.DJRW_ALL_REWARD_MARK);
        resetGsids.add(ActivityConstants.ZMJF_PROGRESS_MARK);
        //点将任务重置
        List<ZhdDjrwTemplate> djrwTemplates = ActivityWeekTemplateCache.getInstance().getDjrwTemplates();
        for (ZhdDjrwTemplate zhdDjrwTemplate : djrwTemplates) {
            List<ZhdDjrwTemplate.ZhdDjrwCheck> chk = zhdDjrwTemplate.getChk();
            for (ZhdDjrwTemplate.ZhdDjrwCheck zhdDjrwCheck : chk) {
                resetGsids.add(zhdDjrwCheck.getMARK());
            }
        }
        //点将积分重置
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        for (ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent : jfbxTemplate.getEvent()) {
            resetGsids.add(zhdJfbxEvent.getMARK());
        }
        //探囊取物重置
        ZhdTnqwTemplate tnqwTemplate = ActivityWeekTemplateCache.getInstance().getTnqwTemplate();
        for(ZhdTnqwTemplate.ZhdTnqwEvent zhdTnqwEvent : tnqwTemplate.getEvent()){
            resetGsids.add(zhdTnqwEvent.getMark());
        }
        Map<Integer, PlayingRole> onlinePlayers = PlayerOnlineCacheMng.getInstance().getOnlinePlayers();
        logger.info("update online cache!size={}", onlinePlayers.size());
        for (Map.Entry<Integer, PlayingRole> aEntry : onlinePlayers.entrySet()) {
            PlayingRole playingRole = aEntry.getValue();
            for (int otherPropGsid : resetGsids) {
                AwardUtils.setRes(playingRole, otherPropGsid, 0, true);
            }
        }
        int affectSize = PlayerOtherDao.getInstance().resetOtherCount(StringUtils.join(resetGsids, ","));
        logger.info("db reset:gsid={},size={}", resetGsids, affectSize);
        logger.info("week reset end-------------------------");
    }

    public void resetWeekActBean() {
        ActMjbgCache.getInstance().clearActMjbg();
        ActMjbgDaoHelper.asyncTruncateMjbg();
        ActCxryCache.getInstance().clearActCxry();
        ActCxryDaoHelper.asyncTruncateCxry();
        ActTnqwCache.getInstance().clearActTnqw();
        ActTnqwDaoHelper.asyncTruncateTnqw();
    }

    /**
     * 观星，探囊取物活动
     */
    public void spinTnqw(PlayingRole playingRole, int buy_type, int addCount) {
        ZhdBean tnqwAct = ActivityWeekCache.getInstance().getZhd("TNQW");
        Date now = new Date();
        if (tnqwAct == null || tnqwAct.getStartTime().after(now) || tnqwAct.getEndTime().before(now)) {
            return;
        }
        //
        ZhdTnqwTemplate tnqwTemplate = ActivityWeekTemplateCache.getInstance().getTnqwTemplate();
        int tnqwMarkGsid;
        int markMaxCount;
        if (buy_type == 1) {
            tnqwMarkGsid = tnqwTemplate.getEvent().get(0).getMark();
            markMaxCount = tnqwTemplate.getEvent().get(0).getLimit();
        } else {
            tnqwMarkGsid = tnqwTemplate.getEvent().get(1).getMark();
            markMaxCount = tnqwTemplate.getEvent().get(1).getLimit();
            addCount *= 2;
        }
        int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), tnqwMarkGsid);
        if (markCount >= markMaxCount) {
            return;
        }
        //add mark
        AwardUtils.changeRes(playingRole, tnqwMarkGsid, addCount, LogConstants.MODULE_ACTIVITY);
    }

    public int[] mjbgHangReward() {
        ZhdBean mjbgAct = ActivityWeekCache.getInstance().getZhd("MJBG");
        Date now = new Date();
        if (mjbgAct == null || mjbgAct.getStartTime().after(now) || mjbgAct.getEndTime().before(now)) {
            return null;
        }
        return MJBG_HANG_REWARD;
    }

    /**
     * 狂欢商店，六月狂欢币掉落
     *
     * @return
     */
    public int[] gjdlHangReward() {
        ZhdBean gjdlAct = ActivityWeekCache.getInstance().getZhd("GJDL");
        Date now = new Date();
        if (gjdlAct == null || gjdlAct.getStartTime().after(now) || gjdlAct.getEndTime().before(now)) {
            return null;
        }
        return GJDL_HANG_REWARD;
    }

    public void jfbxAddScore(PlayingRole playingRole, Integer gsid, Integer count) {
        ZhdBean jfbxAct = ActivityWeekCache.getInstance().getZhd("JFBX");
        Date now = new Date();
        if (jfbxAct == null || jfbxAct.getStartTime().after(now) || jfbxAct.getEndTime().before(now)) {
            return;
        }
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent = jfbxTemplate.getEvent().get(2);
        Integer maxNum = zhdJfbxEvent.getLIMIT();
        int currentNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), gsid);
        if (currentNum >= maxNum) {
            return;
        }
        int oldScore = getJfbxScore(playingRole);
        if (currentNum + count > maxNum) {
            count = maxNum - currentNum;
        }
        AwardUtils.changeRes(playingRole, gsid, count, LogConstants.MODULE_ACTIVITY);
        jfbxMissionCheck(playingRole, oldScore);
    }

    public void jfbxGeneralExchange(PlayingRole playingRole) {
        ZhdBean jfbxAct = ActivityWeekCache.getInstance().getZhd("JFBX");
        Date now = new Date();
        if (jfbxAct == null || jfbxAct.getStartTime().after(now) || jfbxAct.getEndTime().before(now)) {
            return;
        }
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent = jfbxTemplate.getEvent().get(0);
        Integer maxNum = zhdJfbxEvent.getLIMIT();
        int gsid = zhdJfbxEvent.getMARK();
        int currentNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), gsid);
        if (currentNum >= maxNum) {
            return;
        }
        int oldScore = getJfbxScore(playingRole);
        AwardUtils.changeRes(playingRole, gsid, 1, LogConstants.MODULE_ACTIVITY);
        jfbxMissionCheck(playingRole, oldScore);
    }

    public void jfbxCostDjl(PlayingRole playingRole, int costNum) {
        ZhdBean jfbxAct = ActivityWeekCache.getInstance().getZhd("JFBX");
        Date now = new Date();
        if (jfbxAct == null || jfbxAct.getStartTime().after(now) || jfbxAct.getEndTime().before(now)) {
            return;
        }
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent = jfbxTemplate.getEvent().get(1);
        Integer maxNum = zhdJfbxEvent.getLIMIT();
        int gsid = zhdJfbxEvent.getMARK();
        int currentNum = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), gsid);
        if (currentNum >= maxNum) {
            return;
        }
        int oldScore = getJfbxScore(playingRole);
        if (currentNum + costNum > maxNum) {
            costNum = maxNum - currentNum;
        }
        AwardUtils.changeRes(playingRole, gsid, costNum, LogConstants.MODULE_ACTIVITY);
        jfbxMissionCheck(playingRole, oldScore);
    }

    private void jfbxMissionCheck(PlayingRole playingRole, int oldScore) {
        int newScore = getJfbxScore(playingRole);
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        List<ZhdJfbxTemplate.ZhdJfbxBox> box = jfbxTemplate.getBox();
        for (ZhdJfbxTemplate.ZhdJfbxBox zhdJfbxBox : box) {
            Integer score = zhdJfbxBox.getSCORE();
            if (oldScore < score && newScore >= score) {
                sendJfbxReward(playingRole, zhdJfbxBox);
                break;
            }
        }
    }

    private void sendJfbxReward(PlayingRole playingRole, ZhdJfbxTemplate.ZhdJfbxBox zhdJfbxBox) {
        String mailTitle = "Bảo rương bí mật"; //"神秘宝箱活动奖励"
        String mailContent = "Bạn đã hoàn thành hoạt động Bảo rương bí mật: %1$d, phần thưởng như sau:"; //"您完成了神秘宝箱活动积分：%1$d，奖励如下：";
        mailContent = String.format(mailContent, zhdJfbxBox.getSCORE());
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : zhdJfbxBox.getREWARD()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        MailManager.getInstance().sendSysMailToSingle(playingRole.getId(), mailTitle, mailContent, mailAtt);
    }

    private int getJfbxScore(PlayingRole playingRole) {
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        int markNumSum = 0;
        for (ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent : jfbxTemplate.getEvent()) {
            int marCount1 = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), zhdJfbxEvent.getMARK());
            markNumSum += marCount1;
        }
        return markNumSum;
    }

    public void pubDrawAddGeneral(int playerId, int gsid) {
        ZhdBean cxryAct = ActivityWeekCache.getInstance().getZhd("ZMQY");
        Date now = new Date();
        if (cxryAct == null || cxryAct.getStartTime().after(now) || cxryAct.getEndTime().before(now)) {
            return;
        }
        ActCxry actCxry = ActCxryCache.getInstance().getActCxry(playerId);
        if (actCxry == null) {
            return;
        }
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        if (heroTemplate.getSTAR() < 5) {
            return;
        }
        Map<Integer, Integer> wishGenerals = actCxry.getWishGenerals();
        if (wishGenerals.containsKey(gsid)) {
            wishGenerals.put(gsid, 1);
            actCxry.setNum(0);
        } else {
            int sum = actCxry.getNum() + 1;
            ZhdCxryTemplate cxryTemplate = ActivityWeekTemplateCache.getInstance().getCxryTemplate();
            if (sum > cxryTemplate.getZfmax()) {
                sum = cxryTemplate.getZfmax();
            }
            actCxry.setNum(sum);
        }
        ActCxryDaoHelper.asyncUpdateActCxry(actCxry);
    }

    public void cxryAddRate(RandomDispatcher<Integer> rd, int playerId) {
        ZhdBean cxryAct = ActivityWeekCache.getInstance().getZhd("ZMQY");
        Date now = new Date();
        if (cxryAct == null || cxryAct.getStartTime().after(now) || cxryAct.getEndTime().before(now)) {
            return;
        }
        ActCxry actCxry = ActCxryCache.getInstance().getActCxry(playerId);
        if (actCxry == null) {
            return;
        }
        Integer rateNum = actCxry.getNum();
        int finalRate = rateNum * 200;
        Map<Integer, Integer> wishGenerals = actCxry.getWishGenerals();
        for (Map.Entry<Integer, Integer> aEntry : wishGenerals.entrySet()) {
            if (aEntry.getValue() > 0) {
                continue;
            }
            int generalTemplateId = aEntry.getKey();
            Integer chipTemplateId = GeneralChipTemplateCache.getInstance().getChipTemplateByGeneralId(generalTemplateId);
            rd.put(finalRate, chipTemplateId);
        }
    }

}
