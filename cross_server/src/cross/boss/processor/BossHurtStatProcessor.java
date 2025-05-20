package cross.boss.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayer;
import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.bean.CrossBossRoom;
import cross.boss.logic.BossBattleManager;
import cross.boss.logic.BossPlayerManager;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.BeanCrossBossPlayer;
import game.module.battle.ProtoMessageBattle.S2CBossChase;
import game.module.battle.ProtoMessageBattle.S2CCrossBossHurtStat;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37039, accessLimit = 100)
public class BossHurtStatProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossHurtStatProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		logger.info("cross boss hurt stat,playerId={}", playingRole.getId());
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
			logger.error("playerId={},error={}", playingRole.getId(), RetCode.PVP_PLAYER_STATUS_ERROR);
			playingRole.getGamePlayer().writeAndFlush(37040, S2CBossChase.newBuilder());
			return;
		}
		// 获取房间id
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37040, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37040, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37040, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				S2CCrossBossHurtStat.Builder retBuilder = S2CCrossBossHurtStat.newBuilder();
				for (Map.Entry<Integer, Integer> damagePair : crossBossRoom.getDamageSum().entrySet()) {
					int uuid = damagePair.getKey();
					BeanCrossBossPlayer.Builder playerInfoBuilder = retBuilder.addHurtOneBuilder().setUuid(uuid)
							.setHurtSum(damagePair.getValue()).getPlayerInfoBuilder();
					CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(uuid);
					if (crossBossPlayer == null) {
						crossBossPlayer = crossBossRoom.getLoadingPlayerMap().get(uuid);
					}
					if (crossBossPlayer != null) {
						playerInfoBuilder.setId(crossBossPlayer.getCrossPlayerInfo().getPlayerId())
								.setServerId(crossBossPlayer.getCrossPlayerInfo().getServerId())
								.setName(crossBossPlayer.getCrossPlayerInfo().getPlayerName())
								.setLevel(crossBossPlayer.getCrossPlayerInfo().getLevel()).setUuid(uuid)
								.getHeroBuilder()
								.setTemplateId(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getTemplateId())
								.setLevel(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getLevel())
								.setStarCount(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getStar())
								.setAdvanceGrade(crossBossPlayer.getCrossPlayerInfo().getHeroEntity().getQuality());
					}
				}
				playingRole.getGamePlayer().writeAndFlush(37040, retBuilder);
			}

			@Override
			public Object getIdentifyer() {
				return crossBossRoom.getId();
			}

			@Override
			public byte getEventType() {
				return 0;
			}
		});

	}

	@Override
	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}