package game.module.lan.logic;

import com.google.common.collect.EvictingQueue;
import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity.bean.Activity4Gm;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.*;
import game.module.activity.logic.ActivityManager;
import game.module.battle.bean.DBCraftRecordItem;
import game.module.config.logic.ConfigJsonManager;
import game.module.gm.logic.GmManager;
import game.module.lan.bean.LoginSessionBean;
import game.module.lan.bean.QqLoginSessionBean;
import game.module.lan.dao.LoginSessionCache;
import game.module.lan.msg.*;
import game.module.log.bean.LogItemGo;
import game.module.log.dao.LogItemGoDao;
import game.module.mail.logic.MailManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.offline.logic.ServerCache;
import game.module.pay.bean.LogPayEntity;
import game.module.pay.bean.TopPayPlayerBean;
import game.module.pay.dao.PaymentLogDao;
import game.module.pay.logic.ChargeTemplateCache;
import game.module.pay.logic.PaymentManager;
import game.module.server.bean.ServerInfoBean;
import game.module.server.dao.ServerInfoDao;
import game.module.stat.logic.StatManager;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import io.netty.channel.Channel;
import io.protostuff.ProtostuffIOUtil;
import lion.lan.ILanIoExecutor;
import lion.lan.LanServer;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.SendToByteMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageChat.S2CChatPush;
import ws.WsMessageHall.S2CPlayerKickOff;

import java.io.IOException;
import java.util.*;

public class GameLanServerManager implements ILanIoExecutor {

	private static Logger logger = LoggerFactory.getLogger(GameLanServerManager.class);

	private LanServer lanServer;

	static class SingletonHolder {
		static GameLanServerManager instance = new GameLanServerManager();
	}

	public static GameLanServerManager getInstance() {
		return SingletonHolder.instance;
	}

