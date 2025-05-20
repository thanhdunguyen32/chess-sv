package cross.boss.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossEntity;
import cross.boss.bean.CrossBossPlayer;
import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.bean.CrossBossRoom;
import game.CrossServer;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SBossPropChange;
import game.module.battle.ProtoMessageBattle.PVP_PROPERTIES;
import game.module.battle.ProtoMessageBattle.PVP_STATE;
import game.module.battle.ProtoMessageBattle.PushBossChase;
import game.module.battle.ProtoMessageBattle.PushBossHeroReborn;
import game.module.battle.ProtoMessageBattle.PushBossPropChange;
import game.module.battle.ProtoMessageBattle.PushBossSwitchState;
import game.module.battle.ProtoMessageBattle.PushBossSyncPosition;
import game.module.battle.ProtoMessageBattle.PushCrossBossLeaveBattle;
import game.module.battle.ProtoMessageBattle.PushPvpKill;
import game.module.craft.logic.CraftLogic.CastSkillInfo;
import game.module.cross.bean.CrossBossBegin;
import game.module.cross.bean.CrossHeroEntity;
import game.module.cross_boss.logic.CrossBossTemplateCache;
import game.module.item.bean.DBRuneInfoBean;
import game.module.item.bean.HeroEquimentPack.HeroEquiOneInfo;
import game.module.pvp.bean.PvpPlayer.PVP_BATTLE_STATUS;
import game.module.pvp.logic.PvpConstants;
import game.module.question.ProtoMessageQuestion.IOHeroSimpleInfo;
import game.module.robot.logic.MonsterTemplateCache;
import game.module.template.MonsterTemplate;
import game.util.concurrent.MyOrderedThreadPoolExecutor;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.NamedThreadFactory;
import lion.netty4.message.GamePlayer;
import lion.netty4.proto.RpcBaseProto.RetCode;

public class BossBattleManager {

	private static Logger logger = LoggerFactory.getLogger(BossBattleManager.class);

	static class SingletonHolder {
		static BossBattleManager instance = new BossBattleManager();
	}

	public static BossBattleManager getInstance() {
		return SingletonHolder.instance;
	}

	private AtomicInteger roomIdOp = new AtomicInteger();

	private Map<Integer, CrossBossRoom> roomMap = new ConcurrentHashMap<>();

	private Map<Integer, CrossBossRoom> newestRoomMap = new ConcurrentHashMap<>();

	private Map<Integer, Map<Integer, CastSkillInfo>> castSkillTimeMap = new ConcurrentHashMap<>();

	private Map<Integer, ScheduledFuture<?>> reviveScheduleMap = new ConcurrentHashMap<>();

	private MyOrderedThreadPoolExecutor bossPlayExecutor = new MyOrderedThreadPoolExecutor(
			Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("cross-boss-serial-pool"));

