package cross.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.BeanCraftHeroInit;
import game.module.battle.ProtoMessageBattle.C2SCraftReady;
import game.module.battle.ProtoMessageBattle.PushCraftReady;
import game.module.battle.ProtoMessageBattle.S2CCraftReady;
import game.module.craft.bean.CraftRoom;
import game.module.craft.bean.CraftRoomPlayer;
import game.module.craft.bean.CraftRoomPlayer.CraftHeroBean;
import game.module.craft.logic.CraftManager;
import game.session.SessionManager;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36005, accessLimit = 400)
public class CraftLoadReadyProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftLoadReadyProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SCraftReady reqMsg = ProtoUtil.getProtoObj(C2SCraftReady.PARSER, request);
		logger.info("craft load ready,playerId={},msg={}", playingRole.getId(), reqMsg);
		final List<BeanCraftHeroInit> heroInfoList = reqMsg.getHeroInfoList();
		// 状态是否正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CRAFT_LOADING) {
			playingRole.getGamePlayer().writeAndFlush(36006, RetCode.CRAFT_PLAYER_STATUS_ERROR);
			return;
		}
		// 战斗是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36006, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new game.module.craft.bean.CraftRoom.CraftPlayerInfo(serverId, playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36006, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// 玩家已经ready
		if (craftRoomPlayer.isLoadReady()) {
			playingRole.getGamePlayer().writeAndFlush(36006, RetCode.CRAFT_PLAYER_ALREADY_IN_BATTLE);
			return;
		}
		// do
		CraftManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				craftRoomPlayer.setLoadReady(true);
				Map<Integer, CraftHeroBean> heroInfoMap = new HashMap<>();
				craftRoomPlayer.setHeroInfoMap(heroInfoMap);
				for (BeanCraftHeroInit beanCraftHeroInit : heroInfoList) {
					int heroId = beanCraftHeroInit.getHeroId();
					int initHp = beanCraftHeroInit.getInitHp();
					int initSp = beanCraftHeroInit.getInitSp();
					if (initHp >= 100000) {
						initHp = 2000;
					}
					if (initSp >= 2000) {
						initSp = 0;
					}
					heroInfoMap.put(heroId, new CraftHeroBean(heroId, initHp, initSp, initHp));
				}
				// push
				PushCraftReady.Builder pushBuilder = PushCraftReady.newBuilder().setPlayerId(playingRole.getId())
						.setServerId(playingRole.getPlayerBean().getServerId());
				for (CraftRoomPlayer aPvpPlayer : craftRoom.getOnlinePlayers().values()) {
					int playerId = aPvpPlayer.getPlayerId();
					PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
					if (pr != null && pr.getGamePlayer() != null) {
						pr.getGamePlayer().writeAndFlush(36008, pushBuilder);
					}
				}
				playingRole.getGamePlayer().writeAndFlush(36006, S2CCraftReady.newBuilder().build());
				//
				CraftManager.getInstance().battleStartCheck(craftRoom);
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
