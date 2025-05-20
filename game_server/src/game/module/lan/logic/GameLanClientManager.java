package game.module.lan.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lion.netty4.message.SendToByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.entity.PlayingRole;
import game.module.lan.msg.SendLevelup2BusMessage;
import game.module.lan.msg.SendRegist2BusMessage;
import game.session.SessionManager;
import io.netty.channel.Channel;
import lion.common.StringConstants;
import lion.lan.ILanIoExecutor;
import lion.lan.LanClient;
import lion.lan.LanClientSession;
import lion.netty4.message.RequestByteMessage;

public class GameLanClientManager implements ILanIoExecutor {

	private LanClient lanClient;

	private Map<String, LanClientSession> gsConnectionSessionMap = new HashMap<String, LanClientSession>();

	private static Logger logger = LoggerFactory.getLogger(GameLanClientManager.class);

	public GameLanClientManager() {
		lanClient = new LanClient(this);
	}

	public void shutdown() {
		if (lanClient != null) {
			lanClient.close();
		}
	}

	public boolean connect(String host, int port) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap.get(host + StringConstants.SEPARATOR_DI + port);
		if (gsConnectionSession == null) {
			gsConnectionSession = new LanClientSession(lanClient);
			gsConnectionSessionMap.put(host + StringConstants.SEPARATOR_DI + port, gsConnectionSession);
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
				logger.error("lan connect to " + host + ":" + port + " error!", e);
			}
		}
		return ret;
	}

	@Override
	public void execute(Channel channel, RequestByteMessage msg) throws IOException {
		logger.info("reveive msgCode={}", msg.getMsgCode());
		switch (msg.getMsgCode()) {
		case 20004:
		case 20002:
			// GsSyncRequest.getInstance().doReceive(msg);
			break;
		case 30002:// 诸神跨服消息成功返回
			int playerId = msg.readInt();
			long sessionId = msg.readLong();
			PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
//			if (pr != null) {
//				pr.getGamePlayer().writeAndFlush(36058,
//						PushCraftMatchCrossRet.newBuilder().setRet(0).setSessionId(sessionId));
//			}
			break;
		case 30006:// 跨服BOSS战消息成功返回
			playerId = msg.readInt();
			sessionId = msg.readLong();
			int addRet = msg.readInt();
			pr = SessionManager.getInstance().getPlayer(playerId);
//			if (pr != null) {
//				pr.getGamePlayer().writeAndFlush(37004,
//						PushCrossBossStart.newBuilder().setRet(addRet).setSessionId(sessionId));
//			}
			break;
		case 30008:
			playerId = msg.readInt();
//			S2CCrossBossView.Builder retBuilder = S2CCrossBossView.newBuilder();
//			int progressSize = msg.getInt();
//			if (progressSize > 0) {
//				for (int i = 0; i < progressSize; i++) {
//					int roomTypeId = msg.getInt();
//					retBuilder.addRoomTypeId(roomTypeId).addStatus(1);
//				}
//			}
//			int finishSize = msg.getInt();
//			if (finishSize > 0) {
//				for (int i = 0; i < finishSize; i++) {
//					int roomTypeId = msg.getInt();
//					retBuilder.addRoomTypeId(roomTypeId).addStatus(2);
//				}
//			}
//			pr = SessionManager.getInstance().getPlayer(playerId);
//			if (pr != null) {
//				pr.getGamePlayer().writeAndFlush(37038, retBuilder);
//			}
			break;
		default:
			break;
		}
	}

	public void sendRegist2Bus(String hostName, int port, String garenaOpenId, int zoneId, int level) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(hostName + StringConstants.SEPARATOR_DI + port);
		SendRegist2BusMessage sendRegist2BusMessage = new SendRegist2BusMessage(gsConnectionSession.alloc());
		sendRegist2BusMessage.setMessage(garenaOpenId, zoneId, level);
		logger.info("send SendRegist2BusMessage,host={},garenaOpenId={},zoneId={},level={}", hostName, garenaOpenId,
				zoneId, level);
		gsConnectionSession.writeAndFlush(sendRegist2BusMessage);
	}

	public void sendLevelUp2Bus(String hostName, int port, String garenaOpenId, int zoneId, int level) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(hostName + StringConstants.SEPARATOR_DI + port);
		SendLevelup2BusMessage sendLevelup2BusMessage = new SendLevelup2BusMessage(gsConnectionSession.alloc());
		sendLevelup2BusMessage.setMessage(garenaOpenId, zoneId, level);
		logger.info("send SendLevelup2BusMessage,host={},garenaOpenId={},zoneId={},level={}", hostName, garenaOpenId,
				zoneId, level);
		gsConnectionSession.writeAndFlush(sendLevelup2BusMessage);
	}

