package cross.boss.processor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossEntity;
import cross.boss.bean.CrossBossPlayer;
import cross.boss.bean.CrossBossPlayer.CrossPlayerId;
import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.bean.CrossBossRoom;
import cross.boss.logic.BossBattleManager;
import cross.boss.logic.BossPlayerManager;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SBossPropChange;
import game.module.battle.ProtoMessageBattle.PVP_PROPERTIES;
import game.module.battle.ProtoMessageBattle.PushBossPropChange;
import game.module.battle.ProtoMessageBattle.S2CBossPropChange;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37023, accessLimit = 0)
public class BossPropChangeProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossPropChangeProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		final C2SBossPropChange pvpPropChangeMsg = ProtoUtil.getProtoObj(C2SBossPropChange.PARSER, request);
		logger.info("cross boss prop change,msg={}", pvpPropChangeMsg);
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE) {
			logger.error("CROSS_BOSS_PLAYER_STATUS_ERROR,#1");
			playingRole.getGamePlayer().writeAndFlush(37024, S2CBossPropChange.newBuilder());
			return;
		}
		// 获取房间id
		int playerId = playingRole.getPlayerBean().getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37024, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37024, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37024, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		// 玩家信息
		int targetPlayerUuid = pvpPropChangeMsg.getTargetPlayerUuid();
		if (!crossBossRoom.getPlayerMap().containsKey(targetPlayerUuid) && crossBossRoom.getCrossBossEntity().getUuid() != targetPlayerUuid) {
			playingRole.getGamePlayer().writeAndFlush(37024, RetCode.PVP_PLAYER_NOT_EXIST);
			return;
		}
		final CrossBossPlayer crossBossPlayer = crossBossRoom.getPlayerMap().get(targetPlayerUuid);
		final CrossBossEntity crossBossEntity = crossBossRoom.getCrossBossEntity();
		
		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {

			@Override
			public void run() {
				// 推送
				PushBossPropChange.Builder pushBuilder = PushBossPropChange.newBuilder()
						.setPlayerId(pvpPropChangeMsg.getTargetPlayerUuid()).setPropType(pvpPropChangeMsg.getPropType())
						.setVal(pvpPropChangeMsg.getVal()).setCastPlayerId(pvpPropChangeMsg.getCastPlayerUuid());
				if (pvpPropChangeMsg.getPropType() == PVP_PROPERTIES.PVP_PROP_HP) {
					pushBuilder.setIsCrit(pvpPropChangeMsg.getIsCrit());
				} else if (pvpPropChangeMsg.getPropType() == PVP_PROPERTIES.PVP_PROP_SP) {
					pushBuilder.setShowTip(false);
				}
				// 处理
				if(crossBossPlayer != null){
					BossBattleManager.getInstance().propChange(crossBossRoom, crossBossPlayer, pvpPropChangeMsg,pushBuilder);
				}
				else{
					BossBattleManager.getInstance().bossPropChange(crossBossRoom, crossBossEntity, pvpPropChangeMsg,pushBuilder);
				}
				//ret
				for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
					GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
					if (netty4Session != null && netty4Session.isChannelActive()) {
						netty4Session.write(37026, pushBuilder);
					}
				}
				// ret
				playingRole.getGamePlayer().writeAndFlush(37024, S2CBossPropChange.newBuilder().build());
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
