package game.module.bigbattle.logic;

import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.friend.logic.FriendConstants;
import game.module.hero.logic.GeneralConstants;
import game.module.item.logic.ItemConstants;
import game.module.legion.logic.LegionConstants;
import game.module.manor.logic.ManorConstants;
import game.module.manor.logic.OfficialConstants;
import game.module.pvp.logic.PvpConstants;
import game.module.template.ZhdTgslTemplate;
import game.module.user.dao.PlayerOtherDao;
import game.module.user.dao.PlayerServerPropDao;
import game.module.user.logic.PlayerServerPropManager;
import game.session.PlayerOnlineCacheMng;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Admin
 */
public class DayResetManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(DayResetManager.class);

    static class SingletonHolder {

        static DayResetManager instance = new DayResetManager();
    }

    public static DayResetManager getInstance() {
        return SingletonHolder.instance;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("day reset job start-------------------------");
        List<Integer> resetGsids = new ArrayList<>();
        for (BigBattleConstants.BigBattleCountGsid bigBattleCountGsid : BigBattleConstants.BIG_BATTLE_COUNT_CONFIG) {
            resetGsids.add(bigBattleCountGsid.FREE);
            resetGsids.add(bigBattleCountGsid.COUNTGSID);
        }
        resetGsids.add(PvpConstants.PVP_FREE_ATTACK_COUNT_MARK);
        resetGsids.add(GeneralConstants.WU_HUN_COUNT_MARK);
        resetGsids.add(OfficialConstants.OFFICIAL_SALARY_GET_MARK);
        resetGsids.add(ManorConstants.MANOR_BUY_COUNT_MARK);
        resetGsids.add(ManorConstants.MANOR_PATROL_COUNT_MARK);
        resetGsids.add(FriendConstants.FRIEND_EXPLORE_CHAPTER_COUNT_MARK);
        resetGsids.add(LegionConstants.LEGION_SIGN_MARK);
        resetGsids.add(LegionConstants.LEGION_FACTORY_DONATE_MARK);
        resetGsids.add(LegionConstants.LEGION_BOSS_ATTACK_MARK);
        resetGsids.add(LegionConstants.WORLD_BOSS_ATTACK_MARK);
        resetGsids.add(ItemConstants.GZ_YUEKA_GET_MARK);
        resetGsids.add(ItemConstants.ZZ_YUEKA_GET_MARK);
        ZhdTgslTemplate tgslTemplate = ActivityWeekTemplateCache.getInstance().getTgslTemplate();
        if (tgslTemplate != null) {
            resetGsids.add(tgslTemplate.getBoss().getMark());
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
        //parse server cache
        List<Integer> resetServerProp = new ArrayList<>();
        resetServerProp.add(ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK);
        for (Map.Entry<Integer, PlayingRole> aEntry : onlinePlayers.entrySet()) {
            PlayingRole playingRole = aEntry.getValue();
            for (int serverPropGsid : resetServerProp) {
                PlayerServerPropManager.getInstance().setServerProp(playingRole, serverPropGsid, 0, true);
            }
        }
        affectSize = PlayerServerPropDao.getInstance().resetCount(StringUtils.join(resetServerProp, ","));
        logger.info("serverProp db reset:gsid={},size={}", resetServerProp, affectSize);
        logger.info("day reset job end-------------------------");
    }

    private void resetOtherProp1(Map<Integer, PlayingRole> onlinePlayers, int otherPropGsid) {
        for (Map.Entry<Integer, PlayingRole> aEntry : onlinePlayers.entrySet()) {
            PlayingRole playingRole = aEntry.getValue();
            AwardUtils.setRes(playingRole, otherPropGsid, 0, true);
        }
        int affectSize = PlayerOtherDao.getInstance().resetOtherCount(String.valueOf(otherPropGsid));
        logger.info("db reset:{},size={}", otherPropGsid, affectSize);
    }

}