//	public void sendCraftBegin2Cross(String hostName, int port, PlayerBean playerBean, CraftBean craftBean,
//			Map<Integer, HeroEntity> heroAll, long sessionId) {
//		LanClientSession gsConnectionSession = gsConnectionSessionMap
//				.get(hostName + StringConstants.SEPARATOR_DI + port);
//		SendToMessage sendToMessage = new SendToMessage(gsConnectionSession.alloc(), 30001);
//		// 序列化
//		int craftLevel = 0;
//		if (craftBean != null) {
//			craftLevel = craftBean.getLevel();
//		}
//		CrossCraftBegin crossCraftBeginMsg = new CrossCraftBegin(playerBean.getId(), playerBean.getServerId(),
//				playerBean.getName(), playerBean.getIcon(), playerBean.getHeadFrame(), craftLevel);
//		List<CrossHeroEntity> heroList = new ArrayList<>();
//		for (HeroEntity he : heroAll.values()) {
//			CrossHeroEntity crossHeroEntity = new CrossHeroEntity(he.getTemplateId(), he.getLevel(),
//					he.getEvolutionGrade(), he.getAdvanceGrade());
//			crossHeroEntity.setEquipPack(he.getEquimentPack());
//			crossHeroEntity.setSkillPack(he.getSkillPack());
//			crossHeroEntity.setRunePack(he.getRunePack());
//			heroList.add(crossHeroEntity);
//		}
//		crossCraftBeginMsg.setHeroEntityList(heroList);
//		// write
//		byte[] msgBytes = ProtostuffIOUtil.toByteArray(crossCraftBeginMsg, CrossCraftBegin.getSchema(),
//				Global.getProtoBuffer());
//		sendToMessage.writeBytes(msgBytes);
//		//
//		sendToMessage.writeLong(sessionId);
//		// send
//		gsConnectionSession.writeAndFlush(sendToMessage);
//	}

	public void sendCraftCancel2Cross(String crossHost, int crossPort, Integer playerId, Integer serverId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(crossHost + StringConstants.SEPARATOR_DI + crossPort);
		SendToByteMessage sendToByteMessage = new SendToByteMessage(gsConnectionSession.alloc(), 30003);
		sendToByteMessage.writeInt(playerId);
		sendToByteMessage.writeInt(serverId);
		gsConnectionSession.writeAndFlush(sendToByteMessage);
	}

//	public void sendCrossBossJoin2Cross(String crossHost, int crossPort, PlayerBean playerBean, HeroEntity he,
//			int roomId, long sessionId) {
//		LanClientSession gsConnectionSession = gsConnectionSessionMap
//				.get(crossHost + StringConstants.SEPARATOR_DI + crossPort);
//		SendToMessage sendToMessage = new SendToMessage(gsConnectionSession.alloc(), 30005);
//		// 序列化
//		CrossHeroEntity crossHeroEntity = new CrossHeroEntity(he.getTemplateId(), he.getLevel(), he.getEvolutionGrade(),
//				he.getAdvanceGrade());
//		crossHeroEntity.setEquipPack(he.getEquimentPack());
//		crossHeroEntity.setSkillPack(he.getSkillPack());
//		crossHeroEntity.setRunePack(he.getRunePack());
//		CrossBossBegin crossCraftBeginMsg = new CrossBossBegin(playerBean.getId(), playerBean.getServerId(),
//				playerBean.getName(), playerBean.getLevel(), playerBean.getIcon(), playerBean.getHeadFrame(),
//				crossHeroEntity, roomId);
//		// write
//		byte[] msgBytes = ProtostuffIOUtil.toByteArray(crossCraftBeginMsg, CrossBossBegin.getSchema(),
//				Global.getProtoBuffer());
//		sendToMessage.writeBytes(msgBytes);
//		//
//		sendToMessage.writeLong(sessionId);
//		// send
//		gsConnectionSession.writeAndFlush(sendToMessage);
//	}

	public void sendCrossBossView2Cross(String crossHost, int crossPort, int playerId, Integer serverId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(crossHost + StringConstants.SEPARATOR_DI + crossPort);
		SendToByteMessage sendToByteMessage = new SendToByteMessage(gsConnectionSession.alloc(), 30007);
		sendToByteMessage.writeInt(playerId);
		sendToByteMessage.writeInt(serverId);
		// send
		gsConnectionSession.writeAndFlush(sendToByteMessage);
	}

	public void sendPay2Ls(String loginHost, long loginPort, long randOrderId, int serverId, String openId,int productId) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(loginHost + StringConstants.SEPARATOR_DI + loginPort);
		SendToByteMessage sendToByteMessage = new SendToByteMessage(gsConnectionSession.alloc(), 11001);
		sendToByteMessage.writeLong(randOrderId);
		sendToByteMessage.writeInt(serverId);
		sendToByteMessage.writeString(openId);
		sendToByteMessage.writeInt(productId);
		// send
		gsConnectionSession.writeAndFlush(sendToByteMessage);
	}

	public void sendPlayerLevel2Ls(String loginHost, long loginPort, String openId, int serverId,int playerLevel) {
		LanClientSession gsConnectionSession = gsConnectionSessionMap
				.get(loginHost + StringConstants.SEPARATOR_DI + loginPort);
		SendToByteMessage sendToByteMessage = new SendToByteMessage(gsConnectionSession.alloc(), 11003);
		sendToByteMessage.writeString(openId);
		sendToByteMessage.writeInt(serverId);
		sendToByteMessage.writeInt(playerLevel);
		// send
		gsConnectionSession.writeAndFlush(sendToByteMessage);
	}

}