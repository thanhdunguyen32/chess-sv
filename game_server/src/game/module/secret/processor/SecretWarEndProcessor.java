package game.module.secret.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretAward;
import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import db.proto.ProtoMessageSecret.SecretStageAward;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.PaymentConstants;
import game.module.secret.bean.Secret;
import game.module.secret.bean.SecretTemplate;
import game.module.secret.constants.SecretConstants;
import game.module.secret.dao.SecretCache;
import game.module.secret.dao.SecretTemplateCache;
import game.module.secret.logic.SecretManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageSecret.C2SSecretBattleEnd;
import ws.WsMessageBase.IOSecretBoxAward;
import ws.WsMessageBase.IOSecretHero;
import ws.WsMessageSecret.S2CSecretBattleEnd;
import ws.WsMessageBase.SecretItemInfo;

/**
 * 战斗结束
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午3:53:13
 */
@MsgCodeAnn(msgcode = C2SSecretBattleEnd.id, accessLimit = 500)
public class SecretWarEndProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretWarEndProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 战斗结束
	 */
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

	}

	private void setFormationHeros(Secret secret, IOSecretHero[] tmpOnlineFormationList) {
		DBSecretUsedHero.Builder dbSecretUsedHeroBuilder = DBSecretUsedHero.newBuilder();
		for (IOSecretHero secretHero : tmpOnlineFormationList) {
			if (secretHero.hero_type == SecretConstants.HERO) {
				dbSecretUsedHeroBuilder.putHeroUsed(secretHero.hero_id, secretHero.hp_percent);
			} else {
				dbSecretUsedHeroBuilder.addSoldierUsed(secretHero.hero_id);
			}
		}
		secret.setFormationHeros(dbSecretUsedHeroBuilder.build());
	}

	private DBSecretUsedHero buildHeroCost(DBSecretUsedHero existCost, IOSecretHero[] myCostList) {
		DBSecretUsedHero.Builder usedHeroBuilder;
		if (existCost != null) {
			usedHeroBuilder = existCost.toBuilder();
		} else {
			usedHeroBuilder = DBSecretUsedHero.newBuilder();
		}
		if (usedHeroBuilder.getSoldierUsedCount() == 0) {
			for (int i = 0; i < 8; i++) {
				usedHeroBuilder.addSoldierUsed(-1);
			}
		}
		for (IOSecretHero secretHero : myCostList) {
			if (secretHero.hero_type == SecretConstants.HERO) {
				usedHeroBuilder.putHeroUsed(secretHero.hero_id, secretHero.hp_percent);
			} else {
				usedHeroBuilder.setSoldierUsed(secretHero.hero_id, secretHero.hp_percent);
			}
		}
		return usedHeroBuilder.build();
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SSecretBattleEnd secretWarEnd = C2SSecretBattleEnd.parse(request);
		boolean isWin = secretWarEnd.is_win;
		boolean isInterrupt = secretWarEnd.is_interrupt;
		IOSecretHero[] heroCostList = secretWarEnd.my_cost;
		IOSecretHero[] enemyCostList = secretWarEnd.enemy_cost;
		int playerId = playingRole.getId();
		logger.info("secret battle end, msg={}, playerId={}", secretWarEnd, playerId);
		// 不在战斗状态
		// if (playingRole.getPlayerCacheStatus().getPosition() !=
		// PlayerPosition.PLAYER_POSITION_SECRET) {
		// playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_WAR_END,
		// S2CSecretWarEnd.newBuilder().setIsWin(isWin).setIsInterrupt(isInterrupt));
		// return;
		// }
		// 没有秘密基地记录
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playingRole.getId());
		if (secret == null) {
			S2CSecretBattleEnd respMsg = new S2CSecretBattleEnd();
			respMsg.is_win = isWin;
			respMsg.is_interrupt = isInterrupt;
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 参数检测
		if (heroCostList != null) {
			for (IOSecretHero secretHero : heroCostList) {
				boolean findExist = false;
				IOSecretHero[] prevHeroList = secret.getTmpOnlineFormationList();
				for (IOSecretHero secretHero2 : prevHeroList) {
					if (secretHero.hero_type == secretHero2.hero_type && secretHero.hero_id == secretHero2.hero_id) {
						findExist = true;
					}
				}
				// 没找到
				if (!findExist) {
					S2CSecretBattleEnd respMsg = new S2CSecretBattleEnd();
					respMsg.is_win = isWin;
					respMsg.is_interrupt = isInterrupt;
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
			}
		}
		// 战斗结束, 更新玩家状态
		playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_HALL);
		// ret
		S2CSecretBattleEnd respMsg = new S2CSecretBattleEnd();
		respMsg.is_interrupt = isInterrupt;
		respMsg.is_win = isWin;
		// 第一个副本, 失败不做记录
		if (secret.getProgress() == 0 && !isWin) {
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 对应关卡id
		int mapId = secret.getMapId();
		if (secret.getProgress() == 0) {
			mapId = secret.getTmpMapId();
		}
		SecretTemplate currentStageTpl = SecretTemplateCache.getInstance().getSecretTemp(mapId);
		int[] loose_strength = currentStageTpl.getLoose_strength();
		int[] win_strength = currentStageTpl.getWin_strength();
		int currentProgress = secret.getProgress();
		if (isInterrupt) {
			// 中断战斗, 扣除体力
			int loseStrength = loose_strength[currentProgress];
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, -loseStrength, LogConstants.MODULE_SECRET);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// do
		if (!isWin) {
			// 失败, 扣体力
			int loseStrength = loose_strength[currentProgress];
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, -loseStrength, LogConstants.MODULE_SECRET);
			// 敌方消耗信息
			if (currentProgress > 0) {
				DBSecretUsedHero dbSecretUsedHero = buildHeroCost(secret.getEnemyCost(), enemyCostList);
				secret.setEnemyCost(dbSecretUsedHero);
			}
			// 我方消耗信息,我方全军覆没
			IOSecretHero[] onlineFormationList = secret.getTmpOnlineFormationList();
			DBSecretUsedHero.Builder myCostBuilder;
			if (secret.getMyCost() == null) {
				myCostBuilder = DBSecretUsedHero.newBuilder();
			} else {
				myCostBuilder = secret.getMyCost().toBuilder();
			}
			if (myCostBuilder.getSoldierUsedCount() == 0) {
				for (int i = 0; i < 8; i++) {
					myCostBuilder.addSoldierUsed(-1);
				}
			}
			for (IOSecretHero formationHero1 : onlineFormationList) {
				if (formationHero1.hero_type == SecretConstants.HERO) {
					myCostBuilder.putHeroUsed(formationHero1.hero_id, 0);
				} else {
					myCostBuilder.setSoldierUsed(formationHero1.hero_id, 0);
				}
			}
			secret.setMyCost(myCostBuilder.build());
			//清空上阵英雄
			secret.setFormationHeros(DBSecretUsedHero.newBuilder().build());
		} else {
			// 设置map id
			if (currentProgress == 0) {
				secret.setMapId(mapId);
			}
			secret.setProgress(currentProgress + 1);
			// 更新上阵信息
			setFormationHeros(secret, secret.getTmpOnlineFormationList());
			// 成功, 扣体力
			int winStrength = win_strength[currentProgress];
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, -winStrength,  LogConstants.MODULE_SECRET);
			// 加战队经验
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, winStrength*10, LogConstants.MODULE_SECRET);
			// 加英雄经验
			// int heroExp = stageTemplate.getHeroexp();
			// List<SecretHero> secretDbHeros = secret.getTmpOnlineFormationList();
			// for (SecretHero hero : secretDbHeros) {
			// HeroEntity aHero = HeroCache.getInstance().getHero(playingRole.getId(),
			// hero.getHeroId());
			// if (aHero != null) {
			// HeroManager.getInstance().addExp(playingRole, aHero, heroExp);
			// }
			// }
			// 我方消耗信息
			DBSecretUsedHero myCost = buildHeroCost(secret.getMyCost(), heroCostList);
			secret.setMyCost(myCost);
			// 敌方刷新
			secret.setEnemyCost(DBSecretUsedHero.newBuilder().build());
			// 掉落物品
