package cross.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.logic.CrossCraftManager;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import ws.CraftRoom;
import ws.CraftPlayerInfo;
import ws.CraftRoomPlayer;
import ws.CraftManager;
import ws.WsMessageBattle.C2SCraftPropChange;
import ws.WsMessageBattle.PVP_PROPERTIES;
import ws.WsMessageBattle.PushCraftPropChange;
import ws.WsMessageBattle.S2CCraftPropChange;
import ws.WsMessageBattle.S2CPvpSwitchState;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36019, accessLimit = 0)
public class CraftPropChangeProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftPropChangeProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		final C2SCraftPropChange reqMsg = ProtoUtil.getProtoObj(C2SCraftPropChange.PARSER, request);
		final int playerId = playingRole.getId();
		final int serverId = playingRole.getPlayerBean().getServerId();
		logger.info("craft prop change,playerId={}", playerId);
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CRAFT_BATTLE) {
			logger.error("playerId={},error={}", playingRole.getId(), RetCode.CRAFT_PLAYER_STATUS_ERROR);
			playingRole.getGamePlayer().writeAndFlush(36020, S2CPvpSwitchState.newBuilder().build());
			return;
		}
		// 战斗是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36020, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new CraftPlayerInfo(serverId,playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36020, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// do
		CraftManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				// 推送
				PushCraftPropChange.Builder retBuilder = PushCraftPropChange.newBuilder()
						.setTargetPlayerId(reqMsg.getTargetPlayerId()).setTargetServerId(reqMsg.getTargetServerId()).setTargetHeroId(reqMsg.getTargetHeroId())
						.setPropType(reqMsg.getPropType()).setVal(reqMsg.getVal())
						.setCastPlayerId(reqMsg.getCastPlayerId()).setCastServerId(reqMsg.getCastServerId()).setCastHeroId(reqMsg.getCastHeroId());
				if (reqMsg.getPropType() == PVP_PROPERTIES.PVP_PROP_HP) {
					retBuilder.setIsCrit(reqMsg.getIsCrit());
				} else if (reqMsg.getPropType() == PVP_PROPERTIES.PVP_PROP_SP) {
					retBuilder.setShowTip(false);
				}
				for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
					if (aPlayer.getPlayingRole() != null && aPlayer.getPlayingRole().getGamePlayer() != null) {
						aPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36022, retBuilder.build());
					}
				}
				// ret
				playingRole.getGamePlayer().writeAndFlush(36020, S2CCraftPropChange.newBuilder().build());
				// 处理
				CrossCraftManager.getInstance().propChange(craftRoom, reqMsg);
			}

			@Override
			public Object getIdentifyer() {
				return craftRoom.getUuid();
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
