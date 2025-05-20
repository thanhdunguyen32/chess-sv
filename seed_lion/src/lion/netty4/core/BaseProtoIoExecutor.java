package lion.netty4.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.common.ProcessorStatManager;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.IGameServer;
import lion.netty4.message.INetty4EventHandler;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.HttpProcessor;
import lion.netty4.processor.MsgProcessor;
import lion.netty4.processor.ProcessorType;

public class BaseProtoIoExecutor {

	public static final Logger logger = LoggerFactory.getLogger(BaseProtoIoExecutor.class);

	public static final Map<ChannelId, GamePlayer> gamePlayerMap = new ConcurrentHashMap<ChannelId, GamePlayer>();

	public static volatile boolean acceptRequest = true;

	private static IGameServer gameServer;

	public static final Set<INetty4EventHandler> handlerSet = new HashSet<INetty4EventHandler>();

	public static void setGameServer(IGameServer pServer) {
		gameServer = pServer;
	}

	public static void executeIoRequest(final Channel channel, RequestProtoMessage request) {
		// statLog(request);
		if (!acceptRequest) {
			return;
		}
		GamePlayer player = gamePlayerMap.get(channel.id());
		if (player == null) {
			logger.info("player is null");
			return;
		}
		long currentTime = System.currentTimeMillis();
		player.setLastVisitTime(currentTime);
		int msgCode = request.getMsgCode();
		// 该消息处理是系统消息，但ip不是授权ip
		if (msgCode < 100 && !gameServer.checkIP(player.getAddress())) {
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
				ProcessorStatManager.getInstance().logStart(msgCode, channel, currentTime);
				processor.process(player, request);
				statLog(request);
			} catch (Exception e) {
				logger.error("", e);
			}
		} else {
			gameServer.syncExecuteIoRequest(player, request);
			statLog(request);
		}
	}

	private static void statLog(RequestProtoMessage request) {
		ServerStat.queryCount.incrementAndGet();
		int packageSize = request.getLength();
		if (ServerStat.maxPackageSize < packageSize) {
			ServerStat.maxPackageSize = packageSize;
		}
	}

	public static void removeGamePlayer(Channel channel) {
		GamePlayer gamePlayer = gamePlayerMap.get(channel.id());
		if (gamePlayer != null) {
			for (INetty4EventHandler eventHandler : handlerSet) {
				try {
					eventHandler.channelInactive(gamePlayer);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		gamePlayerMap.remove(channel.id());
	}

	public static void kickAll() {
		try {
			for (GamePlayer gp : gamePlayerMap.values()) {
				gp.getChannel().close().sync();
			}
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		gamePlayerMap.clear();
	}

	public static void initGamePlayer(Channel channel) {
		gamePlayerMap.put(channel.id(), new GamePlayer(channel));
	}

	public static int getOnlineCount() {
		return gamePlayerMap.size();
	}

	/**
	 * 关闭操作
	 */
	public void stop() {
		acceptRequest = false;
		gamePlayerMap.clear();
	}

	public static void readTimeout(Channel channel) {
		GamePlayer gamePlayer = gamePlayerMap.get(channel.id());
		if (gamePlayer != null) {
			for (INetty4EventHandler eventHandler : handlerSet) {
				eventHandler.readTimeout(gamePlayer);
			}
		}
	}

	public static void executeHttpRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest,
			RequestProtoMessage protoMessage) {
		int msgCode = protoMessage.getMsgCode();
		HttpProcessor processor = gameServer.getHttpProcessor(msgCode);
		if (processor == null) {
			logger.warn("http processor not found!code={}", msgCode);
			return;
		}
		try {
			processor.process(ctx, httpRequest, protoMessage);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