	private GameLanServerManager() {
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
	public void execute(final Channel channel, RequestByteMessage requestMessage) throws IOException {
		// ip判断
		Set<String> allowIpSet = GameServer.getInstance().getServerConfig().getLanAllowIps();
		if (channel.remoteAddress() != null) {
			String ipStr = channel.remoteAddress().toString();
			String aIp = StringUtils.split(ipStr, ":")[0].substring(1);
			if (!allowIpSet.contains(aIp)) {
				logger.warn("not valid ip lan request,ipAddr={}", ipStr);
				return;
			}
		}
		switch (requestMessage.getMsgCode()) {
            case 10009: // Kiểm tra đăng nhập
        // Nhận: sessionId, openId, serverId
        // Xử lý: 
        // 1. Lưu thông tin session vào LoginSessionCache
        // 2. Kiểm tra người chơi có online không
        // 3. Nếu không online thì kiểm tra tài khoản có tồn tại trong DB không
        // Trả về: Kết quả kiểm tra tài khoản (true/false)
			long sessionId = requestMessage.readLong();
			String openId = requestMessage.readString();
			int serverId = requestMessage.readInt();
			logger.info("receive lan login msg,sid={},openId={},serverId={}", sessionId, openId, serverId);
			LoginSessionCache.getInstance()
					.addLoginSession(new LoginSessionBean(sessionId, openId, System.currentTimeMillis(), serverId));
			PlayerBean playerBean = PlayerOnlineCacheMng.getInstance().getCache(openId, serverId);
			if (playerBean != null) {
				SendToByteMessage responseMsg = new SendToByteMessage(channel.alloc(), 10010);
				responseMsg.writeString(openId);
				responseMsg.writeBoolean(true);
				channel.writeAndFlush(responseMsg);
			} else {
				GameServer.executorService.execute(() -> {
					SendToByteMessage responseMsg = new SendToByteMessage(channel.alloc(), 10010);
					responseMsg.writeString(openId);
					boolean isPlayerExist = PlayerDao.getInstance().checkPlayerExist(openId, serverId);
					responseMsg.writeBoolean(isPlayerExist);
					channel.writeAndFlush(responseMsg);
				});
			}
			break;
        case 10011: // Gửi mail hệ thống
         // Nhận: tiêu đề mail, nội dung mail, danh sách phần thưởng (itemId, số lượng)
        // Xử lý: Gửi mail cho toàn bộ người chơi trong server
        // Lưu ý: Mail sẽ được gửi ngay lập tức
			String mailTitle = requestMessage.readString();
			String mailContent = requestMessage.readString();
			Map<Integer, Integer> mailAtts = new HashMap<>();
			int attachCount = requestMessage.readInt();
			while (attachCount > 0) {
				int gsid = requestMessage.readInt();
				int count = requestMessage.readInt();
				mailAtts.put(gsid, count);
				attachCount--;
			}
			MailManager.getInstance().sendSysMailToAll(mailTitle, mailContent, mailAtts);
			break;
		case 10013: // Gửi ZoneId cho GM
            SendZoneId2GmMessage msg = new SendZoneId2GmMessage(channel.alloc());
            // Xử lý: Gửi thông tin ZoneId=1 về GM tool
        // Mục đích: Để GM tool biết đang kết nối với zone nào
			msg.setZoneId(1);
			channel.writeAndFlush(msg);
			break;
		case 10015: // Đăng nhập Garena (đã comment)
			break;
        case 10017: // Lấy danh sách hoạt động
           // Xử lý: Lấy toàn bộ hoạt động từ ActivityManager
        // Trả về cho mỗi hoạt động:
        // - Trạng thái (0: đóng, 1: mở)
        // - Loại hoạt động
        // - Thời gian bắt đầu
        // - Thời gian kết thúc  
        // - Tiêu đề
			List<Activity4Gm> activityAll = ActivityManager.getInstance().getActivityAllBase();
			SendActivityList2GmMessage retMsg = new SendActivityList2GmMessage(channel.alloc());
			retMsg.writeInt(activityAll.size());
			for (Activity4Gm aBean : activityAll) {
				retMsg.writeInt(aBean.getIsOpen());
				retMsg.writeInt(aBean.getType());
				retMsg.writeLong(aBean.getStartTime() != null ? aBean.getStartTime().getTime() : 0);
				retMsg.writeLong(aBean.getEndTime() != null ? aBean.getEndTime().getTime() : 0);
				retMsg.writeString(aBean.getTitle());
			}
			channel.writeAndFlush(retMsg);
			break;
        case 10019: // Lấy chi tiết một hoạt động
          // Nhận: activityId
        // Xử lý: Lấy thông tin chi tiết của hoạt động từ ActivityManager
        // Trả về: Toàn bộ thông tin của hoạt động bao gồm params
        // Nếu không tìm thấy trả về 0
			int activityId = requestMessage.readInt();
			Activity4Gm activity4Gm = ActivityManager.getInstance().getActivityById(activityId);
			SendActivityOne2GmMessage activityOneMsg = new SendActivityOne2GmMessage(channel.alloc());
			if (activity4Gm != null) {
				activityOneMsg.writeInt(1);
				activityOneMsg.setVal(activity4Gm.getType(), activity4Gm.getStartTime(), activity4Gm.getEndTime(),
						activity4Gm.getIsOpen(), activity4Gm.getTitle(), activity4Gm.getDescription(),
						activity4Gm.getParams());
			} else {
				activityOneMsg.writeInt(0);
			}
			channel.writeAndFlush(activityOneMsg);
			break;
        case 10021: // Cập nhật hoạt động
         // Nhận: Toàn bộ thông tin hoạt động cần update
        // Xử lý: 
        // 1. Tạo activity4Gm từ dữ liệu nhận được
        // 2. Gọi ActivityManager để update
        // Trả về: Message xác nhận đã update
			activity4Gm = new Activity4Gm();
			activity4Gm.setType(requestMessage.readInt());
			activity4Gm.setStartTime(new Date(requestMessage.readLong()));
			activity4Gm.setEndTime(new Date(requestMessage.readLong()));
			activity4Gm.setIsOpen(requestMessage.readInt());
			activity4Gm.setTitle(requestMessage.readString());
			activity4Gm.setDescription(requestMessage.readString());
			byte[] paramsBytes = requestMessage.readByteArray();
			activity4Gm.setParams(paramsBytes);
			logger.info("gm updateActivity,type={}", activity4Gm.getType());
			ActivityManager.getInstance().updateActivity(activity4Gm);
			channel.writeAndFlush(new SendToByteMessage(channel.alloc(), 10022));
			break;
        case 10023: // Danh sách tài khoản bị cấm
         // Xử lý: Lấy danh sách tài khoản bị cấm từ ServerCache
        // Trả về: Map<playerId, thời gian hết hạn>
			Map<Integer, Long> banNames = ServerCache.getInstance().getFenghaoAll();
			SendBanNames2GmMessage banNames2GmMessage = new SendBanNames2GmMessage(channel.alloc());
			banNames2GmMessage.setData(banNames);
			channel.writeAndFlush(banNames2GmMessage);
			break;
        case 10025: // Cấm tài khoản
          // Nhận: playerId và thời gian cấm
        // Xử lý:
        // 1. Thêm vào danh sách cấm
        // 2. Nếu người chơi đang online thì kick
        // 3. Xóa session của người chơi
			Integer playerId = requestMessage.readInt();
			long endTimeMili = requestMessage.readLong();
			ServerCache.getInstance().addFengHao(playerId, endTimeMili);
			// 踢掉玩家
			PlayingRole targetPr = SessionManager.getInstance().getPlayer(playerId);
			if (targetPr != null) {
				S2CPlayerKickOff pushMsg = new ws.WsMessageHall.S2CPlayerKickOff();
				targetPr.getGamePlayer().writeAndFlush(pushMsg.build(targetPr.alloc()));
				sessionId = targetPr.getGamePlayer().getSessionId();
				SessionManager.getInstance().removeLogicPlayer(sessionId, true);
				targetPr.getGamePlayer().saveSessionId(null);
				targetPr.getGamePlayer().close();
			}
			channel.writeAndFlush(new SendToByteMessage(channel.alloc(), 10026));
			break;
        case 10027: // Gỡ cấm tài khoản
        // Nhận: playerId
        // Xử lý: Xóa khỏi danh sách tài khoản bị cấm
			playerId = requestMessage.readInt();
			ServerCache.getInstance().removeFengHao(playerId);
			channel.writeAndFlush(new SendToByteMessage(channel.alloc(), 10028));
			break;
        case 10029: // Cấm chat
         // Nhận: playerId và thời gian cấm chat
        // Xử lý:
        // 1. Thêm vào danh sách cấm chat
        // 2. Xóa lịch sử chat công cộng của người chơi
			playerId = requestMessage.readInt();
			endTimeMili = requestMessage.readLong();
			ServerCache.getInstance().addJinYan(playerId, endTimeMili);
			ServerCache.getInstance().removeChatPublic(playerId);
			break;
        case 10031: // Gỡ cấm chat
         // Nhận: playerId
        // Xử lý: Xóa khỏi danh sách cấm chat
			playerId = requestMessage.readInt();
			ServerCache.getInstance().cancelJinYan(playerId);
			break;
        case 10033: // Gửi mail có đính kèm
          // Nhận: 
        // - Loại người nhận (byte)
        // - ID người nhận 
        // - Người gửi
        // - Tiêu đề, nội dung
        // - Phần thưởng đính kèm
        // - Thời hạn mail
			byte addressee = requestMessage.readByte();
			String receiveId = requestMessage.readString();
			String sender = requestMessage.readString();
			String title = requestMessage.readString();
			String content = requestMessage.readString();
			String attach = requestMessage.readString();
			int validity = requestMessage.readInt();

			MailManager.getInstance().sendGMMail(addressee, receiveId, sender, title, content, attach, validity);
			break;
        case 10035: // Kick người chơi
         // Nhận: playerId
        // Xử lý:
        // 1. Tìm người chơi trong SessionManager
        // 2. Gửi message kick
        // 3. Đóng kết nối
        // 4. Xóa session
			playerId = requestMessage.readInt();
			// 踢掉玩家
			targetPr = SessionManager.getInstance().getPlayer(playerId);
			if (targetPr != null) {
				S2CPlayerKickOff pushMsg = new ws.WsMessageHall.S2CPlayerKickOff();
				targetPr.getGamePlayer().writeAndFlush(pushMsg.build(targetPr.alloc()));
				sessionId = targetPr.getGamePlayer().getSessionId();
				SessionManager.getInstance().removeLogicPlayer(sessionId, false);
				targetPr.getGamePlayer().saveSessionId(null);
				targetPr.getGamePlayer().close();
			}
			break;
        case 10037: // Thông báo chạy chữ
         // Nhận: Nội dung và số lần lặp
        // Xử lý: Gửi thông báo marquee tới tất cả người chơi
			logger.info("=== RECEIVED SUBTITLE REQUEST ===");
			logger.info("From: {}", channel.remoteAddress());
			String subtitleContent = requestMessage.readString();
			int repeateCount = requestMessage.readInt();
			logger.info("Content: {}, RepeatCount: {}", subtitleContent, repeateCount);
			GmManager.getInstance().sendMarquee(subtitleContent, repeateCount);
			logger.info("=== END SUBTITLE REQUEST ===");
			break;
        case 10049: // Xóa dữ liệu hoạt động
          // Nhận: Loại hoạt động
        // Xử lý: Reset toàn bộ dữ liệu của hoạt động đó
        // Bao gồm: Cache và database
			final int activityType = requestMessage.readInt();
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("reset activity,type={}", activityType);
					if (activityType == ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO) {
						ActivityQZYBCache.getInstance().resetQZYB();
					} else if (activityType == ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG) {
						ActivityChongZhiBangCache.getInstance().resetRank();
					} else if (activityType == ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG) {
						ActivityXiaoFeiBangBangCache.getInstance().resetRank();
					} else {
						// 移除缓存
						ActivityPlayerCache.getInstance().removePlayerActivityType(activityType);
						// 移除数据库
						ActivityPlayerDao.getInstance().removePlayerActivityType(activityType);
					}
					channel.writeAndFlush(new SendToByteMessage(channel.alloc(), 10050));
				}
			});
			break;
        case 10051: // Lấy thông tin thống kê server
          // Xử lý: Lấy các thông số thống kê từ StatManager
        // Trả về: Mảng các thông số thống kê
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("gm get Main Stat Params!");
					int[] statInfo = StatManager.getMainStatParams();
					logger.info("server stat:{}",statInfo);
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10052);
					for (int aval : statInfo) {
						send2Msg.writeInt(aval);
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
        case 10053: // Xếp hạng nạp tiền
         // Xử lý: Lấy danh sách top nạp từ StatManager
        // Trả về cho mỗi người chơi:
        // - ID, tên, account_id
        // - Tổng số tiền đã nạp
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("top pay players!");
					List<TopPayPlayerBean> statInfo = StatManager.getTopPayPlayerBeans();
					logger.info("player size:{}",statInfo.size());
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10054);
					send2Msg.writeInt(statInfo.size());
					for (TopPayPlayerBean aval : statInfo) {
						send2Msg.writeInt(aval.getPlayer_id());
						String realPlayerName = getRealPlayerName(aval.getPlayer_id());
						send2Msg.writeString(realPlayerName != null ? realPlayerName : aval.getName());
						send2Msg.writeString(aval.getAccount_id());
						send2Msg.writeInt(aval.getSumMoney());
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
        case 10055: // Nội dung chat
        // Xử lý: 
        // 1. Lấy lịch sử chat công cộng
        // 2. Lấy lịch sử chat riêng tư
        // 3. Lấy lịch sử chat bang hội
        // Trả về: Toàn bộ nội dung chat kèm thông tin người gửi
			logger.warn("query chat content!");
			SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10056);
			Collection<S2CChatPush> chatPublicAll = ServerCache.getInstance().getChatPublicAll();
			Map<Integer, EvictingQueue<S2CChatPush>> chatPrivateAll = ServerCache.getInstance().getChatPrivateAll();
			if (chatPublicAll.size() + chatPrivateAll.size() == 0) {
				send2Msg.writeInt(0);
			} else {
				int privateChatSize = 0;
				for (Map.Entry<Integer, EvictingQueue<S2CChatPush>> privateChatItem : chatPrivateAll.entrySet()) {
					EvictingQueue<S2CChatPush> chatItem = privateChatItem.getValue();
					if (chatItem != null) {
						privateChatSize += chatItem.size();
					}
				}
				int nationChatSize = 0;
				Map<Integer, EvictingQueue<S2CChatPush>> chatNationAll = ServerCache.getInstance().getChatNationAll();
				for(EvictingQueue<S2CChatPush> chatlist : chatNationAll.values()) {
					nationChatSize += chatlist.size();
				}
				send2Msg.writeInt(chatPublicAll.size() + privateChatSize + nationChatSize);
				for (S2CChatPush s2cChatPush : chatPublicAll) {
					send2Msg.writeInt(s2cChatPush.rid);
					send2Msg.writeString(s2cChatPush.rname);
					send2Msg.writeString(s2cChatPush.content);
					send2Msg.writeLong(s2cChatPush.send_time);
				}
				for (Map.Entry<Integer, EvictingQueue<S2CChatPush>> privateChatItem : chatPrivateAll.entrySet()) {
					EvictingQueue<S2CChatPush> chatItem = privateChatItem.getValue();
					int receiverId = privateChatItem.getKey();
					String receiverName = "";
					PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
							.getPlayerOfflineCache(receiverId);
					if (playerOfflineCache != null) {
						receiverName = playerOfflineCache.getName();
					}
					String contentSuffix = "(" + receiverName + "#" + receiverId + ")";
					if (chatItem != null) {
						for (S2CChatPush s2cChatPush : chatItem) {
							send2Msg.writeInt(s2cChatPush.rid);
							send2Msg.writeString(s2cChatPush.rname);
							send2Msg.writeString(s2cChatPush.content + contentSuffix);
							send2Msg.writeLong(s2cChatPush.send_time);
						}
					}
				}
//				for (Map.Entry<Integer, EvictingQueue<S2CChatPush>> nationChatItem : chatNationAll.entrySet()) {
//					int nationId = nationChatItem.getKey();
//					String contentSuffix = "(" + GuozhanConstants.NATION_LIST[nationId - 1] + ")";
//					for (S2CChatPush s2cChatPush : nationChatItem.getValue()) {
//						send2Msg.writeInt(s2cChatPush.sender_uid);
//						send2Msg.writeString(s2cChatPush.sender_name);
//						send2Msg.writeString(s2cChatPush.content + contentSuffix);
//						send2Msg.writeInt(s2cChatPush.timestamp);
//					}
//				}
			}
			channel.writeAndFlush(send2Msg);
			break;
        case 10057: // Xếp hạng nạp tiền trong ngày
          // Nhận: Ngày cần lấy
        // Xử lý: Lấy danh sách top nạp theo ngày từ PaymentLogDao
        // Trả về: Toàn bộ danh sách top nạp
			final String queryDate = requestMessage.readString();
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("daily top pay players!");
					List<TopPayPlayerBean> statInfo = PaymentLogDao.getInstance().getTopupByDate(queryDate);
					logger.info("player size:{}",statInfo.size());
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10058);
					send2Msg.writeInt(statInfo.size());
					for (TopPayPlayerBean aval : statInfo) {
						send2Msg.writeInt(aval.getPlayer_id());
						String realPlayerName = getRealPlayerName(aval.getPlayer_id());
						send2Msg.writeString(realPlayerName != null ? realPlayerName : aval.getName());
						send2Msg.writeString(aval.getAccount_id());
						send2Msg.writeInt(aval.getSumMoney());
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
        case 10059: // Xếp hạng nạp tiền
            // Xử lý: Lấy danh sách top nạp từ PaymentLogDao
            // Trả về cho mỗi người chơi:
            // - ID, tên, account_id
            // - Tổng số tiền đã nạp
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("pay all logs!");
					List<LogPayEntity> statInfo = PaymentLogDao.getInstance().getLogTopupAll();
					logger.info("player size:{}",statInfo.size());
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10060);
					send2Msg.writeInt(statInfo.size());
					for (LogPayEntity aval : statInfo) {
						send2Msg.writeInt(aval.getPlayerId());
						String realPlayerName = getRealPlayerName(aval.getPlayerId());
						send2Msg.writeString(realPlayerName != null ? realPlayerName : aval.getPlayerName());
						send2Msg.writeString(aval.getOrderId());
						send2Msg.writeInt(aval.getPaySum());
						send2Msg.writeLong(aval.getPayTime().getTime());
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
        case 10061: // Xếp hạng nạp tiền
            // Nhận: itemId và playerId
            // Xử lý: Lấy lịch sử sử dụng item từ LogItemGoDao
            // Trả về: Toàn bộ lịch sử sử dụng item
			final int itemId = requestMessage.readInt();
			playerId = requestMessage.readInt();
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("item go log query!itemId={},playerId={}",itemId,playerId);
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10062);
					if(itemId == 0 && playerId == 0) {
						send2Msg.writeInt(0);
						channel.writeAndFlush(send2Msg);
						return;
					}
					List<LogItemGo> statInfo = LogItemGoDao.getInstance().queryLog(itemId, playerId);
					logger.info("log size:{}",statInfo.size());
					//限制消息数量
					send2Msg.writeInt(statInfo.size());
					for (LogItemGo aval : statInfo) {
						send2Msg.writeInt(aval.getPlayerId());
						send2Msg.writeString(aval.getPlayerName());
						send2Msg.writeInt(aval.getModuleType());
						send2Msg.writeInt(aval.getItemId());
						send2Msg.writeInt(aval.getChangeValue());
						send2Msg.writeLong(aval.getCreateTime().getTime());
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
        case 10063: // Lấy danh sách tất cả người chơi
            // Xử lý: Lấy danh sách tất cả người chơi từ PlayerOfflineManager
            // Trả về: Toàn bộ danh sách người chơi
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					logger.warn("gm query all players!");
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 10064);
					Collection<PlayerBaseBean> cacheAll = PlayerOfflineManager.getInstance().getAll();
					send2Msg.writeInt(cacheAll.size());
					for (PlayerBaseBean poc : cacheAll) {
						int playerId = poc.getId();
						PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
						if(pr != null) {
							send2Msg.writeInt(playerId);
							send2Msg.writeString(pr.getPlayerBean().getName());
							send2Msg.writeInt(pr.getPlayerBean().getLevel());
							send2Msg.writeInt(pr.getPlayerBean().getVipLevel());
							send2Msg.writeInt(pr.getPlayerBean().getPower());
							send2Msg.writeInt(pr.getPlayerBean().getMoney());
							send2Msg.writeInt(pr.getPlayerBean().getGold());
						}else {
							send2Msg.writeInt(playerId);
							send2Msg.writeString(poc.getName());
							send2Msg.writeInt(poc.getLevel());
							send2Msg.writeInt(poc.getVipLevel());
							send2Msg.writeInt(poc.getPower());
							send2Msg.writeInt(0);
							send2Msg.writeInt(0);
						}
					}
					channel.writeAndFlush(send2Msg);
				}
			});
			break;
		case 10067: // Tạo đơn hàng giả
            // Nhận: playerId, số tiền nạp, ID sản phẩm
            // Xử lý: Tạo đơn hàng giả cho người chơi
			playerId = requestMessage.readInt();
			int moneyYuan = requestMessage.readInt();
			String productId = requestMessage.readString();
			PlayingRole pr = PlayerOnlineCacheMng.getInstance().getOnlinePlayerById(playerId);
			if (pr != null) {
				PaymentManager.getInstance().fakePay(pr, moneyYuan, productId);
			} else {
				logger.warn("fakepay player not online!player={},rmb={}", playerId, moneyYuan);
			}
			break;
		case 11001: // Thanh toán
            // Nhận: userId, orderId, số tiền, thời gian, PID, serverId
            // Xử lý: Xử lý thanh toán
			String userId = requestMessage.readString();
			String orderid = requestMessage.readString();
			int money = requestMessage.readInt();
			int time = requestMessage.readInt();
			String pid = requestMessage.readString(); // prodyct123
            int serverid = requestMessage.readInt();
            
            GameServer.executorService.execute(new Runnable() {
                public void run() {
                    SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 11002);
                    try {
                        PaymentManager.getInstance().payCallback(channel, userId, orderid, money, time, pid, serverid);
                        send2Msg.writeString("success");
                    } catch (Exception e) {
                        send2Msg.writeString("fail - " + e.getMessage());
                    }
                    channel.writeAndFlush(send2Msg);
                }
            });
			break;
		case 11003: // Sifuba login
            // Nhận: sifubaUserId
            // Xử lý: Tìm người chơi trong cache và gửi thông tin người chơi
			final String sifubaUserId = requestMessage.readString();
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(sifubaUserId, 0);
					int playerId = 0;
					String playerName = "";
					if (pb != null) {
						playerId = pb.getId();
						playerName = pb.getName();
					} else {
						List<PlayerBean> pbs = PlayerDao.getInstance().getPlayerByOpenId(sifubaUserId, 0);
						if (pbs != null && pbs.size() > 0) {
							pb = pbs.get(0);
							playerId = pb.getId();
							playerName = pb.getName();
						}
					}
					logger.info("sifuba query role,sifubaUserId={},playerId={},playerName={}", sifubaUserId, playerId,
							playerName);
					SendRole2LoginMessage sendRole2LoginMessage = new SendRole2LoginMessage(channel.alloc());
					sendRole2LoginMessage.setData(playerId, playerName);
					channel.writeAndFlush(sendRole2LoginMessage);
				}
			});
			break;
		case 11005: // 联运登录
