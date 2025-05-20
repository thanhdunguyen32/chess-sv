package login.logic;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lion.lan.ILanIoExecutor;
import lion.lan.LanClient;
import lion.lan.LanClientSession;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.SendToByteMessage;
import login.bean.IResponseLanMessage;
import login.lan.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginLanClientManager implements ILanIoExecutor {

	private LanClient lanClient;

	private Map<String, LanClientSession> gsConnectionSessionMap = new HashMap<String, LanClientSession>();

	private static Logger logger = LoggerFactory.getLogger(LoginLanClientManager.class);

	private Map<String,IResponseLanMessage> responseHanlderMap = new ConcurrentHashMap<>();
	
	public LoginLanClientManager() {
		lanClient = new LanClient(this);
		// gsConnectSession = new LanClientSession(lanClient);
	}

	public void shutdown() {
		if (lanClient != null) {
			lanClient.close();
		}
	}

	public boolean connect(String hostName, int port) {
		logger.info("connect to hostName={},port={}", hostName, port);
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + port);
		if (gsConnectionSession == null) {
			gsConnectionSession = new LanClientSession(lanClient);
			gsConnectionSessionMap.put(hostName + ":" + port, gsConnectionSession);
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
	public void execute(Channel channel, RequestByteMessage msg) throws IOException {
		logger.info("reveive msgCode={}", msg.getMsgCode());
		switch (msg.getMsgCode()) {
		case 10010:
			String openId = msg.readString();
			IResponseLanMessage responseHanlder = responseHanlderMap.get(openId);
			if (responseHanlder != null) {
				responseHanlder.parse(msg);
				responseHanlderMap.remove(openId);
			}
			break;
		default:
			break;
		}
	}

	public ChannelFuture sendPayment2Gs(String gsHostName, int port, String userid, String orderid, int money, int time, String pid, int serverid) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(gsHostName + ":" + port);
		Payment2GsMessage gsMessage = new Payment2GsMessage(gsConnectionSession.alloc());
		gsMessage.setMessage(userid, orderid, money, time, pid,serverid);
		logger.info(
				"sendPayment2Gs,host={},port={},userid={},orderid={},money={},time={},pid={},serverid={}",
				gsHostName, port, userid, orderid, money, time, pid,serverid);
		return gsConnectionSession.writeAndFlush(gsMessage);
	}

	public ChannelFuture send185SyFanli(String gsHostName, int lanPort, String username, String orderId, int moneyYuan, Integer passTime,
			Integer is_vip, Integer server_id) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(gsHostName + ":" + lanPort);
		SendToByteMessage gsMessage = new SendToByteMessage(gsConnectionSession.alloc(), 11009);
		gsMessage.writeString(username);
		gsMessage.writeString(orderId);
		gsMessage.writeInt(moneyYuan);
		gsMessage.writeInt(passTime);
		gsMessage.writeInt(is_vip);
		gsMessage.writeInt(server_id);
		logger.info(
				"send 185 sy fanli 2 GameServer,host={},userid={},orderid={},money={},time={},is_vip={},serverid={}",
				gsHostName, username, orderId, moneyYuan, passTime, is_vip,server_id);
		return gsConnectionSession.writeAndFlush(gsMessage);
	}

	public ChannelFuture sendLogin2Gs(String gsHostName, int port, long sessionId, String userId, int serverId,
			IResponseLanMessage responseLanMessage) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(gsHostName + ":" + port);
		Login2GsMessage gsMessage = new Login2GsMessage(gsConnectionSession.alloc());
		gsMessage.setMessage(sessionId, userId, serverId);
		logger.info("send Login2GsMessage,ip={}:{},sid={},userId={}", gsHostName,port, sessionId, userId);
		responseHanlderMap.put(userId, responseLanMessage);
		return gsConnectionSession.writeAndFlush(gsMessage);
	}

}
