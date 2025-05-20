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
import ws.WsMessageBattle.C2SCraftSwitchStatus;
import ws.WsMessageBattle.PVP_STATE;
import ws.WsMessageBattle.PushCraftSwitchStatus;
import ws.WsMessageBattle.S2CCraftSwitchStatus;
import ws.WsMessageBattle.S2CPvpSwitchState;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36011, accessLimit = 0)
public class CraftSwitchStatusProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftSwitchStatusProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		final C2SCraftSwitchStatus reqMsg = ProtoUtil.getProtoObj(C2SCraftSwitchStatus.PARSER, request);
		logger.info("craft switch status,playerId={}", playingRole.getId());
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CRAFT_BATTLE) {
			// playingRole.getGamePlayer().writeAndFlush(35114,
			// RetCode.PVP_PLAYER_STATUS_ERROR);
			logger.error("playerId={},error={}", playingRole.getId(), RetCode.CRAFT_PLAYER_STATUS_ERROR);
			playingRole.getGamePlayer().writeAndFlush(36012, S2CPvpSwitchState.newBuilder().build());
			return;
		}
		// 战斗是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36012, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		final int playerId = playingRole.getId();
		final int serverId = playingRole.getPlayerBean().getServerId();
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new CraftPlayerInfo(serverId,playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36012, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// do
		CraftManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				// 能否释放技能,技能cd时间判断
				if (reqMsg.getTargetState() == PVP_STATE.PVP_STATE_CAST_SKILL && reqMsg.getSkillId() % 10 != 1) {
					Map<Integer, CastSkillInfo> heroCastSkillMap = CraftLogic.getInstance()
							.getCastSkillTimeInfo(playerId);
					if (heroCastSkillMap != null && heroCastSkillMap.get(reqMsg.getHeroId()) != null) {
						CastSkillInfo castSkillInfo = heroCastSkillMap.get(reqMsg.getHeroId());
						int timeDiff = (int) (System.currentTimeMillis() - castSkillInfo.getTime());
						float targetDelay = castSkillInfo.getDelay();
						if (timeDiff < targetDelay * 980) {
							logger.info(
									"craft cast skill detect speed up!playerId={},heroId={},skillId={},targetDelay={},timeDiff={}",
									playerId, reqMsg.getHeroId(), reqMsg.getSkillId(), reqMsg.getD(), timeDiff);
							//castSkillInfo.setDelay(castSkillInfo.getDelay() * 2);
							playingRole.getGamePlayer().writeAndFlush(36012, S2CCraftSwitchStatus.newBuilder());
							return;
						}
					}
				}
				PVP_STATE newState = reqMsg.getTargetState();
				PushCraftSwitchStatus.Builder pushBuilder = PushCraftSwitchStatus.newBuilder()
						.setPlayerId(playingRole.getId()).setServerId(serverId).setHeroId(reqMsg.getHeroId()).setTargetState(newState)
						.addAllTargetHero(reqMsg.getTargetHeroList());
				switch (newState) {
				case PVP_STATE_CHASE:
					pushBuilder.setAutoSwitchTarget(reqMsg.getAutoSwitchTarget());
					for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
						if (aPvpPlayer.getPlayingRole() != null
								&& aPvpPlayer.getPlayingRole().getGamePlayer() != null) {
							aPvpPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36014, pushBuilder);
						}
					}
					break;
				case PVP_STATE_PREPARE_ATTACK:
					for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
						if (aPvpPlayer.getPlayingRole() != null
								&& aPvpPlayer.getPlayingRole().getGamePlayer() != null) {
							aPvpPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36014, pushBuilder);
						}
					}
					break;
				case PVP_STATE_CAST_SKILL:
					pushBuilder.setSkillId(reqMsg.getSkillId());
					for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
						if (aPvpPlayer.getPlayingRole() != null
								&& aPvpPlayer.getPlayingRole().getGamePlayer() != null) {
							aPvpPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36014, pushBuilder);
						}
					}
					// 保存释放技能信息
					if (reqMsg.getSkillId() % 10 != 1) {
						Map<Integer, CastSkillInfo> heroCastSkillMap = CraftLogic.getInstance()
								.getCastSkillTimeInfo(playerId);
						if (heroCastSkillMap == null) {
							heroCastSkillMap = new HashMap<>();
							CraftLogic.getInstance().addCastSkillTimeMap(playerId, heroCastSkillMap);
						}
						int heroId = reqMsg.getHeroId();
						CastSkillInfo heroCastSkillInfo = heroCastSkillMap.get(heroId);
						if (heroCastSkillInfo == null) {
							heroCastSkillInfo = new CastSkillInfo(System.currentTimeMillis(), reqMsg.getD());
							heroCastSkillMap.put(heroId, heroCastSkillInfo);
						} else {
							heroCastSkillInfo.setDelay(reqMsg.getD());
							heroCastSkillInfo.setTime(System.currentTimeMillis());
						}
					}
					break;
				case PVP_STATE_DIE:
					break;
				default:
					break;
				}
				playingRole.getGamePlayer().writeAndFlush(36012, S2CCraftSwitchStatus.newBuilder());
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
