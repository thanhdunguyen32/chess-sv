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
import game.module.battle.ProtoMessageBattle.C2SCrossBossLoadReady;
import game.module.battle.ProtoMessageBattle.PushCrossBossJoinBattle;
import game.module.battle.ProtoMessageBattle.S2CCrossBossLoadReady;
import game.module.question.ProtoMessageQuestion.IOHeroSimpleInfo;
import game.util.concurrent.OrderedEventRunnable;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 37007, accessLimit = 200)
public class BossLoadReadyProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(BossLoadReadyProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SCrossBossLoadReady reqMsg = ProtoUtil.getProtoObj(C2SCrossBossLoadReady.PARSER, request);
		final int initHp = reqMsg.getInitHp();
		final int initSp = reqMsg.getInitSp();
		final float speedUpRate = reqMsg.getSpeedUpRate();
		logger.info("cross boss load ready!initHp={},initSp={}", initHp, initSp);
		// 状态是否正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CROSS_BOSS_LOADING) {
			playingRole.getGamePlayer().writeAndFlush(37008, RetCode.CROSS_BOSS_STATUS_ERROR);
			return;
		}
		// 获取房间id
		final int playerId = playingRole.getPlayerBean().getId();
		final int serverId = playingRole.getPlayerBean().getServerId();
		CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId,
				serverId);
		if (crossBossPlayerJoinInfo == null) {
			playingRole.getGamePlayer().writeAndFlush(37008, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomTypeId = crossBossPlayerJoinInfo.getRoomTypeId();
		Map<Integer, Integer> roomIdMap = crossBossPlayerJoinInfo.getRoomIdMap();
		if (roomIdMap == null || roomIdMap.get(roomTypeId) == null) {
			playingRole.getGamePlayer().writeAndFlush(37008, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		int roomId = roomIdMap.get(roomTypeId);
		final CrossBossRoom crossBossRoom = BossBattleManager.getInstance().getRoom(roomId);
		if (crossBossRoom == null) {
			playingRole.getGamePlayer().writeAndFlush(37008, RetCode.CROSS_BOSS_ROOM_NOT_EXIST);
			return;
		}
		// 正式加入房间
		BossBattleManager.getInstance().execute(new OrderedEventRunnable() {
			@Override
			public void run() {
				CrossPlayerId crossPlayerId = new CrossPlayerId(serverId, playerId);
				CrossBossPlayer crossBossPlayer = crossBossRoom.getLoadingPlayerMap().get(crossPlayerId.hashCode());
				if (crossBossPlayer == null) {
					playingRole.getGamePlayer().writeAndFlush(37008, RetCode.CROSS_BOSS_STATUS_ERROR);
					return;
				}
				crossBossRoom.getPlayerMap().put(crossPlayerId.hashCode(), crossBossPlayer);
				crossBossRoom.getLoadingPlayerMap().remove(crossPlayerId.hashCode());
				// 随机位置
				float[] myBornPosition = BossBattleManager.getInstance().randBornPosition();
				crossBossPlayer.setBorn_position(myBornPosition);
				crossBossPlayer.setPos_pair(new float[] { myBornPosition[0], myBornPosition[1] });
				if (crossBossPlayer.isFirstCreate()) {
					crossBossPlayer.setHp(initHp >= 100000 ? 2000 : initHp);
					crossBossPlayer.setSp(initSp >= 2000 ? 0 : initSp);
					crossBossPlayer.setMaxHp(crossBossPlayer.getHp());
					crossBossPlayer.setFirstCreate(false);
				}
				crossBossPlayer.setSpeedUpRate(speedUpRate);
				// 状态
				playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_CROSS_BOSS_BATTLE);
				// ret
				int elapseTime = (int) ((System.currentTimeMillis() - crossBossRoom.getStartTime()) / 1000);
				S2CCrossBossLoadReady.Builder retBuilder = S2CCrossBossLoadReady.newBuilder().setElapseTime(elapseTime);
				retBuilder.getBossInfoBuilder().setHp(crossBossRoom.getCrossBossEntity().getHp()).setSp(0)
						.addPosPair(crossBossRoom.getCrossBossEntity().getPos_pair()[0])
						.addPosPair(crossBossRoom.getCrossBossEntity().getPos_pair()[1])
						.setUuid(crossBossRoom.getCrossBossEntity().getUuid())
						.setSpeedUpRate(crossBossRoom.getCrossBossEntity().getSpeed_up_rate());
				for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
					if (aCrossBossPlayer.getNetty4Session() != null
							&& aCrossBossPlayer.getNetty4Session().isChannelActive()) {
						retBuilder.addMembersBuilder().setUuid(aCrossBossPlayer.getCrossPlayerId().hashCode())
								.setHp(aCrossBossPlayer.getHp()).setSp(aCrossBossPlayer.getSp())
								.addPosPair(aCrossBossPlayer.getPos_pair()[0])
								.addPosPair(aCrossBossPlayer.getPos_pair()[1])
								.setSpeedUpRate(aCrossBossPlayer.getSpeedUpRate());
					}
				}
				playingRole.getGamePlayer().writeAndFlush(37008, retBuilder);
				// push
				PushCrossBossJoinBattle.Builder pushBuilder = PushCrossBossJoinBattle.newBuilder();
				IOHeroSimpleInfo.Builder heroBuilder = pushBuilder.getMemberBuilder()
						.setId(crossBossPlayer.getCrossPlayerId().playerId)
						.setServerId(crossBossPlayer.getCrossPlayerId().serverId)
						.setName(crossBossPlayer.getCrossPlayerInfo().getPlayerName())
						.setLevel(crossBossPlayer.getCrossPlayerInfo().getLevel())
						.setUuid(crossBossPlayer.getCrossPlayerId().hashCode()).getHeroBuilder();
				BossBattleManager.buildIOHeroSimpleInfo(heroBuilder,
						crossBossPlayer.getCrossPlayerInfo().getHeroEntity());
				pushBuilder.getDynamicInfoBuilder().setHp(crossBossPlayer.getHp()).setSp(0)
						.addPosPair(crossBossPlayer.getPos_pair()[0]).addPosPair(crossBossPlayer.getPos_pair()[1])
						.setUuid(crossBossPlayer.getUuid()).setSpeedUpRate(crossBossPlayer.getSpeedUpRate());
				for (CrossBossPlayer aCrossBossPlayer : crossBossRoom.getPlayerMap().values()) {
					GamePlayer netty4Session = aCrossBossPlayer.getNetty4Session();
					if (netty4Session != null && netty4Session.isChannelActive()) {
						netty4Session.writeAndFlush(37010, pushBuilder);
					}
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
