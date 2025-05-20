package lion.netty4.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.IGameServer;
import lion.netty4.message.INetty4EventHandler;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.processor.MsgProcessor;
import lion.netty4.processor.ProcessorType;

public class BaseIoExecutor {

	public static final Logger logger = LoggerFactory
			.getLogger(BaseIoExecutor.class);

	public static volatile boolean acceptRequest = true;

	private static IGameServer gameServer;

	public static final Set<INetty4EventHandler> handlerSet = new HashSet<INetty4EventHandler>();

	public static final Set<Channel> gamePlayerSet = Collections
			.newSetFromMap(new ConcurrentHashMap<Channel, Boolean>());

	public static void setGameServer(IGameServer pServer) {
		gameServer = pServer;
	}

	public static void executeIoRequest(final Channel channel,
			final Object remoteObj) {
		if (!acceptRequest) {
			return;
		}
		final RequestByteMessage request = (RequestByteMessage) remoteObj;
		final GamePlayer player = channel.attr(SocketProtoServer.KEY_GAME_PLAYER)
				.get();
		if (player == null) {
			logger.info("player is null");
			return;
		}
		player.setLastVisitTime(System.currentTimeMillis());
		int msgCode = request.getMsgCode();
		// 该消息处理是系统消息，但ip不是授权ip
		if (msgCode < 10000 && !gameServer.checkIP(player.getAddress())) {
			logger.error("invalidate request from {}", player.getAddress());
			return;
		}
		MsgProcessor processor = gameServer.getMsgProcessor(msgCode);
		if (processor == null) {
			return;
		}
		if (!processor.isEnable()) {
			logger.warn("msg processor is closed " + msgCode);
			return;
		} else if (processor.getProcessorType() == ProcessorType.MultiThread) {
			try {
				processor.process(player, request);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			gameServer.syncExecuteIoRequest(player, request);
		}
	}

	public static void removeGamePlayer(Channel channel) {
		GamePlayer gamePlayer = channel.attr(SocketProtoServer.KEY_GAME_PLAYER)
				.get();
		if (gamePlayer != null) {
			for (INetty4EventHandler eventHandler : handlerSet) {
				try {
					eventHandler.channelInactive(gamePlayer);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		channel.attr(SocketProtoServer.KEY_GAME_PLAYER).set(null);
		gamePlayerSet.remove(channel);
	}

	public static void kickAll() {
		try {
			for (Channel aChannel : gamePlayerSet) {
				aChannel.close().sync();
			}
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		gamePlayerSet.clear();
	}

	public static void initGamePlayer(Channel channel) {
		channel.attr(SocketProtoServer.KEY_GAME_PLAYER).set(new GamePlayer(channel));
		gamePlayerSet.add(channel);
	}

	public static int getOnlineCount() {
		return gamePlayerSet.size();
	}

	/**
	 * 关闭操作
	 */
	public void stop() {
		acceptRequest = false;
		gamePlayerSet.clear();
	}

}
