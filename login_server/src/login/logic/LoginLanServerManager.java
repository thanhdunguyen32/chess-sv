package login.logic;

import com.google.common.io.Files;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import lion.lan.ILanIoExecutor;
import lion.lan.LanServer;
import lion.netty4.message.RequestByteMessage;
import login.LoginServer;
import login.bean.AnnounceBean;
import login.bean.ServerList4Db;
import login.dao.ServerListDao;
import login.lan.SendAnnounce2GmMessage;
import login.lan.SendServerStatus2GmMessage;
import login.processor_http.HttpFileHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LoginLanServerManager implements ILanIoExecutor {

	private static Logger logger = LoggerFactory.getLogger(LoginLanServerManager.class);

	private LanServer lanServer;

	static class SingletonHolder {
		static LoginLanServerManager instance = new LoginLanServerManager();
	}

	public static LoginLanServerManager getInstance() {
		return SingletonHolder.instance;
	}

	private LoginLanServerManager() {
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
		Set<String> allowIpSet = LoginServer.getInstance().getLoginServerConfig().getLanAllowIps();
		if (channel.remoteAddress() != null) {
			String ipStr = channel.remoteAddress().toString();
			String aIp = StringUtils.split(ipStr, ":")[0].substring(1);
			if (!allowIpSet.contains(aIp)) {
				logger.warn("not valid ip lan request,ipAddr={}", ipStr);
				return;
			}
		}
		switch (requestMessage.getMsgCode()) {
		case 10039:// 请求公告内容
			SendAnnounce2GmMessage activityOneMsg = new SendAnnounce2GmMessage(channel.alloc());
			String noticeStr;
			List<AnnounceBean> list = AnnounceManager.getInstance().getAnnounceList();
			if (list.isEmpty()) {
				noticeStr = LoginServer.getInstance().getLoginServerConfig().getAnnouncement();
			} else {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (AnnounceBean announceBean : list) {
					if (i > 0) {
						sb.append("\n*/*\n");
					}
					sb.append(announceBean.getContent());
					i++;
				}
				noticeStr = sb.toString();
			}
			activityOneMsg.setVal(noticeStr);
			channel.writeAndFlush(activityOneMsg);
			break;
		case 10041:// 编辑公告
			String announcementContent = requestMessage.readString();
			LoginServer.executorService.execute(() -> {
				String[] announcelist = announcementContent.split("\\*/\\*");
				for (int idx = 0; idx < announcelist.length; idx++) {
					if(idx >=4){
						break;
					}
					String aanno = announcelist[idx];
					aanno = aanno.trim();
					AnnounceManager.getInstance().updateAnnounce(idx + 1, aanno);
				}
			});
			break;
		case 10043:// 查看服务器状态
			int serverId = requestMessage.readInt();
			ServerList4Db serverList4Db = ServerListManager.getInstance().getServer(serverId);
			int serverStatus = -1;
			if (serverList4Db != null) {
				serverStatus = serverList4Db.getStatus();
			}
			SendServerStatus2GmMessage sendMsg = new SendServerStatus2GmMessage(channel.alloc());
			sendMsg.setVal(serverStatus);
			channel.writeAndFlush(sendMsg);
			break;
		case 10045:// 设置服务器状态
			serverId = requestMessage.readInt();
			int status = requestMessage.readInt();
			serverList4Db = ServerListManager.getInstance().getServer(serverId);
			if (serverList4Db != null) {
				serverList4Db.setStatus(status);
			}
			ServerListDao.getInstance().updateServerStatus(serverId, status);
			break;
		case 11001:
			long randOrderId = requestMessage.readLong();
			serverId = requestMessage.readInt();
			String openId = requestMessage.readString();
			int productId = requestMessage.readInt();
			logger.info("xiao7 add pay info,randOrderId={},serverId={},openId={},productId={}", randOrderId, serverId,
					openId, productId);
			Xiao7PayListManager.getInstance().addPayInfo(randOrderId, openId, serverId, productId);
			break;
		case 11003://更新有角色的服务器信息
			String openAccountId = requestMessage.readString();
			serverId = requestMessage.readInt();
			int roleLevel = requestMessage.readInt();
			logger.info("game server add ServerHasRole info,server={},account={},level={}", serverId, openAccountId, roleLevel);
			ServerHasRoleManager.getInstance().addHasRoleServer(openAccountId,serverId,roleLevel);
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
		String announcementContent = "到了放个屁  \n  */* ggd士大夫色调分离\n电饭锅  sdls \n sdlg\n  */* sflsa0930";
		String[] split = announcementContent.split("\\*/\\*");
		logger.info("ret={}", Arrays.toString(split));
	}

}
