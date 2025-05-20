package game.module.exped.logic;

import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.PowerFormation;
import game.module.chapter.dao.PowerFormationCache;
import game.module.chapter.logic.PowerFormationManager;
import game.module.exped.bean.ExpedBean;
import game.module.exped.bean.ExpedPlayer;
import game.module.exped.dao.ExpedCache;
import game.module.exped.dao.ExpedDaoHelper;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.template.GeneralTemplate;
import game.module.user.logic.PlayerHeadManager;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author HeXuhui
 */
public class ExpedManager {

    private static Logger logger = LoggerFactory.getLogger(ExpedManager.class);

    public ExpedPlayer generateCheckpointEnemy(PlayingRole playingRole) {
        int expedProgress = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), ExpedConstants.EXPED_PROGRESS_MARK);
        //根据战力选择
        Integer myPower = playingRole.getPlayerBean().getPower();
        int targetPower = (int) (myPower * 0.6f + expedProgress * 0.02f);
        int powerFormationNum = PowerFormationManager.getInstance().getPowerFormationNum(targetPower);
        List<PowerFormation> toSelectFormations = new ArrayList<>();
        for (int i = powerFormationNum + 2; i >= powerFormationNum - 2 && i >= 0; i--) {
            Queue<PowerFormation> powerFormationByIndex = PowerFormationCache.getInstance().getPowerFormationByIndex(i);
            if (powerFormationByIndex != null) {
                toSelectFormations.addAll(powerFormationByIndex);
            }
        }
        //选择玩家阵型
        if (toSelectFormations.size() > 0) {
            PowerFormation powerFormation = toSelectFormations.get(RandomUtils.nextInt(0, toSelectFormations.size()));
            ExpedPlayer expedPlayer = new ExpedPlayer();
            expedPlayer.setRname(powerFormation.getName());
            expedPlayer.setLevel(powerFormation.getLevel());
            expedPlayer.setIconid(powerFormation.getIconId());
            expedPlayer.setHeadid(powerFormation.getHeadId());
            expedPlayer.setFrameid(powerFormation.getFrameId());
            expedPlayer.setPower(powerFormation.getPower());
            expedPlayer.setDbBattleset(powerFormation.getDbBattleset());
            return expedPlayer;
        } else {
            //选择机器人
            Integer playerLevel = playingRole.getPlayerBean().getLevel();
            int robotLevel = (int) (playerLevel * 0.7f + expedProgress * (280 - playerLevel * 0.7f) / 30f);
            Map<Integer, BattlePlayerBase> battlePlayerMap = FormationRobotManager.getInstance().generateRobot(robotLevel);
            ExpedPlayer expedPlayer = new ExpedPlayer();
            BattlePlayerBase next = battlePlayerMap.values().iterator().next();
            int gsid = next.getGsid();
            GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
            String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
            expedPlayer.setRname(rname);
            expedPlayer.setLevel(robotLevel);
            int headid = PlayerHeadManager.getInstance().getHeadid(gsid);
            int iconId = PlayerHeadManager.getInstance().headId2IconId(headid);
            expedPlayer.setIconid(iconId);
            expedPlayer.setHeadid(headid);
            expedPlayer.setFrameid(51001);
            expedPlayer.setPower(FormationRobotManager.getInstance().getPower(battlePlayerMap));
            expedPlayer.setBattlePlayerMap(battlePlayerMap);
            return expedPlayer;
        }
    }

    static class SingletonHolder {

        static ExpedManager instance = new ExpedManager();


    }

    public static ExpedManager getInstance() {
        return SingletonHolder.instance;
    }

    public ExpedBean createExped(int playerId) {
        ExpedBean expedBean = new ExpedBean();
        expedBean.setPlayerId(playerId);
        expedBean.setWishCount(Arrays.asList(0, 0, 0));
        expedBean.setMyHp(new HashMap<>());
        Date now = new Date();
        expedBean.setLastResetTime(now);
        return expedBean;
    }

    public void expedResetCheck(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ExpedBean expedBean = ExpedCache.getInstance().getExped(playerId);
        if (expedBean == null || expedBean.getLastResetTime() == null) {
            return;
        }
        Date lastResetTime = expedBean.getLastResetTime();
        int dayNum = getDayNum(lastResetTime);
        Date now = new Date();
        int nowDayNum = getDayNum(now);
        if (dayNum != nowDayNum) {
            expedReset(playingRole);
        }
    }

    public void expedReset(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ExpedBean expedBean = ExpedCache.getInstance().getExped(playerId);
        //wish reset
        List<Integer> wishCount = expedBean.getWishCount();
        for (int i = 0; i < 3; i++) {
            wishCount.set(i, 0);
        }
        //hps
        if (expedBean.getMyHp() != null) {
            expedBean.getMyHp().clear();
        }
        expedBean.setEnemyHp(null);
        expedBean.setCheckpointEnemy(null);
        Date now = new Date();
        expedBean.setLastResetTime(now);
        //save bean
        ExpedDaoHelper.asyncUpdateExped(expedBean);
        //reset count
        AwardUtils.setRes(playingRole, ExpedConstants.EXPED_PROGRESS_MARK, 0, true);
        //reset wish count
        AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_WISH_MARK, 0, true);
        //reset treat and relive count
        AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_TREAT_MARK, 0, true);
        AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_REVIVE_MARK, 0, true);
    }

    private int getDayNum(Date lastResetTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastResetTime);
        int weekCount = calendar.get(Calendar.WEEK_OF_YEAR);
        int dayOfWeek = DateUtils.truncate(calendar, Calendar.DATE).get(Calendar.DAY_OF_WEEK);
        int dayNum = 0;
        for (int i = 0; i < ExpedConstants.EXPED_RESET_DAY.length; i++) {
            if (dayOfWeek >= ExpedConstants.EXPED_RESET_DAY[i]) {
                dayNum++;
            }
        }
        return weekCount * 3 + dayNum;
    }

}