//			int[] mapAward = currentStageTpl.getStage_list();
//			int stageId = mapAward[currentProgress];
//			Map<String, Object> stageTemplate = StageUtils.getStageTemplateByStageId(stageId);
//			Map<Integer, Integer> stageAwardMap = StageEndProcessor.calculateReward(playingRole, stageTemplate);
//			respMsg.reward_items = new SecretItemInfo[stageAwardMap.size()];
//			int i = 0;
//			// 活动期间, 奖励翻倍
//			int awardDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_SECRET);
//			for (Map.Entry<Integer, Integer> aEntryPair : stageAwardMap.entrySet()) {
//				int itemCount = aEntryPair.getValue() * awardDouble;
//				AwardUtils.changeCoinOrItem(playingRole, aEntryPair.getKey(), itemCount,
//						PaymentConstants.PAYMENT_SECRET_WAR_AWARD, LogConstants.MODULE_SECRET, LogConstants.YES);
//				respMsg.reward_items[i++] = new SecretItemInfo(aEntryPair.getKey(), itemCount);
//			}
			// 宝箱可领取
			DBSecretBoxAward.Builder dbSecretBoxAwardBuilder;
			if (secret.getBoxAward() == null) {
				dbSecretBoxAwardBuilder = DBSecretBoxAward.newBuilder();
			} else {
				dbSecretBoxAwardBuilder = secret.getBoxAward().toBuilder();
			}
			dbSecretBoxAwardBuilder.addAwardsBuilder().setStageIndex(secret.getProgress() - 1).setIsGet(0);
			secret.setBoxAward(dbSecretBoxAwardBuilder.build());
		}
		// 每日任务进度
