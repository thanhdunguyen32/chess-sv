package game.module.mission.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.logic.ChapterManager;
import game.module.item.dao.EquipTemplateCache;
import game.module.item.dao.TreasureTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.mission.bean.MissionDaily;
import game.module.mission.constants.MissionConstants;
import game.module.mission.dao.MissionDailyCache;
import game.module.mission.dao.MissionDailyDao;
import game.module.mission.dao.MissionDailyTemplateCache;
import game.module.template.EquipTemplate;
import game.module.template.MissionDailyTemplate;
import game.module.template.TreasureTemplate;
import game.module.user.bean.PlayerBean;
import game.module.user.logic.PlayerManager;
import game.module.user.logic.PlayerServerPropManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @author Admin
 */
public class MissionManager {

    private static Logger logger = LoggerFactory.getLogger(MissionManager.class);

    static class SingletonHolder {

        static MissionManager instance = new MissionManager();
    }

    public static MissionManager getInstance() {
        return SingletonHolder.instance;
    }

    public void loginUpdateMissionDaily(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        MissionDaily missionDaily = MissionDailyCache.getInstance().getPlayerMissionDaily(playerId);
        Date updateTime = null;
        if (missionDaily != null) {
            updateTime = missionDaily.getUpdateTime();
        }
        Date now = new Date();
        if (updateTime == null || !DateUtils.isSameDay(updateTime, now)) {
            //clear mission mark
            List<MissionDailyTemplate> allMissionDaily = MissionDailyTemplateCache.getInstance().getAllMissionDaily();
            for (MissionDailyTemplate missionDailyTemplate : allMissionDaily) {
                //award not get
                Integer getmark = missionDailyTemplate.getGETMARK();
                AwardUtils.setRes(playingRole, getmark, 0, false);
                //update progress
                String pmark = missionDailyTemplate.getPMARK();
                if (StringUtils.isNumeric(pmark)) {
                    int pmarkGsid = Integer.parseInt(pmark);
                    AwardUtils.setRes(playingRole, pmarkGsid, 0, false);
                }
            }
            //save bean
            if (missionDaily == null) {
                missionDaily = new MissionDaily();
                missionDaily.setPlayerId(playerId);
                missionDaily.setUpdateTime(now);
                MissionDailyCache.getInstance().addMissionDaily(missionDaily);
                MissionDaily finalMissionDaily = missionDaily;
                GameServer.executorService.execute(() -> MissionDailyDao.getInstance().addMissionDaily(finalMissionDaily));
            } else {
                missionDaily.setUpdateTime(now);
                MissionDaily finalMissionDaily1 = missionDaily;
                GameServer.executorService.execute(() -> MissionDailyDao.getInstance().updateMissionDaily(finalMissionDaily1));
            }
        }
    }

    public boolean checkMissionFinish(PlayerBean playerBean, String pmark, Integer cnum) {
        boolean ret = false;
        if (StringUtils.isNumeric(pmark)) {
            int myCount = ItemManager.getInstance().getCount(playerBean, Integer.parseInt(pmark));
            ret = myCount >= cnum;
        } else {
            switch (pmark) {
                case "login":
                    ret = 1 >= cnum;
                    break;
                case "level":
                    int mylevel = playerBean.getLevel();
                    ret = mylevel >= cnum;
                    break;
                case "legion":
                    long myLegionId = LegionManager.getInstance().getLegionId(playerBean.getId());
                    ret = myLegionId > 0;
                    break;
                case "maxmapid":
                    ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerBean.getId());
                    int maxmapid = ChapterManager.getInstance().getInitMapId();
                    if (chapterBean != null) {
                        maxmapid = chapterBean.getMaxMapId();
                    }
                    ret = maxmapid > cnum;
                    break;
                case "tower":
                    int finishTower = PlayerServerPropManager.getInstance().getTower(playerBean.getId()) - 1;
                    ret = finishTower >= cnum;
                    break;
                case "dgtopchapter":
//                    var o = this.dgtop.pass, l = 0;
//                    (o || 0 == o) && (l = o + 1);
//                    s = l;
                    ret = false;
                    break;
            }
        }
        return ret;
    }

    public void generalLevelup(PlayingRole playingRole, Integer generalLevel) {
        int playerId = playingRole.getId();
        int oldLevel = PlayerManager.getInstance().getOtherCount(playerId, MissionConstants.GENERAL_LEVELUP);
        if (generalLevel > oldLevel) {
            AwardUtils.setRes(playingRole, MissionConstants.GENERAL_LEVELUP, generalLevel, true);
        }
    }

    public void updatePowerMission(PlayingRole playingRole, int myPower) {
        AwardUtils.setRes(playingRole, MissionConstants.MISSION_POWER_MARK, myPower, true);
    }

    public void equipChangeProgress(PlayingRole playingRole, int gsid, int itemCount) {
        EquipTemplate equipTemplate = EquipTemplateCache.getInstance().getEquipTemplateById(gsid);
        switch (equipTemplate.getQUALITY()) {
            case 3:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_3_EQUIP, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 4:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_4_EQUIP, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 5:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_5_EQUIP, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 6:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_6_EQUIP, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
        }
    }

    public void treasureChangeProgress(PlayingRole playingRole, int gsid, int itemCount) {
        TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(gsid);
        switch (treasureTemplate.getQUALITY()) {
            case 4:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_4_TREASURE, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 5:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_5_TREASURE, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 6:
                AwardUtils.changeRes(playingRole, MissionConstants.QUALITY_6_TREASURE, itemCount, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
        }
    }

    public void treasureStarChangeProgress(PlayingRole playingRole, Integer treasureTemplateId) {
        TreasureTemplate treasureTemplate = TreasureTemplateCache.getInstance().getTreasureTemplateById(treasureTemplateId);
        switch (treasureTemplate.getSTAR()) {
            case 2:
                AwardUtils.changeRes(playingRole, MissionConstants.TREASURE_STAR_2, 1, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 3:
                AwardUtils.changeRes(playingRole, MissionConstants.TREASURE_STAR_3, 1, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 4:
                AwardUtils.changeRes(playingRole, MissionConstants.TREASURE_STAR_4, 1, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 5:
                AwardUtils.changeRes(playingRole, MissionConstants.TREASURE_STAR_5, 1, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
            case 6:
                AwardUtils.changeRes(playingRole, MissionConstants.TREASURE_STAR_6, 1, LogConstants.MODULE_ACHIEVE_MISSION);
                break;
        }
    }

    public void generalStarChangeProgress(PlayingRole playingRole, int generalStar){
        switch (generalStar) {
            case 4:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_4, 1, LogConstants.MODULE_GENERAL);
                break;
            case 5:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_5, 1, LogConstants.MODULE_GENERAL);
                break;
            case 6:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_6, 1, LogConstants.MODULE_GENERAL);
                break;
            case 7:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_7, 1, LogConstants.MODULE_GENERAL);
                break;
            case 8:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_8, 1, LogConstants.MODULE_GENERAL);
                break;
            case 9:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_9, 1, LogConstants.MODULE_GENERAL);
                break;
            case 10:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_10, 1, LogConstants.MODULE_GENERAL);
                break;
            case 11:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_11, 1, LogConstants.MODULE_GENERAL);
                break;
            case 12:
                AwardUtils.changeRes(playingRole, MissionConstants.GENERAL_STAR_12, 1, LogConstants.MODULE_GENERAL);
                break;
        }
    }

}
