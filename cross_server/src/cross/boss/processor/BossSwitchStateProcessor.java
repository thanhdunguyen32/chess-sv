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
import game.module.battle.ProtoMessageBattle.C2SBossSwitchState;
import game.module.battle.ProtoMessageBattle.PVP_STATE;
import game.module.battle.ProtoMessageBattle.S2CBossSwitchState;
import game.module.pvp.bean.PvpPlayer.PVP_BATTLE_STATUS;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37019, accessLimit = 0)
public class BossSwitchStateProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossSwitchStateProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		final C2SBossSwitchState pvpSwitchStateMsg = ProtoUtil.getProtoObj(C2SBossSwitchState.PARSER, request);
		logger.info("cross boss switch state ,player={},msg={}", playingRole.getId(), pvpSwitchStateMsg);
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
			logger.error("CROSS_BOSS_PLAYER_STATUS_ERROR,#1");
			playingRole.getGamePlayer().writeAndFlush(37020, S2CBossSwitchState.newBuilder());
			return;
		}
		// 获取房间id
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37020, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37020, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37020, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		// 玩家信息
		final CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(crossPlayerId.hashCode());
		if (crossBossPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(37020, RetCode.PVP_PLAYER_NOT_EXIST);
			return;
		}
		// 玩家状态是否为死亡
		if (crossBossPlayer.getRawStatus() == PVP_BATTLE_STATUS.DIE) {
			logger.error("player={},is die!", playingRole.getId());
			playingRole.getGamePlayer().writeAndFlush(37020, S2CBossSwitchState.newBuilder());
			return;
		}

		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {

			@Override
			public void run() {
				RetCode retCode = RetCode.RET_OK;
				PVP_STATE newState = pvpSwitchStateMsg.getNewState();
				switch (newState) {
				case PVP_STATE_PREPARE_ATTACK:
					retCode = BossBattleManager.getInstance().prepareAttack(playingRole, newState,
							pvpSwitchStateMsg.getTargetPlayerUuid(), pvpSwitchStateMsg.getNewPosX(),
							pvpSwitchStateMsg.getNewPosZ(), crossBossRoom, crossBossPlayer);
					break;
				case PVP_STATE_CAST_SKILL:
//					if (crossBossPlayer.getRawStatus() != PVP_BATTLE_STATUS.PREPARE_ATTACK) {
//						playingRole.getGamePlayer().writeAndFlush(37020, RetCode.PVP_PLAYER_STATE_ERROR);
//						return;
//					}
					retCode = BossBattleManager.getInstance().castSkill(playingRole, newState,
							pvpSwitchStateMsg.getTargetPlayerUuid(), pvpSwitchStateMsg.getNewPosX(),
							pvpSwitchStateMsg.getNewPosZ(), pvpSwitchStateMsg.getSkillIndex(), crossBossRoom,
							crossBossPlayer, pvpSwitchStateMsg.getClientDelay());
					break;
				case PVP_STATE_IDLE:
					retCode = BossBattleManager.getInstance().idle(playingRole, newState, pvpSwitchStateMsg.getNewPosX(),
							pvpSwitchStateMsg.getNewPosZ(), crossBossRoom, crossBossPlayer);
					break;
				default:
					break;
				}
				// ret
				if (retCode == RetCode.RET_OK) {
					playingRole.getGamePlayer().writeAndFlush(37020, S2CBossSwitchState.newBuilder());
				} else {
					playingRole.getGamePlayer().writeAndFlush(37020, retCode);
				}
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