//			long sifubaSessionId = requestMessage.getLong();
//			String sifubaUserId1 = requestMessage.getString();
//			
//			serverId = requestMessage.getInt();
//			LoginSessionCache.getInstance().addLoginSession(
//					new LianYunLoginSessionBean(sifubaSessionId, sifubaUserId1, System.currentTimeMillis(), serverId));
			break;
		case 11007: // qq登录
			long qqSessionId = requestMessage.readLong();
			String qqOpenId = requestMessage.readString();
			serverId = requestMessage.readInt();
			LoginSessionCache.getInstance().addLoginSession(
					new QqLoginSessionBean(qqSessionId, qqOpenId, System.currentTimeMillis(), serverId));
			break;
		case 11009: //185sy rh SDK返利
			userId = requestMessage.readString();
			orderid = requestMessage.readString();
			money = requestMessage.readInt();
			time = requestMessage.readInt();
			int isVip = requestMessage.readInt();
			serverid = requestMessage.readInt();
			PaymentManager.getInstance().fanliCallback(channel, userId, orderid, money, time, isVip, serverid);
            break;
        case 11011:
            // read all file json recharge
            GameServer.executorService.execute(new Runnable() {
                public void run() {
                    SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 11012);
                    send2Msg.writeString(ConfigJsonManager.getInstance().loadConfigJsons(new String[]{"dbRecharge"}));
                    // String temp = ChargeTemplateCache.getInstance().getTemplateMapJsonString();
                    // logger.info("temp={}", temp);
                    // send2Msg.writeString(temp);
                    channel.writeAndFlush(send2Msg);
                }
            });
            break;
        case 11013:
            // get user info
			int serverID = requestMessage.readInt();
			String username = requestMessage.readString();
			logger.info("serverID={},username={}", serverID, username);	
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					SendToByteMessage send2Msg = new SendToByteMessage(channel.alloc(), 11014);
					List<PlayerBean> pbs = PlayerDao.getInstance().getPlayerByUsernameAndServerID(username, serverID);
					if (pbs.size() > 0) {
						send2Msg.writeString(pbs.get(0).toJsonString());
					} else {
						send2Msg.writeString("");
					}
                    channel.writeAndFlush(send2Msg);
				}
			});
			break;
		case 30007:
			// playerId = requestMessage.getInt();
			// boolean isPunish = requestMessage.getBool();
			// CraftManager.getInstance().gsCancelRoom(playerId, isPunish);
			break;
         case 10071: // Lấy số user (offline) và online
                int totalUserCount = PlayerOfflineManager.getInstance().getAll().size();
                int onlineUserCount = SessionManager.getInstance().getOnlineCount();
                // Tạo 1 gói trả về
                SendToByteMessage retMsgUser = new SendToByteMessage(channel.alloc(), 10071);
                retMsgUser.writeInt(totalUserCount);
                retMsgUser.writeInt(onlineUserCount);
                // Gửi gói trả về GM Tool
                channel.writeAndFlush(retMsgUser);
                break;
		case 10073:
			// Khi nhận request code=10073 => Lấy danh sách server_info rồi trả về code=10074
			GameServer.executorService.execute(() -> {
				// 1) Lấy danh sách server_info từ DB
				List<ServerInfoBean> serverList = ServerInfoDao.getInstance().getAllServers();
				// 2) Tạo message code=10074 để hồi đáp
				SendToByteMessage resp = new SendToByteMessage(channel.alloc(), 10074);
				// 3) Ghi số lượng server
				resp.writeInt(serverList.size());
				// 4) Ghi từng trường
				for(ServerInfoBean s : serverList) {
					resp.writeInt(s.getId() != null ? s.getId() : 0);
					resp.writeString(s.getName() != null ? s.getName() : "");
					resp.writeString(s.getIp() != null ? s.getIp() : "");
					resp.writeInt(s.getPort() != null ? s.getPort() : 0);
					resp.writeInt(s.getPortSsl() != null ? s.getPortSsl() : 0);
					resp.writeInt(s.getStatus() != null ? s.getStatus() : 0);
					resp.writeInt(s.getLanPort() != null ? s.getLanPort() : 0);
					resp.writeString(s.getHttpUrl() != null ? s.getHttpUrl() : "");
					// created_at => chuyển thành long
					long createdTime = (s.getTime_open() != null ? s.getTime_open().getTime() : 0L);
					resp.writeLong(createdTime);
				}
				logger.info("=== [case 10073] serverList.size={} => sending code=10074 ===", serverList.size());
				// 5) Gửi về client
				channel.writeAndFlush(resp);
			});
			break;
			
			default:
			break;
		}
	}

	public static void main(String[] args) {
		int[] a1 = new int[] {1,2,3};
		logger.info("a1={}",a1);
	}

	private String getRealPlayerName(int playerId){
		PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playerId);
		if(poc != null){
			return poc.getName();
		}
		return null;
	}

}