package cc.mrbird.febs.system.service.lan;

import cc.mrbird.febs.common.properties.GmProperties;
import cc.mrbird.febs.system.entity.Activity4Gm;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.MailItem;
import cc.mrbird.febs.system.entity.ServerInfoBean;
import cc.mrbird.febs.system.lan_obj.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lion.lan.ILanIoExecutor;
import lion.lan.LanClient;
import lion.lan.LanClientSession;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.SendToByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class GmLanManager implements ILanIoExecutor {

	private LanClient lanClient;

	private Map<String, LanClientSession> gsConnectionSessionMap = new HashMap<String, LanClientSession>();

	@Autowired
	private GmProperties gmProperties;

	public GmLanManager() {
		lanClient = new LanClient(this);
	}

	public void shutdown() {
		if (lanClient != null) {
			lanClient.close();
		}
	}

	public boolean connect(GsEntity gsEntity) {
		String host = gsEntity.getHost();
		int port = gsEntity.getPort();
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(host + ":" + port);
		if (gsConnectionSession == null) {
			gsConnectionSession = new LanClientSession(lanClient);
			gsConnectionSessionMap.put(host + ":" + port, gsConnectionSession);
		}
		// already connected
		if (gsConnectionSession.isConnected()) {
			return true;
		}
		boolean ret = false;
		synchronized (gsConnectionSession) {
			try {
				gsConnectionSession.connect(host, port);
				ret = true;
			} catch (Exception e) {
				log.error("lan connect to " + host + ":" + port + " error!", e);
			}
		}
		return ret;
	}

	public void sendMailItem2Gs(String hostName, int lanPort, MailItem mailItem) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendMailItem2GsMessage msg = new SendMailItem2GsMessage(gsConnectionSession.alloc());
		msg.setMessage(mailItem.getAddressee(), mailItem.getReceiveId(), mailItem.getSender(), mailItem.getMailTitle(),
				mailItem.getMailContent(), mailItem.getMailAttach(), mailItem.getValidityTime());
		log.info(
				"sendMailItem2Gs,host={},Addressee={}, receiveId={}, sender={}, title={}, content={}, attach={}, validity={}",
				hostName + ":" + lanPort, mailItem.getAddressee(), mailItem.getReceiveId(), mailItem.getSender(),
				mailItem.getMailTitle(), mailItem.getMailContent(), mailItem.getMailAttach(),
				mailItem.getValidityTime());
		gsConnectionSession.writeAndFlush(msg);
	}

	public void getZoneId(String hostName, int lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		GetZoneIdMessage getZoneIdMessage = new GetZoneIdMessage(gsConnectionSession.alloc());
		log.info("send GetZoneIdMessage,host={}", hostName);
		gsConnectionSession.writeAndFlush(getZoneIdMessage);
	}

	public void getAllActivityBase(String hostName, int lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		GetActivityBaseMessage msg = new GetActivityBaseMessage(gsConnectionSession.alloc());
		// msg.writeInt(1);
		log.info("send GetActivityBaseMessage,host={}", hostName);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
		// log.info("send getAllActivityBase#2");
	}

	public void getActivityById(String hostName, int lanPort, int id) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		GetActivityByIdMessage msg = new GetActivityByIdMessage(gsConnectionSession.alloc(), id);
		log.info("send GetActivityByIdMessage,host={},id={}", hostName, id);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void getBanPlayerList(String hostName, int lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		GetBanPlayerNameMessage msg = new GetBanPlayerNameMessage(gsConnectionSession.alloc());
		log.info("send getBanPlayerList,host={}", hostName);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	// day la cho lang nghe server game tra ve
	@Override
	public void execute(Channel channel, RequestByteMessage msg) throws IOException {
		log.info("reveive msgCode={}", msg.getMsgCode());
		switch (msg.getMsgCode()) {
		case 10014:
		case 10018:
		case 10020:
		case 10022:
		case 10024:
		case 10026:
		case 10028:
		case 10040:
		case 10044:
		case 10048:
		case 10050:
		case 10052:
		case 10054:
		case 10056:
		case 10058:
		case 10060:
        case 10062:
        case 10064:
        case 11012:
		case 10071:
		case 10074:
        case 11014:
        case 11002:
			msg.retain();
			GsSyncRequest.getInstance().doReceive(msg);
			break;
		default:
			break;
		}
	}

	public void updateActivity(String hostName, int lanPort, Activity4Gm activity4Gm) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendActivityOneMessage msg = new SendActivityOneMessage(gsConnectionSession.alloc());
		msg.setVal(activity4Gm.getTemplateId(), activity4Gm.getStartTime(), activity4Gm.getEndTime(),
				activity4Gm.getIsOpen(), activity4Gm.getTitle(), activity4Gm.getDescription(), activity4Gm.getParams());
		log.info("send SendActivityOneMessage,host={}", hostName+":"+lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void fengHao(String hostName, int lanPort, Integer playerId, long time) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendFenghaoMessage msg = new SendFenghaoMessage(gsConnectionSession.alloc());
		msg.setVal(playerId, time);
		log.info("send fengHao message,host={}", hostName+":"+lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void jieFeng(String hostName, int lanPort, Integer playerId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendCancelFenghaoMessage msg = new SendCancelFenghaoMessage(gsConnectionSession.alloc());
		msg.setVal(playerId);
		log.info("send cancel fengHao message,host={}", hostName+":"+lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void jinYan(String hostName, int lanPort, int playerId, long time) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendJinYanMessage msg = new SendJinYanMessage(gsConnectionSession.alloc());
		msg.setVal(playerId, time);
		log.info("send jinYan message,host={}", hostName+":"+lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void jinYanCancel(String hostName, int lanPort, int playerId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendJinYanCancelMessage msg = new SendJinYanCancelMessage(gsConnectionSession.alloc());
		msg.setVal(playerId);
		log.info("send cancel jinYan message,host={}", hostName+":"+lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void kick(String hostName, int lanPort, Integer playerId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendKickMessage msg = new SendKickMessage(gsConnectionSession.alloc());
		msg.setVal(playerId);
		log.info("send kick message,host={}", hostName+":"+lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	/**
	 * 跑马灯公告
	 * 
	 * @param hostName
	 * @param lanPort
	 * @param content
	 * @param repeateCount
	 */
	public void subtitle(String host, int port, String content, int repeatCount) {
		try {
			// Lấy session kết nối tới game server
			LanClientSession gsConnectionSession = gsConnectionSessionMap.get(host + ":" + port);
			if (gsConnectionSession == null || !gsConnectionSession.isConnected()) {
				log.error("No connection to game server {}:{}", host, port);
				throw new RuntimeException("Không có kết nối tới game server");
			}

			// Tạo message để gửi tới game server
			SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10037);
			msg.writeString(content);  // Gửi nội dung trực tiếp, không cần encode vì đã được xử lý ở tầng message
			msg.writeInt(repeatCount);

			// Gửi message
			log.info("Sending subtitle to {}:{} - Content: {}, RepeatCount: {}", 
					host, port, content, repeatCount);
			gsConnectionSession.writeAndFlush(msg);

		} catch (Exception e) {
			log.error("Error sending subtitle", e);
			throw e;
		}
	}

	public void fakepay(String hostName, int lanPort, int playerId, int moneyYuan,String productId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10067);
		msg.writeInt(playerId);
		msg.writeInt(moneyYuan);
		msg.writeString(productId);
		log.info("send fakepay message,host={},port={}", hostName, lanPort);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void announcement(String hostName, int lanPort, String content) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendAnnouncementMessage msg = new SendAnnouncementMessage(gsConnectionSession.alloc());
		msg.setVal(content);
		log.info("send Announcement message,host={}", hostName);
		gsConnectionSession.writeAndFlush(msg);
	}

	public boolean connectLs(String lsHostName, int lsLanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(lsHostName + ":" + lsLanPort);
		if (gsConnectionSession == null) {
			gsConnectionSession = new LanClientSession(lanClient);
			gsConnectionSessionMap.put(lsHostName + ":" + lsLanPort, gsConnectionSession);
		}
		// already connected
		if (gsConnectionSession.isConnected()) {
			return true;
		}
		boolean ret = false;
		synchronized (gsConnectionSession) {
			try {
				gsConnectionSession.connect(lsHostName, lsLanPort);
				ret = true;
			} catch (Exception e) {
				log.error("lan connect to LoginServer[" + lsHostName + ":" + lsLanPort + "] error!", e);
			}
		}
		return ret;
	}

	public void getLsAnnouncement(String lsHostName, Integer lsLanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(lsHostName + ":" + lsLanPort);
		GetLsAnnouncementMessage msg = new GetLsAnnouncementMessage(gsConnectionSession.alloc());
		// msg.writeInt(1);
		log.info("send GetLsAnnouncementMessage,host={},port={}", lsHostName, lsLanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void getLsServerStatus(String lsHostName, Integer lsLanPort, int serverId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(lsHostName + ":" + lsLanPort);
		GetLsServerStatusMessage msg = new GetLsServerStatusMessage(gsConnectionSession.alloc());
		msg.writeInt(serverId);
		log.info("send getLsServerStatus,host={},port={},serverId={}", lsHostName, lsLanPort, serverId);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void setServerStatus(String hostName, Integer lanPort, int serverId, int newStatus) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendServerNewStatusMessage msg = new SendServerNewStatusMessage(gsConnectionSession.alloc());
		msg.setVal(serverId, newStatus);
		log.info("send new server status message,host={}", hostName);
		gsConnectionSession.writeAndFlush(msg);
	}

	public void resetActivityByType(String hostName, Integer lanPort, int activityType) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10049);
		msg.writeInt(activityType);
		log.info("send retset activity by type message,host={},activity_type={}", hostName, activityType);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getMainStatParams(String hostName, Integer lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10051);
		log.info("send getMainStatParams msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getTopPayPlayers(String hostName, Integer lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10053);
		log.info("send getTopPayPlayers msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getChatContent(String hostName, Integer lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10055);
		log.info("send getChatContent msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getDailyPayLog(String hostName, Integer lanPort, String payTime) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10057);
		msg.writeString(payTime);
		log.info("send getDailyPayLog msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getPayLogAll(String hostName, Integer lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10059);
		log.info("send getPayLogAll msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}

    public void getPlayerAllBase(String hostName, Integer lanPort) {
        LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
        SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10063);
        log.info("send getPlayerAllBase msg,host={},lanPort={}", hostName, lanPort);
        GsSyncRequest.getInstance().setRetMsg(null);
        gsConnectionSession.writeAndFlush(msg);
    }
    
    public void getRechargeList(String hostName, Integer lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 11011);
		log.info("send getRechargeList msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getItemGoLog(String hostName, Integer lanPort, int itemId, int playerId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10061);
		msg.writeInt(itemId);
		msg.writeInt(playerId);
		log.info("send getItemGoLog msg,host={},lanPort={}", hostName, lanPort);
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	public void getPlayerFromServer(String hostName, int lanPort) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
		if (gsConnectionSession == null || !gsConnectionSession.isConnected()) {
			log.error("No connection or not connected to {}:{}", hostName, lanPort);
			return;
		}
		
		// Tạo 1 message (SendToByteMessage) với msgcode=10071
		SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 10071);
		// Gửi
		log.info("send getOnlineUserStat msg,host={},lanPort={}", hostName, lanPort);
		// Dọn retMsg cũ
		GsSyncRequest.getInstance().setRetMsg(null);
		gsConnectionSession.writeAndFlush(msg);
	}
	
	public void getServerList(String host, int port) {
		LanClientSession session = gsConnectionSessionMap.get(host + ":" + port);
		if (session == null || !session.isConnected()) {
			log.error("No connection to {}:{}", host, port);
			return;
		}
		// Tạo message code=10073 => “request DS server_info”
		SendToByteMessage req = new SendToByteMessage(session.alloc(), 10073);
		
		log.info("send getServerList msg,host={},port={}", host, port);
		
		// Clear retMsg cũ
		GsSyncRequest.getInstance().setRetMsg(null);
		
		// Gửi
		session.writeAndFlush(req);
	}
	
	
	public void getUserInfo(String hostName, Integer lanPort, String username, int serverID) {
        LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
        SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 11013);
        msg.writeInt(serverID);
        msg.writeString(username);
        log.info("send getUserInfo msg,host={},lanPort={}", hostName, lanPort);
        GsSyncRequest.getInstance().setRetMsg(null);
        gsConnectionSession.writeAndFlush(msg);
    }

    public void buyPaymentItem(String hostName, Integer lanPort, String userId, String orderId, int money, int time, String pid, int serverId) {
        LanClientSession gsConnectionSession = gsConnectionSessionMap.get(hostName + ":" + lanPort);
        SendToByteMessage msg = new SendToByteMessage(gsConnectionSession.alloc(), 11001);
        msg.writeString(userId);
        msg.writeString(orderId);
        msg.writeInt(money);
        msg.writeInt(time);
        msg.writeString(pid);
        msg.writeInt(serverId);
        log.info("send buyPaymentItem msg,host={},lanPort={}", hostName, lanPort);
        GsSyncRequest.getInstance().setRetMsg(null);
        gsConnectionSession.writeAndFlush(msg);
    }
	
}