//		DailyMissionManager.getInstance().secretChangeProgress(playingRole);
		// 更新secret
		SecretManager.getInstance().asyncUpdateSecretAll(secret);
		//
		buildRespMsg(respMsg,secret);
		// ret
		playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	private void buildRespMsg(S2CSecretBattleEnd respMsg, Secret secret) {
		respMsg.map_id = secret.getMapId();
		respMsg.progress = secret.getProgress();
		// 宝箱领取信息
		DBSecretBoxAward dbRanAward = secret.getBoxAward();
		List<SecretStageAward> randomAwards = dbRanAward.getAwardsList();
		if (randomAwards != null && !randomAwards.isEmpty()) {
			respMsg.boxAward = new ArrayList<>(randomAwards.size());
			int i = 0;
			for (SecretStageAward randomAward : randomAwards) {
				// 发送奖励领取信息
				IOSecretBoxAward secretBoxAward = new IOSecretBoxAward();
				respMsg.boxAward.add(secretBoxAward);
				secretBoxAward.stage_index = randomAward.getStageIndex();
				secretBoxAward.is_get = randomAward.getIsGet();
				//
				List<DBSecretAward> dbSecretAwards = randomAward.getSecretAwardList();
				if (dbSecretAwards != null && !dbSecretAwards.isEmpty()) {
					secretBoxAward.award_list = new ArrayList<>(dbSecretAwards.size());
					int j = 0;
					for (DBSecretAward dbSecretAward : dbSecretAwards) {
						secretBoxAward.award_list.add(new SecretItemInfo(dbSecretAward.getAwardId(),
								dbSecretAward.getAwardCnt()));
					}
				}
			}
		}
		// 上阵英雄+士兵
		DBSecretUsedHero dbSecretUsedHero = secret.getFormationHeros();
		if (dbSecretUsedHero != null) {
			Map<Integer, Integer> formationHeros = dbSecretUsedHero.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1,
							dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = dbSecretUsedHero.getSoldierUsedList();
			for (Integer aSoldierIndex : soldierList) {
				IOSecretHero ioSecretHero = new IOSecretHero(1000, 2, -aSoldierIndex);
				heroRecordList.add(ioSecretHero);
			}
			respMsg.online_formation = heroRecordList;
		}

		// 我方消耗
		DBSecretUsedHero myCostInfo = secret.getMyCost();
		if (myCostInfo != null) {
			Map<Integer, Integer> formationHeros = myCostInfo.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1,
							dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = myCostInfo.getSoldierUsedList();
			int idx = 0;
			for (Integer aSoldierHp : soldierList) {
				if (aSoldierHp != -1) {
					IOSecretHero ioSecretHero = new IOSecretHero(aSoldierHp, 2, -idx);
					heroRecordList.add(ioSecretHero);
				}
				idx++;
			}
			respMsg.my_cost = heroRecordList;
		}

		// 敌方消耗
		DBSecretUsedHero enemyCostInfo = secret.getEnemyCost();
		if (enemyCostInfo != null) {
			Map<Integer, Integer> formationHeros = enemyCostInfo.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1,
							dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = enemyCostInfo.getSoldierUsedList();
			int idx = 0;
			for (Integer aSoldierHp : soldierList) {
				if (aSoldierHp != -1) {
					IOSecretHero ioSecretHero = new IOSecretHero(aSoldierHp, 2, -idx);
					heroRecordList.add(ioSecretHero);
				}
				idx++;
			}
			respMsg.enemy_cost = heroRecordList;
		}
	}

}
