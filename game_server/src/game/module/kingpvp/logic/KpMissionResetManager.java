package game.module.kingpvp.logic;

import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.bigbattle.logic.BigBattleConstants;
import game.module.friend.logic.FriendConstants;
import game.module.hero.logic.GeneralConstants;
import game.module.item.logic.ItemConstants;
import game.module.kingpvp.dao.KingPvpTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.manor.logic.ManorConstants;
import game.module.manor.logic.OfficialConstants;
import game.module.pvp.logic.PvpConstants;
import game.module.template.KingMissionDailyTemplate;
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
public class KpMissionResetManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(KpMissionResetManager.class);

    static class SingletonHolder {

        static KpMissionResetManager instance = new KpMissionResetManager();
    }

    public static KpMissionResetManager getInstance() {
        return SingletonHolder.instance;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("kp mission reset job start-------------------------");
        List<Integer> resetGsids = new ArrayList<>();
        List<KingMissionDailyTemplate> missionDailyTemplates = KingPvpTemplateCache.getInstance().getMissionDailyTemplates();
        for (KingMissionDailyTemplate kingMissionDailyTemplate : missionDailyTemplates){
            resetGsids.add(kingMissionDailyTemplate.getPMARK());
            resetGsids.add(kingMissionDailyTemplate.getGETMARK());
        }
        Map<Integer, PlayingRole> onlinePlayers = PlayerOnlineCacheMng.getInstance().getOnlinePlayers();
        logger.info("online cache!size={}", onlinePlayers.size());
        for (Map.Entry<Integer, PlayingRole> aEntry : onlinePlayers.entrySet()) {
            PlayingRole playingRole = aEntry.getValue();
            for (int otherPropGsid : resetGsids) {
                AwardUtils.setRes(playingRole, otherPropGsid, 0, true);
            }
        }
        int affectSize = PlayerOtherDao.getInstance().resetOtherCount(StringUtils.join(resetGsids, ","));
        logger.info("db reset:gsid={},size={}", resetGsids, affectSize);
        logger.info("kp mission reset job end-------------------------");
    }

}
