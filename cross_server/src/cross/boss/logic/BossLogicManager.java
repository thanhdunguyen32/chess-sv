package cross.boss.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayer;
import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.bean.CrossBossRoom;
import game.CrossServer;
import game.bean.CrossGsBean;
import game.common.AwardConstants;
import game.common.StringConstants;
import game.logic.CrossGsListManager;
import game.module.battle.ProtoMessageBattle.BeanCrossBossFinishPlayer;
import game.module.battle.ProtoMessageBattle.PushCrossBossFinish;
import game.module.cross.bean.CrossBossFinish;
import game.module.item.ItemConstants;
import game.module.template.CrossBossRewardTemplate;

public class BossLogicManager {

	private static Logger logger = LoggerFactory.getLogger(BossLogicManager.class);

	static class SingletonHolder {
		static BossLogicManager instance = new BossLogicManager();
	}

	public static BossLogicManager getInstance() {
		return SingletonHolder.instance;
	}

	public void finishBattle(CrossBossRoom crossBossRoom, int casterUuid) {
		logger.info("cross boss battle finish,room_id={}", crossBossRoom.getId());
		if (crossBossRoom.isFinish()) {
			return;
		} else {
			crossBossRoom.setFinish(true);
		}
		int roomTypeId = crossBossRoom.getRoomType();
		BossBattleManager.getInstance().updateNewestRoom(crossBossRoom);
		List<CrossBossRewardTemplate> rewardTplCfgList = CrossBossRewardTemplateCache.getInstance()
				.getCrossBossRewardTemplateByRoomId(roomTypeId);
		Map<Integer, Integer> damageSum = crossBossRoom.getDamageSum();
		List<DamagePair> damagePairList = new ArrayList<>();
		for (Map.Entry<Integer, Integer> aPair : damageSum.entrySet()) {
			damagePairList.add(new DamagePair(aPair.getKey(), aPair.getValue()));
		}
		// 根据伤害排序
		damagePairList.sort(new Comparator<DamagePair>() {
			@Override
			public int compare(DamagePair o1, DamagePair o2) {
				return o2.damage_val - o1.damage_val;
			}
		});
		// ret
		int lastSeconds = (int) (System.currentTimeMillis() - crossBossRoom.getStartTime()) / 1000;
		PushCrossBossFinish.Builder pushBuilder = PushCrossBossFinish.newBuilder().setLastSeconds(lastSeconds)
				.setIsTimeout(false);
		// 最后击杀者的信息
		CrossBossPlayer lastKillPlayer = crossBossRoom.getPlayerMap().get(casterUuid);
		if (lastKillPlayer != null) {
			pushBuilder.getLastKillerBuilder().setId(lastKillPlayer.getCrossPlayerId().playerId)
					.setName(lastKillPlayer.getCrossPlayerInfo().getPlayerName())
					.setLevel(lastKillPlayer.getCrossPlayerInfo().getLevel())
					.setServerId(lastKillPlayer.getCrossPlayerInfo().getServerId()).setUuid(lastKillPlayer.getUuid());
		} else {
			pushBuilder.getLastKillerBuilder().setId(0).setName("").setLevel(1).setServerId(1).setUuid(0);
		}
		int damageRank = 1;
		int rewardIndex = 0;
		CrossBossRewardTemplate rewardTemplate = rewardTplCfgList.get(rewardIndex);
		for (DamagePair damagePair : damagePairList) {
			int uuid = damagePair.uuid;
			int damageVal = damagePair.damage_val;
			while (rewardTemplate.getRank() < damageRank && rewardIndex < rewardTplCfgList.size()) {
				rewardIndex++;
				if (rewardIndex < rewardTplCfgList.size()) {
					rewardTemplate = rewardTplCfgList.get(rewardIndex);
				} else {
					rewardTemplate = rewardTplCfgList.get(1);
					break;
				}
			}
			int rewardCoins = rewardTemplate.getReward_coins();
			String rewardItems = rewardTemplate.getReward_item();
			int rewardItemId = getRewardItemId(rewardItems);
			// 玩家信息
			CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(uuid);
			if (crossBossPlayer != null) {
				BeanCrossBossFinishPlayer.Builder aPlayerBuilder = pushBuilder.addRankInfoBuilder()
						.setPlayerId(crossBossPlayer.getCrossPlayerInfo().getPlayerId())
						.setName(crossBossPlayer.getCrossPlayerInfo().getPlayerName())
						.setLevel(crossBossPlayer.getCrossPlayerInfo().getLevel())
						.setServerId(crossBossPlayer.getCrossPlayerInfo().getServerId())
						.setHeroId(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getTemplateId())
						.setDamageSum(damageVal);
				//
				CrossBossFinish crossBossFinishMsg = new CrossBossFinish(damageRank);
				crossBossFinishMsg.setItemCountList(new ArrayList<Integer>());
				crossBossFinishMsg.setItemIdList(new ArrayList<Integer>());
				// 奖励金币
				if (rewardCoins > 0) {
					aPlayerBuilder.addRewardItemBuilder().setTemplateId(AwardConstants.GOLD_COIN)
							.setCount(rewardCoins);
					crossBossFinishMsg.getItemIdList().add(AwardConstants.GOLD_COIN);
					crossBossFinishMsg.getItemCountList().add(rewardCoins);
				}
				// 奖励道具
				aPlayerBuilder.addRewardItemBuilder().setTemplateId(rewardItemId)
						.setCount(rewardTemplate.getReward_item_count());
				crossBossFinishMsg.getItemIdList().add(rewardItemId);
				crossBossFinishMsg.getItemCountList().add(rewardTemplate.getReward_item_count());
				// 击杀奖励
				if (casterUuid == uuid) {
					CrossBossRewardTemplate lastKillConfig = rewardTplCfgList.get(0);
					int lastKillRewardId = getRewardItemId(lastKillConfig.getReward_item());
					int lastKillRewardCount = lastKillConfig.getReward_item_count();
					aPlayerBuilder.addRewardItemBuilder().setTemplateId(lastKillRewardId).setCount(lastKillRewardCount);
					crossBossFinishMsg.getItemIdList().add(lastKillRewardId);
					crossBossFinishMsg.getItemCountList().add(lastKillRewardCount);
				}
				// 发送到game server
				int aServerId = crossBossPlayer.getCrossPlayerInfo().getServerId();
				int aPlayerId = crossBossPlayer.getCrossPlayerInfo().getPlayerId();
				CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(aServerId);
				String gsHost = gsInfo.getIp();
				int gsPort = gsInfo.getLanPort();
				if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
					CrossServer.getInstance().getLanClientManager().sendBossEnd2Gs(gsHost, gsPort, aPlayerId,
							crossBossFinishMsg);
				}
			}
			damageRank++;
		}
		// 推送给玩家
		for (CrossBossPlayer crossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			if (crossBossPlayer.getNetty4Session() != null && crossBossPlayer.getNetty4Session().isChannelActive()) {
				crossBossPlayer.getNetty4Session().writeAndFlush(37042, pushBuilder);
			}
		}
		// 移除缓存
		removeCache(crossBossRoom);
	}

	public void finishBattleTimeout(CrossBossRoom crossBossRoom) {
		logger.info("cross boss battle finish timeout,room_id={}", crossBossRoom.getId());
		if (crossBossRoom.isFinish()) {
			return;
		} else {
			crossBossRoom.setFinish(true);
		}
		int roomTypeId = crossBossRoom.getRoomType();
		BossBattleManager.getInstance().updateNewestRoom(crossBossRoom);
		List<CrossBossRewardTemplate> rewardTplCfgList = CrossBossRewardTemplateCache.getInstance()
				.getCrossBossRewardTemplateByRoomId(roomTypeId);
		Map<Integer, Integer> damageSum = crossBossRoom.getDamageSum();
		List<DamagePair> damagePairList = new ArrayList<>();
		for (Map.Entry<Integer, Integer> aPair : damageSum.entrySet()) {
			damagePairList.add(new DamagePair(aPair.getKey(), aPair.getValue()));
		}
		// 根据伤害排序
		damagePairList.sort(new Comparator<DamagePair>() {
			@Override
			public int compare(DamagePair o1, DamagePair o2) {
				return o2.damage_val - o1.damage_val;
			}
		});
		// ret
		int lastSeconds = (int) (System.currentTimeMillis() - crossBossRoom.getStartTime()) / 1000;
		PushCrossBossFinish.Builder pushBuilder = PushCrossBossFinish.newBuilder().setLastSeconds(lastSeconds)
				.setIsTimeout(true);
		// 参与奖励
		CrossBossRewardTemplate rewardTemplate = rewardTplCfgList.get(1);
		for (DamagePair damagePair : damagePairList) {
			int uuid = damagePair.uuid;
			int damageVal = damagePair.damage_val;
			int rewardCoins = rewardTemplate.getReward_coins();
			String rewardItems = rewardTemplate.getReward_item();
			int rewardItemId = getRewardItemId(rewardItems);
			// 玩家信息
			CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(uuid);
			if (crossBossPlayer != null) {
				BeanCrossBossFinishPlayer.Builder aPlayerBuilder = pushBuilder.addRankInfoBuilder()
						.setPlayerId(crossBossPlayer.getCrossPlayerInfo().getPlayerId())
						.setName(crossBossPlayer.getCrossPlayerInfo().getPlayerName())
						.setLevel(crossBossPlayer.getCrossPlayerInfo().getLevel())
						.setServerId(crossBossPlayer.getCrossPlayerInfo().getServerId())
						.setHeroId(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getTemplateId())
						.setDamageSum(damageVal);
				//
				CrossBossFinish crossBossFinishMsg = new CrossBossFinish(-1);
				crossBossFinishMsg.setItemCountList(new ArrayList<Integer>());
				crossBossFinishMsg.setItemIdList(new ArrayList<Integer>());
				// 奖励金币
				if (rewardCoins > 0) {
					aPlayerBuilder.addRewardItemBuilder().setTemplateId(AwardConstants.GOLD_COIN)
							.setCount(rewardCoins);
					crossBossFinishMsg.getItemIdList().add(AwardConstants.GOLD_COIN);
					crossBossFinishMsg.getItemCountList().add(rewardCoins);
				}
				// 奖励道具
				aPlayerBuilder.addRewardItemBuilder().setTemplateId(rewardItemId)
						.setCount(rewardTemplate.getReward_item_count());
				crossBossFinishMsg.getItemIdList().add(rewardItemId);
				crossBossFinishMsg.getItemCountList().add(rewardTemplate.getReward_item_count());
				// 发送到game server
				int aServerId = crossBossPlayer.getCrossPlayerInfo().getServerId();
				int aPlayerId = crossBossPlayer.getCrossPlayerInfo().getPlayerId();
				CrossGsBean gsInfo = CrossGsListManager.getInstance().getServer(aServerId);
				String gsHost = gsInfo.getIp();
				int gsPort = gsInfo.getLanPort();
				if (CrossServer.getInstance().getLanClientManager().connect(gsHost, gsPort)) {
					CrossServer.getInstance().getLanClientManager().sendBossEnd2Gs(gsHost, gsPort, aPlayerId,
							crossBossFinishMsg);
				}
			}
		}
		// 推送给玩家
		for (CrossBossPlayer crossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			if (crossBossPlayer.getNetty4Session() != null && crossBossPlayer.getNetty4Session().isChannelActive()) {
				crossBossPlayer.getNetty4Session().writeAndFlush(37042, pushBuilder);
			}
		}
		// 移除缓存
		removeCache(crossBossRoom);
	}

	public void removeCache(CrossBossRoom crossBossRoom) {
		// 复活schedule
		for (CrossBossPlayer crossBossPlayer : crossBossRoom.getPlayerMap().values()) {
			int uuid = crossBossPlayer.getUuid();
			BossBattleManager.getInstance().removeSchedule(uuid);
		}
		//
		int roomId = crossBossRoom.getId();
		int roomTypeId = crossBossRoom.getRoomType();
		BossBattleManager.getInstance().removeRoom(roomId);
		// 更新房间信息
		removeRoomId(crossBossRoom.getPlayerMap().values(), roomTypeId);
		removeRoomId(crossBossRoom.getLoadingPlayerMap().values(), roomTypeId);
		//
		crossBossRoom.getPlayerMap().clear();
		crossBossRoom.getLoadingPlayerMap().clear();
		crossBossRoom.getDamageSum().clear();
		// 移除玩家缓存
		removePlayerCache(crossBossRoom.getPlayerMap().values());
		removePlayerCache(crossBossRoom.getLoadingPlayerMap().values());
	}

	private void removeRoomId(Collection<CrossBossPlayer> playerColl, int roomTypeId) {
		for (CrossBossPlayer crossBossPlayer : playerColl) {
			int playerId = crossBossPlayer.getCrossPlayerId().playerId;
			int serverId = crossBossPlayer.getCrossPlayerId().serverId;
			CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance()
					.getPlayerJoinInfo(playerId, serverId);
			Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
			if (roomIdMap != null) {
				roomIdMap.remove(roomTypeId);
			}
			// 结束的房间id
			Set<Integer> finishRoomIds = crossBossPlayerJoinInfo.getFinishRoomIds();
			if (finishRoomIds == null) {
				finishRoomIds = new HashSet<>();
				crossBossPlayerJoinInfo.setFinishRoomIds(finishRoomIds);
			}
			finishRoomIds.add(roomTypeId);
		}
	}

	private void removePlayerCache(Collection<CrossBossPlayer> playerColl) {
//		for (CrossBossPlayer crossBossPlayer : playerColl) {
//			GamePlayer netty4Session = crossBossPlayer.getNetty4Session();
//			if (netty4Session != null && netty4Session.isChannelActive()) {
//				SessionManager.getInstance().remove(netty4Session.getSessionId());
//			}
//		}
	}

	private int getRewardItemId(String rewardItems) {
		String[] rewardItemConfigs = StringUtils.split(rewardItems, StringConstants.SEPARATOR_HENG);
		int rewardItemId = 0;
		if (rewardItemConfigs.length == 1) {
			rewardItemId = Integer.parseInt(rewardItems);
		} else {
			int randIndex = RandomUtils.nextInt(0, rewardItemConfigs.length);
			rewardItemId = Integer.parseInt(rewardItemConfigs[randIndex]);
		}
		return rewardItemId;
	}

	private static final class DamagePair {
		public int uuid;
		public int damage_val;

		public DamagePair(int uuid, int damage_val) {
			super();
			this.uuid = uuid;
			this.damage_val = damage_val;
		}
	}

}
