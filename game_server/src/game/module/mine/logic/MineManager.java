package game.module.mine.logic;

import game.entity.PlayingRole;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.bean.DbBattleset;
import game.module.chapter.dao.BattleFormationCache;
import game.module.exped.logic.FormationRobotManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.mine.bean.*;
import game.module.mine.dao.MineCache;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.template.GeneralTemplate;
import game.module.user.dao.CommonTemplateCache;
import game.module.user.logic.PlayerHeadManager;
import game.session.SessionManager;
import io.netty.util.Timeout;
import lion.math.RandomDispatcher;
import lion.session.GlobalTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageMine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MineManager {

	private static Logger logger = LoggerFactory.getLogger(MineManager.class);

	static class SingletonHolder {
		static MineManager instance = new MineManager();
	}

	public static MineManager getInstance() {
		return SingletonHolder.instance;
	}

	public Map<Integer, Long> mineRandKey = new ConcurrentHashMap<>();

	public Map<Integer, Timeout> scheduleMineFinishMap = new ConcurrentHashMap<>();

	public synchronized DBMine createMineEntity(int robotLevelIndex, int robotPointIndex) {
		logger.info("createMineEntity,robotLevelIndex={},robotPointIndex={}", robotLevelIndex, robotPointIndex);
		DBMine minebuilder = new DBMine();
		List<DBMineLevel> levelList = new ArrayList<>();
		minebuilder.setLevels(levelList);
		List<Integer> mineLevelConfig = (List<Integer>) CommonTemplateCache.getInstance().getConfig("mine_levels");
		for (int i = 0; i < mineLevelConfig.size() - 1; i++) {
			DBMineLevel levelBulder = new DBMineLevel();
			levelList.add(levelBulder);
			List<DBMinePoint> minePoints = new ArrayList<>();
			levelBulder.setMinePoints(minePoints);
			for (int j = 0; j < MineConstants.PAGE_SIZE * MineConstants.POINT_SIZE_1_PAGE; j++) {
				DBMinePoint minePointBuilder = new DBMinePoint();
				minePoints.add(minePointBuilder);
				minePointBuilder.setPlayerId(0);
				if (robotLevelIndex == i && robotPointIndex == j) {
					// 创建机器人队伍
					createMinePointRobots(i, j, minePointBuilder);
				}
			}
		}
		MineDaoHelper.asyncInsertMineEntity(minebuilder);
		MineCache.getInstance().setMineEntity(minebuilder);
		return minebuilder;
	}

	public void createMinePointRobots(int robotLevelIndex, int robotPointIndex, DBMinePoint minePointBuilder) {
		logger.info("create mine point robots!level_index={},point_index={}", robotLevelIndex, robotPointIndex);
		List<Integer> mineLevelConfig = (List<Integer>) CommonTemplateCache.getInstance().getConfig("mine_levels");
		int minLevel = mineLevelConfig.get(robotLevelIndex)*3;
		int robotLevel = minLevel;
		int perPageIndex = robotPointIndex % MineConstants.POINT_SIZE_1_PAGE;
		if (perPageIndex == 0) {
			robotLevel = (int) (minLevel * 1.2f);
		} else if (perPageIndex >= 4) {
			robotLevel = (int) (minLevel * 0.8f);
		}
		Map<Integer, BattlePlayerBase> battlePlayerMap = FormationRobotManager.getInstance().generateRobot(robotLevel);
		BattlePlayerBase next = battlePlayerMap.values().iterator().next();
		int gsid = next.getGsid();
		GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
		String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
		minePointBuilder.setRname(rname);
		minePointBuilder.setLevel(robotLevel);
		int headid = PlayerHeadManager.getInstance().getHeadid(gsid);
		int iconId = PlayerHeadManager.getInstance().headId2IconId(headid);
		minePointBuilder.setIconid(iconId);
		minePointBuilder.setHeadid(headid);
		minePointBuilder.setFrameid(51001);
		minePointBuilder.setPower(FormationRobotManager.getInstance().getPower(battlePlayerMap));
		minePointBuilder.setBattlePlayerMap(battlePlayerMap);
	}

	public void putBattleRandKey(int playerId, long randkey) {
		mineRandKey.put(playerId, randkey);
	}

	public void removeBattleRandKey(int playerId) {
		mineRandKey.remove(playerId);
	}

	public long getRandKey(int playerId) {
		return mineRandKey.get(playerId);
	}

	public int genereteMintPoint(int levelIndex, int pointIndex) {
		return levelIndex * 100 + pointIndex;
	}

	public int[] seperateMinePoint(int pointVal) {
		return new int[] { pointVal / 100, pointVal % 100 };
	}

	public void limitBattleRecord(DBMinePlayer playerBuilder) {
		while (playerBuilder.getBattleRecord().size() > MineConstants.MAX_RECORD_SIZE) {
			playerBuilder.getBattleRecord().remove(0);
		}
	}

	public void addScheduleTimeout(int pointVal, Timeout aTimeout) {
		scheduleMineFinishMap.put(pointVal, aTimeout);
	}

	public Timeout getScheduleTimeout(int pointVal) {
		return scheduleMineFinishMap.get(pointVal);
	}

	public synchronized void mineSendReward(int minePointVal) {
		int[] mineParams = seperateMinePoint(minePointVal);
		int level_index = mineParams[0];
		int point_index = mineParams[1];
		DBMine mineEntityBuilder = MineCache.getInstance().getDBMine();
		DBMinePoint minePointBuilder = mineEntityBuilder.getLevels().get(level_index)
				.getMinePoints().get(point_index);
		if (minePointBuilder.getPlayerId() > 0) {
			minePointResetAndAward(mineEntityBuilder, level_index, point_index, minePointBuilder, minePointVal);
			// save
			MineCache.getInstance().setMineEntity(mineEntityBuilder);
		}
	}

	private void minePointResetAndAward(DBMine mineEntityBuilder, int level_index, int point_index,
			DBMinePoint minePointBuilder, int minePointVal) {
		// 发奖励
		List<List<Integer>> mine_hour_coins = (List<List<Integer>>) CommonTemplateCache.getInstance()
				.getConfig("mine_hour_coins");
		List<List<Number>> mine_hour_gold = (List<List<Number>>) CommonTemplateCache.getInstance()
				.getConfig("mine_hour_gold");
		List<Integer> mine_hour_coins1 = mine_hour_coins.get(level_index);
		List<Number> mine_hour_gold1 = mine_hour_gold.get(level_index);
		int rewardCoins = 0;
		float rewardGold = 0;
		int pointPerPage = point_index % MineConstants.POINT_SIZE_1_PAGE;
		if (pointPerPage == 0) {
			rewardCoins = mine_hour_coins1.get(2);
			rewardGold = mine_hour_gold1.get(2).floatValue();
		} else if (pointPerPage < 4) {
			rewardCoins = mine_hour_coins1.get(1);
			rewardGold = mine_hour_gold1.get(1).floatValue();
		} else {
			rewardCoins = mine_hour_coins1.get(0);
			rewardGold = mine_hour_gold1.get(0).floatValue();
		}
		// 2个小时奖励
		rewardCoins *= MineConstants.MINE_OWN_HOUR;
		rewardGold *= MineConstants.MINE_OWN_HOUR;
		// 发放奖励
		int playerId = minePointBuilder.getPlayerId();
		DBMinePlayer minePlayerEntity = mineEntityBuilder.getPlayers().get(playerId);
		if (minePlayerEntity == null) {
			minePlayerEntity = new DBMinePlayer();
			minePlayerEntity.setGains(new ArrayList<>());
			minePlayerEntity.setOwnMinePoint(new ArrayList<>());
			mineEntityBuilder.getPlayers().put(playerId,minePlayerEntity);
		}
		List<Integer> gains = minePlayerEntity.getGains();
		if(gains == null){
			gains = new ArrayList<>();
			minePlayerEntity.setGains(gains);
		}
		int gainCount = gains.size();
		if (gainCount == 0) {
			gains.add(rewardCoins);
			gains.add((int) rewardGold);
		} else {
			gains.set(0, gains.get(0) + rewardCoins);
			gains.set(1, gains.get(1) + (int) rewardGold);
		}
		// 清除占领
		List<Integer> ownList = minePlayerEntity.getOwnMinePoint();
		int reserveMinePointVal = -1;
		for (Integer aMinePoint : ownList) {
			if (minePointVal != aMinePoint) {
				reserveMinePointVal = aMinePoint;
			}
		}
		minePlayerEntity.getOwnMinePoint().clear();
		if (reserveMinePointVal >-1) {
			minePlayerEntity.getOwnMinePoint().add(reserveMinePointVal);
		}
		// 保存战报
		if(minePlayerEntity.getBattleRecord() == null){
			minePlayerEntity.setBattleRecord(new ArrayList<>());
		}
		DBMineBattleRecord dbMineBattleRecord = new DBMineBattleRecord();
		minePlayerEntity.getBattleRecord().add(dbMineBattleRecord);
		dbMineBattleRecord.setPositive(true);
		dbMineBattleRecord.setIsSuccess(1);
		dbMineBattleRecord.setMinePoint(minePointVal);
		dbMineBattleRecord.setTargetPlayerId(0);
		dbMineBattleRecord.setType(2);
		dbMineBattleRecord.setGains(new HashMap<>());
		if (rewardCoins > 0) {
			dbMineBattleRecord.getGains().put(GameConfig.PLAYER.GOLD, rewardCoins);
		}
		if (rewardGold > 0) {
			dbMineBattleRecord.getGains().put(GameConfig.PLAYER.YB, (int) rewardGold);
		}
		dbMineBattleRecord.setAddTime(System.currentTimeMillis());
		MineManager.getInstance().limitBattleRecord(minePlayerEntity);
		//
		MineCache.getInstance().setMinePlayer(playerId, minePlayerEntity);
		// 更新据点信息
		minePointBuilder.setPlayerId(0);
		minePointBuilder.setDbBattleset(null);
		minePointBuilder.setFinishTime(0L);
//		.clearDefenceFormation().clearFinishTime();
		createMinePointRobots(level_index, point_index, minePointBuilder);
	}

	public WsMessageMine.S2CMineGetAward mineGetAward(PlayingRole playingRole) {
		int awardDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_MINE);
		int playerId = playingRole.getId();
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		DBMinePlayer minePlayerEntity = mineEntity.getPlayers().get(playerId);
		List<WsMessageBase.IORewardItem> retGainList = new ArrayList<>();
		List<Integer> gainList = minePlayerEntity.getGains();
		if (gainList.size() > 0) {
			int rewardCoins = gainList.get(0);
			int reawrdGold = gainList.get(1);
			if (rewardCoins > 0) {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoins*awardDouble, LogConstants.MODULE_MINE);
				retGainList.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoins*awardDouble));
			}
			if (reawrdGold > 0) {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, reawrdGold*awardDouble, LogConstants.MODULE_MINE);
				retGainList.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.YB, reawrdGold*awardDouble));
			}
		}
		// 清空收益
		gainList.clear();
		// 保存数据
		MineCache.getInstance().setMinePlayer(playerId, minePlayerEntity);
		MineCache.getInstance().setMineEntity(mineEntity);
		//ret
		WsMessageMine.S2CMineGetAward respMsg = new WsMessageMine.S2CMineGetAward();
		respMsg.gain = retGainList;
		return respMsg;
	}

	public void serverStartupInit(DBMine mineEntity) {
		if (mineEntity == null) {
			return;
		}
		int level_index = 0;
		for (DBMineLevel levelBuilder : mineEntity.getLevels()) {
			int point_index = 0;
			for (DBMinePoint pointBuilder : levelBuilder.getMinePoints()) {
				int playerId = pointBuilder.getPlayerId();
				if (playerId > 0) {
					// 过期
					long finishTime = pointBuilder.getFinishTime();
					long currentMili = System.currentTimeMillis();
					int minePointVal = genereteMintPoint(level_index, point_index);
					if (currentMili > finishTime) {
						minePointResetAndAward(mineEntity, level_index, point_index, pointBuilder, minePointVal);
					} else {
						// 添加job
						int diffSeconds = (int) ((finishTime - currentMili) / 1000);
						// 定时发送奖励
						Timeout newTimeout = GlobalTimer.getInstance().newTimeout(new TimerTaskMineReward(minePointVal),
								diffSeconds);
						MineManager.getInstance().addScheduleTimeout(minePointVal, newTimeout);
					}
				}
				point_index++;
			}
			level_index++;
		}
		Map<Integer,DBMinePlayer> playerMap = mineEntity.getPlayers();
		if(playerMap != null) {
			MineCache.getInstance().putMinePlayerAll(playerMap);
		}
		MineCache.getInstance().setMineEntity(mineEntity);
	}
	
	public void repairData() {
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		serverStartupInit(mineEntity);
	}
	
	public void resetPlayerData(int playerId) {
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		if (mineEntity == null) {
			return;
		}
		DBMinePlayer minePlayer = mineEntity.getPlayers().get(playerId);
		// 清空我拥有
		if (minePlayer != null && minePlayer.getOwnMinePoint() != null) {
			minePlayer.getOwnMinePoint().clear();
		}
		// 删除矿点信息
		List<DBMineLevel> levelList = mineEntity.getLevels();
		int level_index = 0;
		for (DBMineLevel levelBuilder : levelList) {
			int point_index = 0;
			List<DBMinePoint> pointList = levelBuilder.getMinePoints();
			for (DBMinePoint minePointBuilder : pointList) {
				if (minePointBuilder.getPlayerId() == playerId) {
					minePointBuilder.setPlayerId(0);
					minePointBuilder.setDbBattleset(null);
					minePointBuilder.setFinishTime(0L);
					createMinePointRobots(level_index, point_index, minePointBuilder);
				}
				point_index++;
			}
			level_index++;
		}
		MineCache.getInstance().setMineEntity(mineEntity);
	}
	
	public synchronized WsMessageBattle.S2CMineBattleEnd battleCalculate(PlayingRole playingRole, int btl_result, int level_index,
																			   int point_index, int minePointVal) {
		int awardDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_MINE);
		WsMessageBattle.S2CMineBattleEnd respMsg = new WsMessageBattle.S2CMineBattleEnd();
		respMsg.level_index = level_index;
		respMsg.point_index = point_index;
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		int playerId = playingRole.getId();
		List<Integer> mine_energy_cost_config = (List<Integer>) CommonTemplateCache.getInstance()
				.getConfig("mine_energy_cost");
		int winCostEnergy = mine_energy_cost_config.get(0);
		int looseCostEnergy = mine_energy_cost_config.get(1);
		List<WsMessageBase.IORewardItem> battleGain = new ArrayList<>();
		// 胜利次数
		if (btl_result > 0) {
			DBMinePoint minePointBuilder = mineEntity.getLevels().get(level_index)
					.getMinePoints().get(point_index);
			int targetPlayerId = minePointBuilder.getPlayerId();
			// 玩家
			if (targetPlayerId > 0) {
				// 占领时长
				long finishTime = minePointBuilder.getFinishTime();
				int occupySeconds = (int) (System.currentTimeMillis() + MineConstants.MINE_OWN_HOUR * 60 * 60 * 1000
						- finishTime) / 1000;
				// 我方奖励
				int rewardCoinsSum = 0;
				int rewardGoldSum = 0;
				if (occupySeconds > 0) {
					List<List<Integer>> mine_hour_coins = (List<List<Integer>>) CommonTemplateCache.getInstance()
							.getConfig("mine_hour_coins");
					List<List<Number>> mine_hour_gold = (List<List<Number>>) CommonTemplateCache.getInstance()
							.getConfig("mine_hour_gold");
					List<Integer> mine_hour_coins1 = mine_hour_coins.get(level_index);
					List<Number> mine_hour_gold1 = mine_hour_gold.get(level_index);
					int rewardCoins = 0;
					float rewardGold = 0;
					int pointPerPage = point_index % MineConstants.POINT_SIZE_1_PAGE;
					if (pointPerPage == 0) {
						rewardCoins = mine_hour_coins1.get(2);
						rewardGold = mine_hour_gold1.get(2).floatValue();
					} else if (pointPerPage < 4) {
						rewardCoins = mine_hour_coins1.get(1);
						rewardGold = mine_hour_gold1.get(1).floatValue();
					} else {
						rewardCoins = mine_hour_coins1.get(0);
						rewardGold = mine_hour_gold1.get(0).floatValue();
					}
					rewardCoinsSum = rewardCoins * occupySeconds / 3600;
					rewardGoldSum = (int) (rewardGold * occupySeconds / 3600);
					if (rewardCoinsSum > 0) {
						AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoinsSum*awardDouble, 0,
								LogConstants.MODULE_MINE, (byte) 0);
						battleGain.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoinsSum*awardDouble));
					}
					if (rewardGoldSum > 0) {
						AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, rewardGoldSum*awardDouble, 0,
								LogConstants.MODULE_MINE, (byte) 0);
						battleGain.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.YB, rewardGoldSum*awardDouble));
					}
				}
				// 我的信息
				updateMyPoint(mineEntity, minePointBuilder, playerId, targetPlayerId, minePointVal, rewardCoinsSum,
						rewardGoldSum);
				// 敌方占领信息
				DBMinePlayer mineTargetPlayer = mineEntity.getPlayers().get(targetPlayerId);
				if (mineTargetPlayer == null) {
					mineTargetPlayer = new DBMinePlayer();
					mineTargetPlayer.setOwnMinePoint(new ArrayList<>());
					mineTargetPlayer.setBattleRecord(new ArrayList<>());
					mineTargetPlayer.setGains(new ArrayList<>());
					mineEntity.getPlayers().put(targetPlayerId,mineTargetPlayer);
				}
				List<Integer> ownList = mineTargetPlayer.getOwnMinePoint();
				int reserveMinePointVal = -1;
				for (Integer aMinePoint : ownList) {
					if (minePointVal != aMinePoint) {
						reserveMinePointVal = aMinePoint;
					}
				}
				mineTargetPlayer.getOwnMinePoint().clear();
				if (reserveMinePointVal > -1) {
					mineTargetPlayer.getOwnMinePoint().add(reserveMinePointVal);
				}
				// 敌方战斗记录
				DBMineBattleRecord targetRecordBuilder = new DBMineBattleRecord();
				targetRecordBuilder.setPositive(false);
				targetRecordBuilder.setIsSuccess(0);
				targetRecordBuilder.setMinePoint(minePointVal);
				targetRecordBuilder.setTargetPlayerId(playerId);
				targetRecordBuilder.setType(1);
				targetRecordBuilder.setGains(new HashMap<>());
				if (rewardCoinsSum > 0) {
					targetRecordBuilder.getGains().put(GameConfig.PLAYER.GOLD, rewardCoinsSum);
				}
				if (rewardGoldSum > 0) {
					targetRecordBuilder.getGains().put(GameConfig.PLAYER.YB, rewardGoldSum);
				}
				targetRecordBuilder.setAddTime(System.currentTimeMillis());
				// 最多10条
				MineManager.getInstance().limitBattleRecord(mineTargetPlayer);
				MineCache.getInstance().setMinePlayer(targetPlayerId, mineTargetPlayer);
				//
				MineCache.getInstance().setMineEntity(mineEntity);
				// 敌方推送
				PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
				if (targetPr != null) {
					WsMessageMine.PushMineRob pushMineRob = new WsMessageMine.PushMineRob();
					pushMineRob.level_index = level_index;
					pushMineRob.point_index = point_index;
					pushMineRob.target_player_id = playerId;
					pushMineRob.target_player_name = playingRole.getPlayerBean().getName();
					pushMineRob.loose_items = battleGain;
					targetPr.writeAndFlush(pushMineRob.build(targetPr.alloc()));
				}
			} else {// 机器人
				// 发奖励
				List<List<Integer>> mine_hour_coins = (List<List<Integer>>) CommonTemplateCache.getInstance()
						.getConfig("mine_hour_coins");
				List<List<Number>> mine_hour_gold = (List<List<Number>>) CommonTemplateCache.getInstance()
						.getConfig("mine_hour_gold");
				List<Integer> mine_hour_coins1 = mine_hour_coins.get(level_index);
				List<Number> mine_hour_gold1 = mine_hour_gold.get(level_index);
				int rewardCoins = 0;
				float reardGold = 0;
				int pointPerPage = point_index % MineConstants.POINT_SIZE_1_PAGE;
				if (pointPerPage == 0) {
					rewardCoins = mine_hour_coins1.get(2);
					reardGold = mine_hour_gold1.get(2).floatValue();
				} else if (pointPerPage < 4) {
					rewardCoins = mine_hour_coins1.get(1);
					reardGold = mine_hour_gold1.get(1).floatValue();
					;
				} else {
					rewardCoins = mine_hour_coins1.get(0);
					reardGold = mine_hour_gold1.get(0).floatValue();
					;
				}
				// 铜币2个小时奖励
				rewardCoins *= 2;
				// 金块2个小时奖励
				reardGold *= 2;
				if (rewardCoins > 0) {
					AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoins*awardDouble, 0,
							LogConstants.MODULE_MINE, (byte) 0);
					battleGain.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoins*awardDouble));
				}
				int rewardGoldInt = Math.round(reardGold);
				if (rewardGoldInt > 0) {
					AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, rewardGoldInt*awardDouble, 0,
							LogConstants.MODULE_MINE, (byte) 0);
					battleGain.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.YB, rewardGoldInt*awardDouble));
				}
				updateMyPoint(mineEntity, minePointBuilder, playerId, 0, minePointVal, rewardCoins, rewardGoldInt);
				//
				MineCache.getInstance().setMineEntity(mineEntity);
			}
			// 定时发送奖励
			Timeout aTimeout = MineManager.getInstance().getScheduleTimeout(minePointVal);
			if (aTimeout != null) {
				aTimeout.cancel();
			}
			Timeout newTimeout = GlobalTimer.getInstance().newTimeout(new TimerTaskMineReward(minePointVal),
					MineConstants.MINE_OWN_HOUR * 60 * 60);
			MineManager.getInstance().addScheduleTimeout(minePointVal, newTimeout);
			// 扣体力
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -winCostEnergy, 0,
					LogConstants.MODULE_MINE, (byte) 0);
		} else {
			// 对方添加记录
			DBMinePoint minePointEntity = mineEntity.getLevels().get(level_index).getMinePoints().get(point_index);
			int targetPlayerId = minePointEntity.getPlayerId();
			if (targetPlayerId > 0) {
				// 对方添加记录
				DBMinePlayer targetMinePlayer = mineEntity.getPlayers().get(targetPlayerId);
				if (targetMinePlayer == null) {
					targetMinePlayer = new DBMinePlayer();
					targetMinePlayer.setBattleRecord(new ArrayList<>());
					targetMinePlayer.setGains(new ArrayList<>());
					targetMinePlayer.setOwnMinePoint(new ArrayList<>());
					mineEntity.getPlayers().put(playerId,targetMinePlayer);
				}
				DBMineBattleRecord dbMineBattleRecord = new DBMineBattleRecord();
				targetMinePlayer.getBattleRecord().add(dbMineBattleRecord);
				dbMineBattleRecord.setPositive(false);
				dbMineBattleRecord.setIsSuccess(1);
				dbMineBattleRecord.setMinePoint(minePointVal);
				dbMineBattleRecord.setTargetPlayerId(playerId);
				dbMineBattleRecord.setType(1);
				dbMineBattleRecord.setAddTime(System.currentTimeMillis());
				// 最多10条
				MineManager.getInstance().limitBattleRecord(targetMinePlayer);
//				MineCache.getInstance().setMinePlayer(targetPlayerId, targetMinePlayer);
				MineCache.getInstance().setMineEntity(mineEntity);
			}
		}
		respMsg.reward = battleGain;
		return respMsg;
	}
	
	private void updateMyPoint(DBMine mineBuilder, DBMinePoint minePointBuilder, int playerId, int targetPlayerId,
			int minePointVal, int rewardCoins, int rewardGold) {
		// 更新矿点信息
		minePointBuilder.setPlayerId(playerId);
		// 到期时间
		long finishTime = System.currentTimeMillis() + MineConstants.MINE_OWN_HOUR * 60 * 60 * 1000;
		minePointBuilder.setFinishTime(finishTime);
		// 防守阵容
		int mineFormationIndex = 3;
		BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
		Map<Integer, Long> pvpattFormation = battleFormation.getPvpatt();
		// 我的占领信息
		if(mineBuilder.getPlayers() == null){
			mineBuilder.setPlayers(new HashMap<>());
		}
		DBMinePlayer minePlayer = mineBuilder.getPlayers().get(playerId);
		if (minePlayer == null) {
			minePlayer = new DBMinePlayer();
			minePlayer.setOwnMinePoint(new ArrayList<>());
			minePlayer.setGains(new ArrayList<>());
			minePlayer.setBattleRecord(new ArrayList<>());
			mineBuilder.getPlayers().put(playerId,minePlayer);
		}
		Map<Long,GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
		//已经有占领的矿藏
		if(minePlayer.getOwnMinePoint() != null && minePlayer.getOwnMinePoint().size()>0) {
			List<Long> excludeCards = new ArrayList<>();
			int existMinePointVal = minePlayer.getOwnMinePoint().get(0);
			int[] existMinePos = MineManager.getInstance().seperateMinePoint(existMinePointVal);
			DBMinePoint existMinePoint = mineBuilder.getLevels().get(existMinePos[0]).getMinePoints().get(existMinePos[1]);
			if(existMinePoint.getDbBattleset() != null && existMinePoint.getDbBattleset().getTeam() != null) {
				for (GeneralBean generalBean : existMinePoint.getDbBattleset().getTeam().values()) {
					excludeCards.add(generalBean.getUuid());
				}
			}
			//排除已经防守的卡
			List<GeneralBean> toSelectCards = new ArrayList<>();
			for(GeneralBean ce : generalAll.values()) {
				if(!excludeCards.contains(ce.getUuid())) {
					toSelectCards.add(ce);
				}
			}
			toSelectCards.sort((o1, o2) -> o2.getPower() - o1.getPower());
			//set formation
			DbBattleset dbBattleset = new DbBattleset();
			//mythic
			Map<Integer, Integer> mythics = battleFormation.getMythics();
			if(mythics != null && mythics.containsKey(mineFormationIndex)){
				Integer mythicId = mythics.get(mineFormationIndex);
				MythicalAnimal playerMythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicId);
				dbBattleset.setMythic(playerMythicalAnimal);
			}
			//team
			Map<Integer, GeneralBean> generalBeanMap = new HashMap<>();
			RandomDispatcher<Integer> formationPosDispatcher = new RandomDispatcher<>();
			for (int i = 0; i < 32; i++) {
				formationPosDispatcher.put(1, i);
			}
			for (int i = 0; i < 10 && i < toSelectCards.size(); i++) {
				GeneralBean generalBean = toSelectCards.get(i);
				Integer formationPos = formationPosDispatcher.randomRemove();
				generalBeanMap.put(formationPos, generalBean);
			}
			dbBattleset.setTeam(generalBeanMap);
			minePointBuilder.setDbBattleset(dbBattleset);
		}else {
			DbBattleset dbBattleset = new DbBattleset();
			//mythic
			Map<Integer, Integer> mythics = battleFormation.getMythics();
			if(mythics != null && mythics.containsKey(mineFormationIndex)){
				Integer mythicId = mythics.get(mineFormationIndex);
				MythicalAnimal playerMythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicId);
				dbBattleset.setMythic(playerMythicalAnimal);
			}
			//team
			Map<Integer, GeneralBean> generalBeanMap = new HashMap<>();
			for (Map.Entry<Integer, Long> aEntry : pvpattFormation.entrySet()) {
				GeneralBean generalBean = generalAll.get(aEntry.getValue());
				if (generalBean == null) {
					continue;
				}
				generalBeanMap.put(aEntry.getKey(), generalBean);
			}
			dbBattleset.setTeam(generalBeanMap);
			minePointBuilder.setDbBattleset(dbBattleset);
		}
		//
		minePointBuilder.setBattlePlayerMap(null);
		if(minePlayer.getOwnMinePoint() == null){
			minePlayer.setOwnMinePoint(new ArrayList<>());
		}
		minePlayer.getOwnMinePoint().add(minePointVal);
		// 战斗记录
		DBMineBattleRecord targetRecordBuilder = new DBMineBattleRecord();
		minePlayer.getBattleRecord().add(targetRecordBuilder);
		targetRecordBuilder.setPositive(true);
		targetRecordBuilder.setIsSuccess(1);
		targetRecordBuilder.setMinePoint(minePointVal);
		targetRecordBuilder.setTargetPlayerId(targetPlayerId);
		targetRecordBuilder.setType(1);
		targetRecordBuilder.setGains(new HashMap<>());
		if (rewardCoins > 0) {
			targetRecordBuilder.getGains().put(GameConfig.PLAYER.GOLD, rewardCoins);
		}
		if (rewardGold > 0) {
			targetRecordBuilder.getGains().put(GameConfig.PLAYER.YB, rewardGold);
		}
		targetRecordBuilder.setAddTime(System.currentTimeMillis());
		MineManager.getInstance().limitBattleRecord(minePlayer);
		MineCache.getInstance().setMinePlayer(playerId, minePlayer);
	}

	public synchronized void changeMineDefenceFormation(DBMine mineEntity, int level_index, int point_index,
			DbBattleset stageFormationEntity) {
		mineEntity.getLevels().get(level_index).getMinePoints().get(point_index).setDbBattleset(stageFormationEntity);
		MineCache.getInstance().setMineEntity(mineEntity);
	}

	public boolean checkRedPoints(int playerId) {
		boolean ret = false;
		DBMinePlayer minePlayer = MineCache.getInstance().getMinePlayer(playerId);
		if(minePlayer != null) {
			if(minePlayer.getOwnMinePoint() == null){
				ret = true;
			}else {
				int ownCount = minePlayer.getOwnMinePoint().size();
				if (ownCount < MineConstants.MAX_MINE_COUNT) {
					ret = true;
				}
			}
			List<Integer> gainList = minePlayer.getGains();
			if(gainList != null) {
				for (Integer aGain : gainList) {
					if (aGain > 0) {
						ret = true;
						break;
					}
				}
			}
		}else {
			ret = true;
		}
		return ret;
	}

}
