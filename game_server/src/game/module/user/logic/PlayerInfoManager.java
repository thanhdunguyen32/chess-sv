package game.module.user.logic;

import game.entity.PlayingRole;
import game.module.activity.logic.ActivityManager;
import game.module.activity.logic.ActivityRecordManager;
import game.module.activity.logic.ActivityXiangouManager;
import game.module.award.bean.GameConfig;
import game.module.guide.logic.GuideManager;
import game.module.mission.logic.MissionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pay.logic.ChargeInfoManager;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerLevelTemplateCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Date;

public class PlayerInfoManager {

    private static Logger logger = LoggerFactory.getLogger(PlayerInfoManager.class);

    static class SingletonHolder {
        static PlayerInfoManager instance = new PlayerInfoManager();
    }

    public static PlayerInfoManager getInstance() {
        return SingletonHolder.instance;
    }

    public int addExp(PlayingRole playingRole, int expValue) {
        logger.info("add exp,player={},val={}",playingRole.getId(), expValue);
        PlayerBean playerBean = playingRole.getPlayerBean();
        if (playerBean == null) {
            logger.error("playerBean not exist!");
            return 0;
        }
        Date now = new Date();
        int currentLevel = playerBean.getLevel();
        if (currentLevel >= PlayerLevelTemplateCache.getInstance().getMaxLevel()) {
            return 0;
        }
        int levelExp = playerBean.getLevelExp();
        int newExpVal = expValue + levelExp;
        int nextLevelExp = PlayerLevelTemplateCache.getInstance().getConfigByLevel(currentLevel);
        int newLevel = currentLevel;
        while (newExpVal >= nextLevelExp) {
            Integer nextExpTemplate = PlayerLevelTemplateCache.getInstance().getConfigByLevel(newLevel + 1);
            if (nextExpTemplate == null) {
                break;
            }
            // 升级增加属性
            upgrade(playingRole, nextLevelExp, now);
            newLevel++;
            if (newLevel == PlayerLevelTemplateCache.getInstance().getMaxLevel()) {
                newExpVal = 0;
            } else {
                newExpVal -= nextLevelExp;
            }
            nextLevelExp = nextExpTemplate;
        }
        playerBean.setLevel(newLevel);
        playerBean.setLevelExp(newExpVal);
        // save
        // asyncUpdatePlayerBean(playerBean);
        // send msg
        if (newLevel > currentLevel) {
            logger.info("player upgrade,id={},level={}", playerBean.getId(), newLevel);
            try {
                // 更新缓存
                PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playerBean.getId());
                if (poc != null) {
                    poc.setLevel(newLevel);
                }
                // 新手引导
                GuideManager.getInstance().upgradeSendItems(playingRole, newLevel, currentLevel);
                // GuideManager.getInstance().upgradeGuideCheck(playingRole,currentLevel,
                // newLevel);
                // 副本开启check
//				StagePveManager.getInstance().upgradeCheck(playingRole, newLevel);
                WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.LEVEL, newLevel);
                playingRole.getGamePlayer().write(retMsg.build(playingRole.alloc()));
                //
                ActivityXiangouManager.getInstance().levelXiaogou(playerBean.getId(), currentLevel, newLevel);
                ChargeInfoManager.getInstance().levelUpAddCzjj(currentLevel, newLevel, playerBean.getId());
            } catch (Exception e) {
                logger.error("upgrade guide check!", e);
            }
        }
        int currentLevelSumExp = PlayerLevelTemplateCache.getInstance().getPlayerNextExpByLv(playerBean.getLevel() - 1);
        WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.EXP, currentLevelSumExp + newExpVal);
        playingRole.getGamePlayer().write(retMsg.build(playingRole.alloc()));
        return newLevel - currentLevel;
    }

    /**
     * 奖励体力
     *
     * @param expTemplate
     * @param now
     */
    public void upgrade(PlayingRole playingRole, Integer expTemplate, Date now) {
//		List<List<Integer>> rewards = expTemplate.get();
//		if(rewards != null) {
//			for (List<Integer> list : rewards) {
//				AwardUtils.changeRes(playingRole, list.get(0), list.get(1), list.get(2), LogConstants.MODULE_PLAYER_INIT);
//			}
//		}
        playingRole.getPlayerBean().setUpgradeTime(now);
    }

    public void saveMaxPower(PlayingRole playingRole, int newMaxBattleForce) {
        int currentMaxBf = playingRole.getPlayerBean().getPower();
        if (newMaxBattleForce > currentMaxBf) {
            logger.info("set new max power,power={},playerId={}", newMaxBattleForce, playingRole.getId());
            playingRole.getPlayerBean().setPower(newMaxBattleForce);
            //push power
            WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100010, newMaxBattleForce);
            playingRole.write(pushmsg.build(playingRole.alloc()));
            //update mission progress
            MissionManager.getInstance().updatePowerMission(playingRole, newMaxBattleForce);
        }
    }

    public void changeMoney(PlayingRole playingRole, Integer changeMoney, int module_id) {
        logger.info("change money,player={},val={}",playingRole.getId(), changeMoney);
        int playerMoney = playingRole.getPlayerBean().getMoney();
        playerMoney += changeMoney;
        playingRole.getPlayerBean().setMoney(playerMoney >= 0 ? playerMoney : 0);
        if (changeMoney < 0) {
            ActivityManager.getInstance().consome(playingRole, -changeMoney);
            ActivityRecordManager.getInstance().xiaoFeiBang(playingRole.getId(), -changeMoney);
        }
        // ret
        WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.YB, playingRole.getPlayerBean().getMoney());
        playingRole.getGamePlayer().write(retMsg.build(playingRole.alloc()));
    }

    public void addCopper(PlayingRole playingRole, int changeCoins) {
        logger.info("change copper,player={},val={}",playingRole.getId(), changeCoins);
        int playerCoins = playingRole.getPlayerBean().getGold();
        playerCoins += changeCoins;
        playingRole.getPlayerBean().setGold(playerCoins >= 0 ? playerCoins : 0);
        // ret
        WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.GOLD, playingRole.getPlayerBean().getGold());
        playingRole.write(retMsg.build(playingRole.alloc()));
    }

    /**
     * 更新VIP
     *
     * @param playingRole
     */
    public void updateVipLevAndExp(final PlayingRole playingRole, int vipLev, int vipExp) {
        if (vipExp > 0) {
            // 更新VIP等级和经验
            playingRole.getPlayerBean().setVipLevel(vipLev);
            playingRole.getPlayerBean().setVipExp(vipExp);
            // 更新缓存
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playingRole.getId());
            if (poc != null) {
                poc.setVipLevel(vipLev);
            }
            // ret
            WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.VIPEXP, vipExp);
            playingRole.write(retMsg.build(playingRole.alloc()));
            logger.info("玩家:{},vip_lev={},vip_exp={}", playingRole.getId(), vipLev, vipExp);
        }
    }

    public static boolean checkResource(int resType, int itemType, int itemCount, PlayerBean playerBean) {
        int retval = 0;
        switch (resType) {
//		case GameEnum.REWARD_TYPE.MONEY:
//			switch (itemType) {
//			case GameEnum.REWARD_MONEY.EXP:
//				retval = playerBean.getLevelExp();
//				break;
//			case GameEnum.REWARD_MONEY.VIPEXP:
//				// VIP经验
//				retval = playerBean.getVipExp();
//				break;
//			case GameEnum.REWARD_MONEY.GOLD:
//				retval = playerBean.getJade();
//				break;
//			case GameEnum.REWARD_MONEY.POWER:
//				retval = 0;
//				break;
//			default:
//				break;
//			}
//			break;
            default:
                break;
        }
        return retval >= itemCount;
    }

}