	public void shutdown() {
		logger.warn("shutdown cross boss thread executor!");
		try {
			bossPlayExecutor.shutdown();
			while (!bossPlayExecutor.isTerminated()) {
				bossPlayExecutor.awaitTermination(5, TimeUnit.SECONDS);
			}
			logger.warn("shutdown cross boss thread executor successfully!");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void execute(OrderedEventRunnable orderedEventRunnable) {
		bossPlayExecutor.execute(orderedEventRunnable);
	}

	public void joinBattle() {

	}

	public CrossBossRoom getRoom(Integer existRoomId) {
		return roomMap.get(existRoomId);
	}

	public static void buildIOHeroSimpleInfo(IOHeroSimpleInfo.Builder heroBuilder, CrossHeroEntity he) {
		heroBuilder.setTemplateId(he.getTemplateId()).setLevel(he.getLevel()).setStarCount(he.getStar())
				.setAdvanceGrade(he.getQuality());
		// skill
		if (he.getSkillPack() != null && he.getSkillPack().getSkillLevelList() != null) {
			List<Integer> skillLvels = he.getSkillPack().getSkillLevelList();
			for (Integer level1 : skillLvels) {
				heroBuilder.addSkillLevel(level1);
			}
		}
		// equip
		if (he.getEquipPack() != null && he.getEquipPack().getEquipInfoList() != null) {
			List<HeroEquiOneInfo> heroEquiOneInfos = he.getEquipPack().getEquipInfoList();
			for (HeroEquiOneInfo heroEquiOneInfo : heroEquiOneInfos) {
				heroBuilder.addEquipInfoBuilder().setPosition(heroEquiOneInfo.getPosition())
						.setFumoGrade(heroEquiOneInfo.getFumoLevel());
			}
		}
		// rune
		if (he.getRunePack() != null && he.getRunePack().getRuneInfoBeanList() != null) {
			List<DBRuneInfoBean> beanList = he.getRunePack().getRuneInfoBeanList();
			for (DBRuneInfoBean dbRuneInfoBean : beanList) {
				heroBuilder.addRuneInfo(dbRuneInfoBean.getRuneTemplateId());
			}
		}
	}

	public CrossBossRoom joinRoom(int roomTypeId, int playerId, int serverId, CrossBossBegin crossCraftBegin,
			GamePlayer netty4Session) {
		CrossBossRoom newestRoom = newestRoomMap.get(roomTypeId);
		boolean needCreateNewRoom = true;
		if (newestRoom != null) {
			int playerSize = newestRoom.getPlayerMap().size();
			int loadingPlayerSize = newestRoom.getLoadingPlayerMap().size();
			if (playerSize + loadingPlayerSize < BossConstants.ROOM_MAX_PLAYER_SIZE) {
				needCreateNewRoom = false;
			}
		}
		// 需要创建房间
		if (needCreateNewRoom) {
			newestRoom = createBossRoom(roomTypeId);
			roomMap.put(newestRoom.getId(), newestRoom);
			newestRoomMap.put(roomTypeId, newestRoom);
		}
		// 添加成员
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayer crossBossPlayer = new CrossBossPlayer();
		crossBossPlayer.setCrossPlayerId(crossPlayerId);
		crossBossPlayer.setCrossPlayerInfo(crossCraftBegin);
		crossBossPlayer.setNetty4Session(netty4Session);
		crossBossPlayer.setFirstCreate(true);
		newestRoom.getLoadingPlayerMap().put(crossPlayerId.hashCode(), crossBossPlayer);
		// 缓存
		BossPlayerManager.getInstance().putRoomId(crossPlayerId, roomTypeId, newestRoom.getId());
		return newestRoom;
	}

	private CrossBossRoom createBossRoom(int roomTypeId) {
		CrossBossRoom bossRoom = new CrossBossRoom();
		bossRoom.setId(roomIdOp.incrementAndGet());
		bossRoom.setRoomType(roomTypeId);
		bossRoom.setStartTime(System.currentTimeMillis());
		// 添加boss
		CrossBossEntity crossBossEntity = new CrossBossEntity();
		int monsterTemplateId = CrossBossTemplateCache.getInstance().getCrossBossTemplateById(roomTypeId).getBoss_id();
		MonsterTemplate mt = MonsterTemplateCache.getInstance().getMonsterTemplateById(monsterTemplateId);
		crossBossEntity.setTemplate_id(monsterTemplateId);
		crossBossEntity.setHp(mt.getHp());
		crossBossEntity.setUuid(-RandomUtils.nextInt(0, Integer.MAX_VALUE));
		crossBossEntity.setPos_pair(new float[] { 0f, 0f });
		bossRoom.setCrossBossEntity(crossBossEntity);
		return bossRoom;
	}

	public float[] randBornPosition() {
		float rand_x = RandomUtils.nextFloat(0f, 2 * BossConstants.BOSS_MAX_POSITION_RADIUS)
				- BossConstants.BOSS_MAX_POSITION_RADIUS;
		float rand_z = RandomUtils.nextFloat(0f, 2 * BossConstants.BOSS_MAX_POSITION_RADIUS)
				- BossConstants.BOSS_MAX_POSITION_RADIUS;
		return new float[] { rand_x, rand_z };
	}

	public RetCode updatePosition(CrossBossRoom bossRoom, CrossBossPlayer pvpPlayer, float newX, float newZ,
			float targetX, float targetZ, boolean isMove) {
		// 更新是否过于频繁
		long curentTimeMil = System.currentTimeMillis();
		long timeDiff = curentTimeMil - pvpPlayer.getLastUpdateTime();
		if (timeDiff < PvpConstants.MOVE_MSG_FREQUENT_TIME_DIFF && isMove && pvpPlayer.isMove()) {
			double oldAngle = Math.toDegrees(Math.atan2(pvpPlayer.getTarget_pos_pair()[1] - pvpPlayer.getPos_pair()[1],
					pvpPlayer.getTarget_pos_pair()[0] - pvpPlayer.getPos_pair()[0]));
			double newAngle = Math.toDegrees(Math.atan2(targetZ - newZ, targetX - newX));
			double angleDiff = Math.abs(oldAngle - newAngle);
			if (angleDiff >= PvpConstants.MIN_ANGLE_OFFSET && angleDiff <= 360 - PvpConstants.MIN_ANGLE_OFFSET) {
				// 移动消息发送过于频繁！
				return RetCode.PVP_SYNC_POS_FREQUENT;
			}
		}
		// 是否作弊
		// if (pvpPlayer.isMove()) {
		// int expectDistance = (int) (pvpPlayer.getSpeed() * timeDiff);
		// int xDiff = Math.abs(Math.round(newX) -
		// Math.round(pvpPlayer.getX()));
		// int zDiff = Math.abs(Math.round(newZ) -
		// Math.round(pvpPlayer.getZ()));
		// if (Math.abs(xDiff * 1000 + zDiff * 1000 - expectDistance) > 3000) {
		// // 作弊
		// return RetCode.PVP_SYNC_POS_DISTANCE_RRROR;
		// }
		// }
		// save status
		if (pvpPlayer.isMove()) {
			pvpPlayer.getPos_pair()[0] = newX;
			pvpPlayer.getPos_pair()[1] = newZ;
		}
		if (pvpPlayer.getTarget_pos_pair() == null) {
			pvpPlayer.setTarget_pos_pair(new float[] { 0f, 0f });
		}
		pvpPlayer.getTarget_pos_pair()[0] = targetX;
		pvpPlayer.getTarget_pos_pair()[1] = targetZ;
		pvpPlayer.setMove(isMove);
		pvpPlayer.setLastUpdateTime(curentTimeMil);
		// 推送
		for (CrossBossPlayer aCrossBossPlayer : bossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37014, PushBossSyncPosition.newBuilder().setUuid(pvpPlayer.getUuid())
						.setIsMove(isMove).setNewPosX(newX).setNewPosZ(newZ).setTargetX(targetX).setTargetZ(targetZ));
			}
		}
		return RetCode.RET_OK;
	}

