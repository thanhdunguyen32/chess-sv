package game.lan;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.bean.CrossBossPlayerJoinInfo;
import cross.boss.logic.BossPlayerManager;
import cross.boss.logic.BossRequestManager;
import cross.logic.CraftRequestManager;
import game.CrossServer;
import game.module.craft.logic.CraftMatchManager;
import game.module.cross.bean.CrossBossBegin;
import game.module.cross.bean.CrossCraftBegin;
import game.module.hero.dao.HeroCache;
import game.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.protostuff.ProtostuffIOUtil;
import lion.lan.ILanIoExecutor;
import lion.lan.LanServer;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.SendToMessage;

public class CrossLanServerManager implements ILanIoExecutor {

	private static Logger logger = LoggerFactory.getLogger(CrossLanServerManager.class);

	private LanServer lanServer;

	static class SingletonHolder {
		static CrossLanServerManager instance = new CrossLanServerManager();
	}

	public static CrossLanServerManager getInstance() {
		return SingletonHolder.instance;
	}

	private CrossLanServerManager() {
	}

	public void startServer(int port) {
		try {
			lanServer = new LanServer(port);
			lanServer.run(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		if (lanServer != null) {
			lanServer.shutdown();
		}
	}

	@Override
	public void execute(final Channel channel, RequestMessage requestMessage) throws IOException {
		// ip判断
		Set<String> allowIpSet = CrossServer.getInstance().getCrossServerConfig().getLanAllowIps();
		if (channel.remoteAddress() != null) {
			String ipStr = channel.remoteAddress().toString();
			String aIp = StringUtils.split(ipStr, ":")[0].substring(1);
			if (!allowIpSet.contains(aIp)) {
				logger.warn("not valid ip lan request,ipAddr={}", ipStr);
				return;
			}
		}
		switch (requestMessage.getMsgCode()) {
		case 30001:// 诸神争霸跨服消息
			// 序列化
			ByteBuf byteBuf = requestMessage.getByteArray();
			byte[] msgBytes = byteBuf.array();
			long sessionId = requestMessage.getLong();
			CrossCraftBegin crossCraftBegin = new CrossCraftBegin();
			ProtostuffIOUtil.mergeFrom(msgBytes, crossCraftBegin, CrossCraftBegin.getSchema());
			logger.info("receive player craft message,player_id={},server_id={},sessionId={}",
					crossCraftBegin.getPlayerId(), crossCraftBegin.getServerId(), sessionId);
			CraftRequestManager.getInstance().addRequest(sessionId, crossCraftBegin);
			// ret
			SendToMessage sendToMessage = new SendToMessage(channel.alloc(), 30002);
			sendToMessage.writeInt(crossCraftBegin.getPlayerId());
			sendToMessage.writeLong(sessionId);
			channel.writeAndFlush(sendToMessage);
			byteBuf.release();
			break;
		case 30003:// 诸神争霸取消匹配
			int playerId = requestMessage.getInt();
			int serverId = requestMessage.getInt();
			logger.info("receive craft cancel match msg,playerId={},serverId={}", playerId, serverId);
			//移除缓存
			CraftMatchManager.getInstance().cancelMatch(playerId, serverId);
			HeroCache.getInstance().removeCraftHeros(serverId, playerId);
			break;
		case 30005:// 跨服boss战开始加入
			// 反序列化
			byteBuf = requestMessage.getByteArray();
			msgBytes = byteBuf.array();
			sessionId = requestMessage.getLong();
			CrossBossBegin crossBossBegin = new CrossBossBegin();
			ProtostuffIOUtil.mergeFrom(msgBytes, crossBossBegin, CrossBossBegin.getSchema());
			logger.info("receive player cross boss join msg,player_id={},server_id={},sessionId={}",
					crossBossBegin.getPlayerId(), crossBossBegin.getServerId(), sessionId);
			//是否已经完成
			playerId = crossBossBegin.getPlayerId();
			serverId = crossBossBegin.getServerId();
			int roomTypeId = crossBossBegin.getRoomId();
			int addRet = 0;
			CrossBossPlayerJoinInfo bossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId, serverId);
			if(bossPlayerJoinInfo != null && bossPlayerJoinInfo.getFinishRoomIds()!= null && bossPlayerJoinInfo.getFinishRoomIds().contains(roomTypeId)){
				addRet = 1;
			}
			else{
				BossRequestManager.getInstance().addRequest(sessionId, crossBossBegin);
			}
			// ret
			sendToMessage = new SendToMessage(channel.alloc(), 30006);
			sendToMessage.writeInt(crossBossBegin.getPlayerId());
			sendToMessage.writeLong(sessionId);
			sendToMessage.writeInt(addRet);
			channel.writeAndFlush(sendToMessage);
			byteBuf.release();
			break;
		case 30007:
			playerId = requestMessage.getInt();
			serverId = requestMessage.getInt();
			CrossBossPlayerJoinInfo crossBossPlayerJoinInfo = BossPlayerManager.getInstance().getPlayerJoinInfo(playerId, serverId);
			sendToMessage = new SendToMessage(channel.alloc(), 30008);
			sendToMessage.writeInt(playerId);
			if(crossBossPlayerJoinInfo != null){
				//progress
				Map<Integer, Integer> progressMap = crossBossPlayerJoinInfo.getRoomIdMap();
				if(progressMap != null){
					sendToMessage.writeInt(progressMap.size());
					for(Integer aRoomTypeId : progressMap.keySet()){
						sendToMessage.writeInt(aRoomTypeId);
					}
				}
				else{
					sendToMessage.writeInt(0);
				}
				//finish
				Set<Integer> finishRoomIds = crossBossPlayerJoinInfo.getFinishRoomIds();
				if(finishRoomIds != null){
					sendToMessage.writeInt(finishRoomIds.size());
					for(Integer aRoomTypeId : finishRoomIds){
						sendToMessage.writeInt(aRoomTypeId);
					}
				}
				else{
					sendToMessage.writeInt(0);
				}
			}
			else{
				sendToMessage.writeInt(0);
				sendToMessage.writeInt(0);
			}
			channel.writeAndFlush(sendToMessage);
			break;
		default:
			break;
		}
	}

}
