package game.module.activity_month.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity_month.dao.MActivityFinalTemplateCache;
import game.module.activity_month.dao.MActivityTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mail.logic.MailManager;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pay.logic.LibaoBuyManager;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.season.dao.SeasonDaoHelper;
import game.module.template.MActivityFinalTemplate;
import game.module.template.MActivityTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import game.module.user.dao.PlayerOtherDao;
import game.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.*;

public class ActivityMonthManager {

    private static Logger logger = LoggerFactory.getLogger(ActivityMonthManager.class);

    static class SingletonHolder {
        static ActivityMonthManager instance = new ActivityMonthManager();
    }

    public static ActivityMonthManager getInstance() {
        return SingletonHolder.instance;
    }

    public Date getYdhdEndTime() {
        Date now = new Date();
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        if (battleSeason.getMonthEndTime() == null || battleSeason.getMonthEndTime().before(now)) {
            Date monthEndTime = refreshMonthEndTime();;
            battleSeason.setMonthEndTime(monthEndTime);
            SeasonDaoHelper.asyncUpdateMonthEndTime(monthEndTime);
            //月度购买记录刷新
            LibaoBuyManager.getInstance().resetMonthBuyInfo();
            resetMonthActivity();
        }
        return battleSeason.getMonthEndTime();
    }

    public Date refreshMonthEndTime(){
        Date now = new Date();
        Date openTime = GameServer.getInstance().getServerConfig().getOpenTime();
        int diffDay = (int) ((now.getTime() - openTime.getTime()) / 24 / 3600000);
        int diffMonth = diffDay / 30;
        return DateUtils.addDays(openTime, (diffMonth + 1) * 30);
    }

    public void resetMonthActivity() {
        logger.info("resetMonthActivity!");
        Set<Integer> markList = new HashSet<>();
        List<MActivityTemplate> affairsTemplateMap = MActivityTemplateCache.getInstance().getAffairsTemplateMap();
        for (MActivityTemplate mActivityTemplate : affairsTemplateMap) {
            Integer rmark = mActivityTemplate.getRMARK();
            markList.add(rmark);
            markList.add(mActivityTemplate.getPROCESS());
        }
        List<MActivityTemplate> expedTemplateMap = MActivityTemplateCache.getInstance().getExpedTemplateMap();
        for (MActivityTemplate mActivityTemplate : expedTemplateMap) {
            Integer rmark = mActivityTemplate.getRMARK();
            markList.add(rmark);
            markList.add(mActivityTemplate.getPROCESS());
        }
        List<MActivityTemplate> gStarTemplateMap = MActivityTemplateCache.getInstance().getgStarTemplateMap();
        for (MActivityTemplate mActivityTemplate : gStarTemplateMap) {
            Integer rmark = mActivityTemplate.getRMARK();
            markList.add(rmark);
        }
        List<MActivityTemplate> pvpTemplateMap = MActivityTemplateCache.getInstance().getPvpTemplateMap();
        for (MActivityTemplate mActivityTemplate : pvpTemplateMap) {
            Integer rmark = mActivityTemplate.getRMARK();
            markList.add(rmark);
            markList.add(mActivityTemplate.getPROCESS());
        }
        List<MActivityFinalTemplate> mActivityFinalTemplates = MActivityFinalTemplateCache.getInstance().getMActivityFinalTemplate();
        for (MActivityFinalTemplate mActivityFinalTemplate : mActivityFinalTemplates){
            Integer nMark = mActivityFinalTemplate.getRMARK().get("N");
            markList.add(nMark);
            Integer sMark = mActivityFinalTemplate.getRMARK().get("S");
            markList.add(sMark);
        }
        resetMarks(markList);
    }

    private void resetMarks(Set<Integer> markList) {
        Map<Integer, Map<Integer, PlayerProp>> markCacheAll = PlayerOtherCache.getInstance().getAll();
        for (Map.Entry<Integer, Map<Integer, PlayerProp>> aEntry : markCacheAll.entrySet()) {
            Integer playerId = aEntry.getKey();
            Map<Integer, PlayerProp> otherCache = aEntry.getValue();
            for (int aMarkId : markList) {
                otherCache.remove(aMarkId);
            }
            PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
            if (playingRole != null && playingRole.isChannelActive()) {
                for (int aMarkId : markList) {
                    // push item change
                    WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(aMarkId, 0);
                    playingRole.writeAndFlush(pushMsg.build(playingRole.alloc()));
                }
            }
        }
        //
        int affectSize = PlayerOtherDao.getInstance().resetOtherCount(StringUtils.join(markList, ","));
        logger.info("db reset:gsid={},size={}", markList, affectSize);
    }