	public RetCode chaseTarget(CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer, int targetPlayerId,
			float newPosX, float newPosZ, float targetX, float targetZ) {
		// TODO 位置判断
		crossBossPlayer.getPos_pair()[0] = newPosX;
		crossBossPlayer.getPos_pair()[1] = newPosZ;
		crossBossPlayer.setLastUpdateTime(System.currentTimeMillis());
		crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.CHASE);
		// 推送
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37018,
						PushBossChase.newBuilder().setOwnPlayerUuid(crossBossPlayer.getCrossPlayerId().hashCode())
								.setTargetPlayerUuid(targetPlayerId));
			}
		}
		return RetCode.RET_OK;
	}

	public void propChange(CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer,
			C2SBossPropChange pvpPropChangeMsg, PushBossPropChange.Builder pushBuilder) {
		PVP_PROPERTIES propType = pvpPropChangeMsg.getPropType();
		switch (propType.getNumber()) {
		case PVP_PROPERTIES.PVP_PROP_HP_VALUE:
			if (crossBossPlayer.getHp() <= 0) {
				logger.info("current hp is 0,playerId={}", crossBossPlayer.getCrossPlayerId().hashCode());
				return;
			}
			int val = pvpPropChangeMsg.getVal();
			int newHp = crossBossPlayer.getHp() + val;
			if (newHp > crossBossPlayer.getMaxHp()) {
				newHp = crossBossPlayer.getMaxHp();
			}
			pushBuilder.setEndVal(newHp);
			logger.info("player,id={}.hp={},maxHp={}", crossBossPlayer.getCrossPlayerId().hashCode(), newHp,
					crossBossPlayer.getMaxHp());
			crossBossPlayer.setHp(newHp);
			logger.info("player,id={},currentHp={},maxHp={},val={}", crossBossPlayer.getCrossPlayerId().hashCode(),
					newHp, crossBossPlayer.getMaxHp(), val);
			// 攻击者id
			int castPlayerId = pvpPropChangeMsg.getCastPlayerUuid();
			if (newHp <= 0) {
				// 是否为老牛的复活技能
				// if (pvpPlayer.getHero().getTemplateId() == 3010) {
				// DBHeroSkillPack dbHeroSkillPack =
				// pvpPlayer.getHero().getSkillPack();
				// if (dbHeroSkillPack != null &&
				// dbHeroSkillPack.getSkillLevelList().size() >= 3) {
				// // TODO 老牛复活
				// }
				// }
				// 攻击者增加能量
				playerDie(crossBossRoom, crossBossPlayer, castPlayerId);
			}
			if (val < 0) {
				// TODO 伤害统计
				// PvpPlayer castPvpPlayer = pvpScene.getPlayer(castPlayerId);
				// if (castPvpPlayer != null) {
				// castPvpPlayer.setDamageSum(castPvpPlayer.getDamageSum() -
				// val);
				// }
			}
			break;
		case PVP_PROPERTIES.PVP_PROP_SP_VALUE:
			val = pvpPropChangeMsg.getVal();
			int lastValue = crossBossPlayer.getSp() + val;
			if (lastValue > PvpConstants.SP_CAST_BIG) {
				lastValue = PvpConstants.SP_CAST_BIG;
			} else if (lastValue < 0) {
				lastValue = 0;
			}
			// int diffSp = lastValue - pvpPlayer.getSp();
			crossBossPlayer.setSp(lastValue);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * 玩家死亡处理
	 * 
	 * @param pvpScene
	 * @param pvpPlayer
	 * @param castPlayerId
	 */
	private void playerDie(CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer, int castPlayerId) {
		crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.DIE);
		CrossBossPlayer castPvpPlayer = crossBossRoom.getPlayerMap().get(castPlayerId);
		if (castPvpPlayer != null) {
			logger.info("caster add sp,playerId={}", castPlayerId);
			int killerAddSp = PvpConstants.SP_DIE_ADD;
			if (castPvpPlayer.getSp() + killerAddSp > PvpConstants.SP_CAST_BIG) {
				killerAddSp = PvpConstants.SP_CAST_BIG - castPvpPlayer.getSp();
			}
			castPvpPlayer.setSp(castPvpPlayer.getSp() + killerAddSp);
			// 推送
			PushBossPropChange.Builder retBuilder = PushBossPropChange.newBuilder().setPlayerId(castPlayerId)
					.setPropType(PVP_PROPERTIES.PVP_PROP_SP).setVal(killerAddSp).setShowTip(true);
			PushPvpKill.Builder killBuilder = PushPvpKill.newBuilder().setKillerId(castPlayerId)
					.setDieId(crossBossPlayer.getCrossPlayerId().hashCode());
			// send
			for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
				GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
				if (netty4Session != null && netty4Session.isChannelActive()) {
					netty4Session.write(37026, retBuilder.build());
					netty4Session.write(35134, killBuilder);
				}
			}
		}
		// 状态转换成死亡
		PushBossSwitchState.Builder dieStateBuilder = PushBossSwitchState.newBuilder()
				.setOwnPlayerUuid(crossBossPlayer.getCrossPlayerId().hashCode()).setTargetPlayerUuid(0)
				.setNewState(PVP_STATE.PVP_STATE_DIE).setNewPosX(0).setNewPosZ(0);
		// send
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.write(37022, dieStateBuilder);
			}
		}
		// schedule revive
		scheduleRevive(crossBossRoom, crossBossPlayer);
	}

	/**
	 * 自动复活
	 * 
	 * @param pvpScene
	 * @param pvpPlayer
	 */
	public void scheduleRevive(final CrossBossRoom crossBossRoom, final CrossBossPlayer crossBossPlayer) {
		ScheduledFuture<?> sf = CrossServer.executorService.schedule(new Runnable() {
			public void run() {
				logger.info("player revive,id={}", crossBossPlayer.getCrossPlayerId().hashCode());
				crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.IDLE);
				crossBossPlayer.getPos_pair()[0] = crossBossPlayer.getBorn_position()[0];
				crossBossPlayer.getPos_pair()[1] = crossBossPlayer.getBorn_position()[1];
				crossBossPlayer.setHp(crossBossPlayer.getMaxHp());
				// push
				PushBossHeroReborn.Builder pushBuilder = PushBossHeroReborn.newBuilder();
				pushBuilder.getPositionInfoBuilder().setUuid(crossBossPlayer.getCrossPlayerId().hashCode())
						.setHp(crossBossPlayer.getHp()).setSp(crossBossPlayer.getSp())
						.setSpeedUpRate(crossBossPlayer.getSpeedUpRate()).addPosPair(crossBossPlayer.getPos_pair()[0])
						.addPosPair(crossBossPlayer.getPos_pair()[1]);
				for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
					GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
					if (netty4Session != null && netty4Session.isChannelActive()) {
						netty4Session.writeAndFlush(37030, pushBuilder);
					}
				}
			}
		}, 10400, TimeUnit.MILLISECONDS);
		reviveScheduleMap.put(crossBossPlayer.getCrossPlayerId().hashCode(), sf);
	}

	public RetCode prepareAttack(PlayingRole playingRole, PVP_STATE newState, int targetPlayerUuid, float newPosX,
			float newPosZ, CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer) {
		// TODO 位置判断
		crossBossPlayer.getPos_pair()[0] = newPosX;
		crossBossPlayer.getPos_pair()[1] = newPosZ;
		crossBossPlayer.setLastUpdateTime(System.currentTimeMillis());
		crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.PREPARE_ATTACK);
		// 推送
		PushBossSwitchState.Builder pushBuilder = PushBossSwitchState.newBuilder()
				.setOwnPlayerUuid(crossBossPlayer.getCrossPlayerId().hashCode()).setTargetPlayerUuid(targetPlayerUuid)
				.setNewState(newState).setNewPosX(newPosX).setNewPosZ(newPosZ);
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37022, pushBuilder);
			}
		}
		return RetCode.RET_OK;
	}

	public RetCode castSkill(PlayingRole playingRole, PVP_STATE newState, int targetPlayerUuid, float newPosX,
			float newPosZ, int skillIndex, CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer,
			float clientDelay) {
		// 能否释放技能,技能cd时间判断
		if (skillIndex != 1) {
			Map<Integer, CastSkillInfo> heroCastSkillMap = castSkillTimeMap
					.get(crossBossPlayer.getCrossPlayerId().hashCode());
			if (heroCastSkillMap != null && heroCastSkillMap.get(skillIndex) != null) {
				CastSkillInfo castSkillInfo = heroCastSkillMap.get(skillIndex);
				int timeDiff = (int) (System.currentTimeMillis() - castSkillInfo.getTime());
				float targetDelay = castSkillInfo.getDelay();
				if (timeDiff < targetDelay * 950) {
					logger.info(
							"cross boss cast skill detect speed up!playerId={},skillIndex={},targetDelay={},timeDiff={}",
							crossBossPlayer.getCrossPlayerId().hashCode(), skillIndex, targetDelay, timeDiff);
					//castSkillInfo.setDelay(clientDelay * 2);
					return RetCode.RET_OK;
				}
			}
		}
		// TODO 位置判断
		crossBossPlayer.getPos_pair()[0] = newPosX;
		crossBossPlayer.getPos_pair()[1] = newPosZ;
		crossBossPlayer.setLastUpdateTime(System.currentTimeMillis());
		crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.SKILLING);
		// 推送
		PushBossSwitchState.Builder pushBuilder = PushBossSwitchState.newBuilder()
				.setOwnPlayerUuid(crossBossPlayer.getCrossPlayerId().hashCode()).setTargetPlayerUuid(targetPlayerUuid)
				.setNewState(newState).setNewPosX(newPosX).setNewPosZ(newPosZ).setSkillIndex(skillIndex);
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37022, pushBuilder);
			}
		}
		// 放大招
		if (skillIndex == 1) {
			crossBossPlayer.setSp(0);
			// 推送
			PushBossPropChange.Builder retBuilder = PushBossPropChange.newBuilder()
					.setPlayerId(crossBossPlayer.getCrossPlayerId().hashCode()).setPropType(PVP_PROPERTIES.PVP_PROP_SP)
					.setVal(-PvpConstants.SP_CAST_BIG).setShowTip(false);
			for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
				GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
				if (netty4Session != null && netty4Session.isChannelActive()) {
					netty4Session.writeAndFlush(37026, retBuilder);
				}
			}
		} else {// 保存技能释放信息
			Map<Integer, CastSkillInfo> heroCastSkillMap = castSkillTimeMap
					.get(crossBossPlayer.getCrossPlayerId().hashCode());
			if (heroCastSkillMap == null) {
				heroCastSkillMap = new HashMap<>();
				castSkillTimeMap.put(crossBossPlayer.getCrossPlayerId().hashCode(), heroCastSkillMap);
			}
			CastSkillInfo heroCastSkillInfo = heroCastSkillMap.get(skillIndex);
			if (heroCastSkillInfo == null) {
				heroCastSkillInfo = new CastSkillInfo(System.currentTimeMillis(), clientDelay);
				heroCastSkillMap.put(skillIndex, heroCastSkillInfo);
			} else {
				heroCastSkillInfo.setDelay(clientDelay);
				heroCastSkillInfo.setTime(System.currentTimeMillis());
			}
		}
		return RetCode.RET_OK;
	}

	public RetCode idle(PlayingRole playingRole, PVP_STATE newState, float newPosX, float newPosZ,
			CrossBossRoom crossBossRoom, CrossBossPlayer crossBossPlayer) {
		// TODO 位置判断
		crossBossPlayer.getPos_pair()[0] = newPosX;
		crossBossPlayer.getPos_pair()[1] = newPosZ;
		crossBossPlayer.setLastUpdateTime(System.currentTimeMillis());
		crossBossPlayer.setRawStatus(PVP_BATTLE_STATUS.IDLE);
		// 推送
		PushBossSwitchState.Builder pushBuilder = PushBossSwitchState.newBuilder()
				.setOwnPlayerUuid(crossBossPlayer.getCrossPlayerId().hashCode()).setNewState(newState)
				.setNewPosX(newPosX).setNewPosZ(newPosZ);
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37022, pushBuilder);
			}
		}
		return RetCode.RET_OK;
	}

	public void bossPropChange(CrossBossRoom crossBossRoom, CrossBossEntity crossBossEntity,
			C2SBossPropChange pvpPropChangeMsg, PushBossPropChange.Builder pushBuilder) {
		PVP_PROPERTIES propType = pvpPropChangeMsg.getPropType();
		switch (propType.getNumber()) {
		case PVP_PROPERTIES.PVP_PROP_HP_VALUE:
			int val = pvpPropChangeMsg.getVal();
			int newHp = crossBossEntity.getHp() + val;
			logger.info("cross boss .hp={},maxHp={}", newHp);
			crossBossEntity.setHp(newHp);
			pushBuilder.setEndVal(newHp);
			int casterUuid = pvpPropChangeMsg.getCastPlayerUuid();
			if (val < 0) {
				// 攻击者id
				// 伤害统计
				Integer existHurt = crossBossRoom.getDamageSum().get(casterUuid);
				if (existHurt == null) {
					crossBossRoom.getDamageSum().put(casterUuid, -val);
				} else {
					crossBossRoom.getDamageSum().put(casterUuid, existHurt - val);
				}
			}
			if (newHp <= 0) {
				BossLogicManager.getInstance().finishBattle(crossBossRoom,casterUuid);
			}
			break;
		default:
			break;
		}
	}

	public void downlineBattle(PlayingRole playingRole) {
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			return;
		}
		// 给所有玩家发送消息
		PushCrossBossLeaveBattle.Builder pushBuilder = PushCrossBossLeaveBattle.newBuilder()
				.setPlayerUuid(crossPlayerId.hashCode());
		for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
			if (netty4Session != null && netty4Session.isChannelActive()) {
				netty4Session.writeAndFlush(37036, pushBuilder);
			}
		}
	}

	public void updateNewestRoom(CrossBossRoom crossBossRoom) {
		int roomTypeId = crossBossRoom.getRoomType();
		CrossBossRoom newestRoom = newestRoomMap.get(roomTypeId);
		if(crossBossRoom == newestRoom){
			newestRoomMap.remove(roomTypeId);
		}
	}

	public void removeSchedule(int uuid){
		ScheduledFuture<?> sf = reviveScheduleMap.get(uuid);
		if(sf != null){
			sf.cancel(true);
		}
	}
	
	public void removeRoom(int roomId){
		roomMap.remove(roomId);
	}
	
	public Collection<CrossBossRoom> getAllRooms(){
		return roomMap.values();
	}
	
}
