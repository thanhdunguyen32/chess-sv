package test.stat;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameServer;
import game.ServerConfig;
import game.common.StringConstants;
import game.db.DataSourceManager;
import game.module.item.bean.PlayerNovicePack;

public class PlayerGuideStat {

	private static Logger logger = LoggerFactory.getLogger(PlayerGuideStat.class);

	public static void main(String[] args) {
		ServerConfig serverConfig = loadConfig();
		DataSourceManager.init(serverConfig.getMysqlUrl(), serverConfig.getMysqlClassname(),
				serverConfig.getMysqlUsername(), serverConfig.getMysqlPassword());
		List<PlayerExtraBean> retList = PlayerExtraDao.getInstance().getAll();
		int finish107 = 0;
		int finish104 = 0;
		int finish103 = 0;
		int finish109 = 0;
		for (PlayerExtraBean playerExtraBean : retList) {
			PlayerNovicePack novicePack = playerExtraBean.getPlayerNovicePack();
			if (novicePack != null) {
				List<Integer> noviceList = novicePack.getFinishNoviceList();
				if (noviceList != null) {
					logger.info("noviceList={}", noviceList);
					if (noviceList.contains(107)) {
						finish107++;
					}
					if (noviceList.contains(104)) {
						finish104++;
					}
					if (noviceList.contains(103)) {
						finish103++;
					}
					if (noviceList.contains(109)) {
						finish109++;
					}
				}
			}
		}
		logger.info("finish,107={},104={},103={},109={}", finish107, finish104, finish103, finish109);
		//107=2143,104=1851,103=1587,109=1247
	}

	public static ServerConfig loadConfig() {
		logger.warn("load executer");
		InputStream propStream = null;
		Properties properties = new Properties();
		String serverConfStr = ServerConfig.CONFIG_FILE_NAME;
		try {
			propStream = GameServer.class.getClassLoader().getResourceAsStream(serverConfStr);
			if (propStream == null) {
				propStream = GameServer.class.getClassLoader().getResourceAsStream("/" + serverConfStr);
				properties.load(propStream);
			} else {
				properties.load(propStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setSocketPort(Integer.valueOf(properties.getProperty(ServerConfig.KEY_SOCKET_PORT)));
		serverConfig.setHttpPort(Integer.valueOf(properties.getProperty(ServerConfig.KEY_HTTP_PORT)));
		serverConfig.setGsLanPort(Integer.valueOf(properties.getProperty(ServerConfig.KEY_GS_LAN_PORT)));
		serverConfig.setZoneId(Integer.valueOf(properties.getProperty(ServerConfig.KEY_ZONE_ID)));
		serverConfig.setIsAndroid(Boolean.valueOf(properties.getProperty(ServerConfig.KEY_IS_ANDROID)));
		serverConfig.setMysqlUrl(properties.getProperty(ServerConfig.KEY_MYSQL_URL));
		serverConfig.setMysqlClassname(properties.getProperty(ServerConfig.KEY_MYSQL_CLASSNAME));
		serverConfig.setMysqlUsername(properties.getProperty(ServerConfig.KEY_MYSQL_USERNAME));
		serverConfig.setMysqlPassword(properties.getProperty(ServerConfig.KEY_MYSQL_PASSWORD));
		serverConfig.setCrossHost(properties.getProperty(ServerConfig.KEY_CROSS_HOST));
		serverConfig.setCrossLanPort(Integer.valueOf(properties.getProperty(ServerConfig.KEY_CROSS_LAN_PORT)));
		serverConfig.setGmEnable(Boolean.valueOf(properties.getProperty(ServerConfig.KEY_GM_ENABLE)));
		String lanAllowIps = properties.getProperty(ServerConfig.KEY_LAN_ALLOW_IP);
		String[] allowIps = StringUtils.split(lanAllowIps, StringConstants.SEPARATOR_DI);
		Set<String> allowIpSet = new HashSet<>();
		for (String aIp : allowIps) {
			allowIpSet.add(aIp);
		}
		serverConfig.setLanAllowIps(allowIpSet);
		return serverConfig;
	}

}