    public void affairFinish(PlayingRole playingRole, Integer affairStar) {
        if (affairStar < 4) {
            return;
        }
        int activityIndex = affairStar - 4;
        MActivityTemplate mActivityTemplate = MActivityTemplateCache.getInstance().getAffairsTemplateMap().get(activityIndex);
        Integer processMark = mActivityTemplate.getPROCESS();
        AwardUtils.changeRes(playingRole, processMark, 1, LogConstants.MODULE_ACTIVITY_MONTH);
        //check finish
        int progressCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), processMark);
        int rewardMark = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), mActivityTemplate.getRMARK());
        if (progressCount >= mActivityTemplate.getNUM() && rewardMark == 0) {
            //reward
            sendRewardMail(playingRole.getId(), "Thưởng mục tiêu nội vụ", "Chúc mừng Chúa đã đạt được mục tiêu nội vụ" + mActivityTemplate.getNAME() + "Để đạt được mục tiêu, Quân sư 3Q rất vui mừng, và đặc biệt ban hành một chỉ định ban thưởng cho lãnh chúa. Xin hãy nhận những phần thưởng này！",
                    mActivityTemplate.getITEMS());
            AwardUtils.changeRes(playingRole, mActivityTemplate.getRMARK(), 1, LogConstants.MODULE_ACTIVITY_MONTH);
            mActivityFinal(playingRole, 2);
        }
    }

    public void expedFinish(PlayingRole playingRole, int expedLevel) {
        if (expedLevel != 15 && expedLevel != 30) {
            return;
        }
        List<MActivityTemplate> expedTemplateMap = MActivityTemplateCache.getInstance().getExpedTemplateMap();
        Integer processMark;
        if (expedLevel == 15) {
            processMark = expedTemplateMap.get(0).getPROCESS();
        } else {
            processMark = expedTemplateMap.get(3).getPROCESS();
        }
        AwardUtils.changeRes(playingRole, processMark, 1, LogConstants.MODULE_ACTIVITY_MONTH);
        //check finish
        int progressCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), processMark);
        for (MActivityTemplate mActivityTemplate : expedTemplateMap) {
            if(!mActivityTemplate.getPROCESS().equals(processMark)) {
                continue;
            }
            int rewardMark = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), mActivityTemplate.getRMARK());
            if (progressCount >= mActivityTemplate.getNUM() && rewardMark == 0) {
                //reward
                sendRewardMail(playingRole.getId(), "Phần thưởng mục tiêu thử nghiệm thám hiểm", "Chúc mừng Chúa đã hoàn thành thử thách thám hiểm" + mActivityTemplate.getNAME() + "Để đạt được mục tiêu, Quân sư 3Q rất vui mừng, và đặc biệt ban hành một chỉ định ban thưởng cho lãnh chúa. Xin hãy nhận những phần thưởng này！",
                        mActivityTemplate.getITEMS());
                AwardUtils.changeRes(playingRole, mActivityTemplate.getRMARK(), 1, LogConstants.MODULE_ACTIVITY_MONTH);
                mActivityFinal(playingRole, 0);
            }
        }
    }

    public void generalCompose(PlayingRole playingRole, int gstar) {
        List<MActivityTemplate> gStarTemplateMap = MActivityTemplateCache.getInstance().getgStarTemplateMap();
        for (MActivityTemplate mActivityTemplate : gStarTemplateMap) {
            if (gstar == mActivityTemplate.getSTAR()) {
                AwardUtils.changeRes(playingRole, mActivityTemplate.getRMARK(), 1, LogConstants.MODULE_ACTIVITY_MONTH);
                int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), mActivityTemplate.getRMARK());
                if (markCount <= mActivityTemplate.getNUM()) {
                    //reward
                    sendRewardMail(playingRole.getId(), "Mục tiêu trở thành thần của tướng quân được khen thưởng", "Chúc mừng Chúa đã thành tướng, thành thần" + mActivityTemplate.getNAME() + "Để đạt được mục tiêu, Quân sư 3Q rất vui mừng, và đặc biệt ban hành một chỉ định ban thưởng cho lãnh chúa. Xin hãy nhận những phần thưởng này！",
                            mActivityTemplate.getITEMS());
                    mActivityFinal(playingRole, 1);
                }
            }
        }
    }

    public void pvpBattleEnd(PlayingRole playingRole, boolean isWin, boolean isKingPvp) {
        List<MActivityTemplate> pvpTemplateMap = MActivityTemplateCache.getInstance().getPvpTemplateMap();
        Integer processMark = pvpTemplateMap.get(0).getPROCESS();
        if (isKingPvp) {
            processMark = pvpTemplateMap.get(4).getPROCESS();
        }
        AwardUtils.changeRes(playingRole, processMark, isWin ? 2 : 1, LogConstants.MODULE_ACTIVITY_MONTH);
        //check finish
        int progressCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), processMark);
        for (MActivityTemplate mActivityTemplate : pvpTemplateMap) {
            if (!mActivityTemplate.getPROCESS().equals(processMark)) {
                continue;
            }
            int rewardMark = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), mActivityTemplate.getRMARK());
            if (progressCount >= mActivityTemplate.getNUM() && rewardMark == 0) {
                //reward
                sendRewardMail(playingRole.getId(), "Phần thưởng mục tiêu thi đấu võ thuật", "Chúc mừng chúa tể đã đạt đến cuộc thi võ thuật" + mActivityTemplate.getNAME() + "Để đạt được mục tiêu, Quân sư 3Q rất vui mừng, và đặc biệt ban hành một chỉ định ban thưởng cho lãnh chúa. Xin hãy nhận những phần thưởng này！",
                        mActivityTemplate.getITEMS());
                AwardUtils.changeRes(playingRole, mActivityTemplate.getRMARK(), 1, LogConstants.MODULE_ACTIVITY_MONTH);
                mActivityFinal(playingRole, 3);
            }
        }
    }

    private void sendRewardMail(int playerId, String title, String content, List<RewardTemplateSimple> rewards) {
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : rewards) {
            int gsid = rewardTemplateSimple.getGSID();
            int count = rewardTemplateSimple.getCOUNT();
            if (mailAtt.containsKey(gsid)) {
                mailAtt.put(gsid, mailAtt.get(gsid) + count);
            } else {
                mailAtt.put(gsid, count);
            }
        }
        MailManager.getInstance().sendSysMailToSingle(playerId, title, content, mailAtt);
    }

    private void mActivityFinal(PlayingRole playingRole, int levelIndex) {
        MActivityFinalTemplate mActivityFinalTemplate = MActivityFinalTemplateCache.getInstance().getMActivityFinalTemplate(levelIndex);
        List<Object> chk = mActivityFinalTemplate.getCHK();
        if (chk.get(0) instanceof Integer) {
            for (Object chkMarkObj : chk) {
                Integer chkMark = (Integer) chkMarkObj;
                int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), chkMark);
                if (markCount == 0) {
                    return;
                }
            }
        } else if (chk.get(0) instanceof Map) {
            for (Object chkMarkObj : chk) {
                Map<String, Integer> aMap = (Map<String, Integer>) chkMarkObj;
                Integer chkMark = aMap.get("GSID");
                Integer chkMarkCount = aMap.get("COUNT");
                int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), chkMark);
                if (markCount < chkMarkCount) {
                    return;
                }
            }
        }
        //is reward
        Integer nMark = mActivityFinalTemplate.getRMARK().get("N");
        if (ItemManager.getInstance().getCount(playingRole.getPlayerBean(), nMark) > 0) {
            return;
        }
        //do reward
        List<RewardTemplateSimple> rewardItems = mActivityFinalTemplate.getITEMS().get("N");
        List<RewardTemplateSimple> rewardsAll = new ArrayList<>(rewardItems);
        //add S rewards
        if(ChargeInfoManager.getInstance().isYdAddon(playingRole.getId())){
            List<RewardTemplateSimple> rewardItemsS = mActivityFinalTemplate.getITEMS().get("S");
            rewardsAll.addAll(rewardItemsS);
        }
        sendRewardMail(playingRole.getId(), "Phần thưởng mục tiêu nhiệm vụ hàng tháng", "xin chúc mừng chúa" + mActivityFinalTemplate.getNAME() + "Để đạt được mục tiêu, Quân sư 3Q rất vui mừng, và đặc biệt ban hành một chỉ định ban thưởng cho lãnh chúa. Xin hãy nhận những phần thưởng này！",
                rewardsAll);
        //add mark
        AwardUtils.changeRes(playingRole, nMark, 1, LogConstants.MODULE_ACTIVITY_MONTH);
        if (ChargeInfoManager.getInstance().isYdAddon(playingRole.getId())) {
            Integer sMark = mActivityFinalTemplate.getRMARK().get("S");
            AwardUtils.changeRes(playingRole, sMark, 1, LogConstants.MODULE_ACTIVITY_MONTH);
        }
    }

}
