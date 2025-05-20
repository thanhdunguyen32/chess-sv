package lion.netty4.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.IGameServer;
import lion.netty4.message.INetty4EventHandler;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.processor.MsgProcessor;
import lion.netty4.processor.ProcessorType;

public class MyIoExecutor {

	public static final Logger logger = LoggerFactory.getLogger(MyIoExecutor.class);

	public static volatile boolean acceptRequest = true;

	private static IGameServer gameServer;

	public static final Set<INetty4EventHandler> handlerSet = new HashSet<INetty4EventHandler>();

	public static final Map<ChannelId, GamePlayer> gamePlayerMap = new ConcurrentHashMap<ChannelId, GamePlayer>();

	public static void setGameServer(IGameServer pServer) {
		gameServer = pServer;
	}

	public static void executeIoRequest(final Channel channel, MyRequestMessage requestMsg) {
		if (!acceptRequest) {
			logger.warn("not accept request!");
			return;
		}
		GamePlayer player = gamePlayerMap.get(channel.id());
		if (player == null) {
			logger.info("player is null");
			return;
		}
		player.setLastVisitTime(System.currentTimeMillis());
		int msgCode = requestMsg.getMsgCode();
		// 该消息处理是系统消息，但ip不是授权ip
		if (msgCode < 100 && !gameServer.checkIP(player.getAddress())) {
			logger.error("invalid request from {}", player.getAddress());
			return;
		}
		MsgProcessor processor = gameServer.getMsgProcessor(msgCode);
		if (processor == null) {
			return;
		}
		if (!processor.isEnable()) {
			logger.warn("msg processor is closed:{}", msgCode);
			return;
		} else if (processor.getProcessorType() == ProcessorType.MultiThread) {
			try {
				processor.process(player, requestMsg);
				statLog(requestMsg);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			gameServer.syncExecuteIoRequest(player, requestMsg);
			statLog(requestMsg);
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
	
	private static void statLog(MyRequestMessage request) {
		ServerStat.queryCount.incrementAndGet();
		int packageSize = request.getLength();
		if (ServerStat.maxPackageSize < packageSize) {
			ServerStat.maxPackageSize = packageSize;
		}
	}

	public static void initGamePlayer(Channel channel) {
		gamePlayerMap.put(channel.id(), new GamePlayer(channel));
	}

	public static int getOnlineCount() {
		return gamePlayerMap.size();
	}

	public static GamePlayer getGamePlayer(ChannelId channelId){
		return gamePlayerMap.get(channelId);
	}

	/**
	 * 关闭操作
	 */
	public void stop() {
		acceptRequest = false;
		gamePlayerMap.clear();
	}

}
