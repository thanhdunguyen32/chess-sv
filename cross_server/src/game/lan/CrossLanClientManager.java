package game.lan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.StringConstants;
import game.module.battle.bean.DBCraftRecordItem;
import game.module.cross.bean.CrossBossFinish;
import game.util.Global;
import io.netty.channel.Channel;
import io.protostuff.ProtostuffIOUtil;
import lion.lan.ILanIoExecutor;
import lion.lan.LanClient;
import lion.lan.LanClientSession;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.SendToMessage;

public class CrossLanClientManager implements ILanIoExecutor {

	private LanClient lanClient;

	private Map<String, LanClientSession> gsConnectionSessionMap = new HashMap<String, LanClientSession>();

	private static Logger logger = LoggerFactory.getLogger(CrossLanClientManager.class);

	public CrossLanClientManager() {
		lanClient = new LanClient(this);
		// gsConnectSession = new LanClientSession(lanClient);
	}

	public void shutdown() {
		if (lanClient != null) {
			lanClient.close();
		}
	}

	public boolean connect(String hostName, int port) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(hostName + StringConstants.SEPARATOR_HENG + port);
		if (gsConnectionSession == null) {
			gsConnectionSession = new LanClientSession(lanClient);
			gsConnectionSessionMap.put(hostName + StringConstants.SEPARATOR_HENG + port, gsConnectionSession);
		}
		// already connected
		if (gsConnectionSession.isConnected()) {
			return true;
		}
		boolean ret = false;
		synchronized (gsConnectionSession) {
			try {
				gsConnectionSession.connect(hostName, port);
				ret = true;
			} catch (Exception e) {
				logger.error("lan connect to " + hostName + ":" + port + " error!", e);
			}
		}
		return ret;
	}

	@Override
	public void execute(Channel channel, RequestMessage msg) throws IOException {
		logger.info("reveive msgCode={}", msg.getMsgCode());
		switch (msg.getMsgCode()) {
		case 11004:
			break;
		default:
			break;
		}
	}

	public void sendCraftResult2Gs(String gsHost, int gsPort, int playerId, boolean isWin,
			DBCraftRecordItem craftRecordItem) {
		logger.info("send craft result 2 gs,playerId={},isWin={},craftRecordItem={}", playerId, isWin, craftRecordItem);
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(gsHost + StringConstants.SEPARATOR_HENG + gsPort);
		SendToMessage sendToMessage = new SendToMessage(gsConnectionSession.alloc(), 30005);
		// 序列化
		sendToMessage.writeInt(playerId);
		sendToMessage.writeBoolean(isWin);
		// write
		byte[] msgBytes = ProtostuffIOUtil.toByteArray(craftRecordItem, DBCraftRecordItem.getSchema(),
				Global.getProtoBuffer());
		sendToMessage.writeBytes(msgBytes);
		// send
		gsConnectionSession.writeAndFlush(sendToMessage);
	}

	public void sendCraftCancel2Gs(String gsHost, int gsPort, int aPlayerId, boolean isPunish) {
		logger.info("send craft cancel 2 gs,playerId={},isPunish={}", aPlayerId, isPunish);
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(gsHost + StringConstants.SEPARATOR_HENG + gsPort);
		SendToMessage sendToMessage = new SendToMessage(gsConnectionSession.alloc(), 30007);
		// 序列化
		sendToMessage.writeInt(aPlayerId);
		sendToMessage.writeBoolean(isPunish);
		// send
		gsConnectionSession.writeAndFlush(sendToMessage);
	}

	public void sendBossEnd2Gs(String gsHost, int gsPort, int playerId, CrossBossFinish crossBossFinishMsg) {
		logger.info("send boss result 2 gs,playerId={},crossBossFinishMsg={}", playerId, ReflectionToStringBuilder.toString(crossBossFinishMsg));
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(gsHost + StringConstants.SEPARATOR_HENG + gsPort);
		SendToMessage sendToMessage = new SendToMessage(gsConnectionSession.alloc(), 30009);
		// 序列化
		sendToMessage.writeInt(playerId);
		// write
		byte[] msgBytes = ProtostuffIOUtil.toByteArray(crossBossFinishMsg, CrossBossFinish.getSchema(),
				Global.getProtoBuffer());
		sendToMessage.writeBytes(msgBytes);
		// send
		gsConnectionSession.writeAndFlush(sendToMessage);
	}
	
}
