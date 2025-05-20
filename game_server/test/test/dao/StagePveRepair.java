package test.dao;

import java.io.InputStream;
import java.util.ArrayList;
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
import game.module.battle.bean.DBChapterRecord;
import game.module.battle.bean.DBChapterRecordOld;
import game.module.battle.bean.DBPlayerStageRecord;
import game.module.battle.bean.DBPlayerStageRecordOld;
import game.module.map.bean.StagePve;
import game.module.map.dao.StagePveDao;

public class StagePveRepair {

	private static Logger logger = LoggerFactory.getLogger(StagePveRepair.class);

	public static void main(String[] args) {
		ServerConfig serverConfig = loadConfig();
		DataSourceManager.init(serverConfig.getMysqlUrl(), serverConfig.getMysqlClassname(),
				serverConfig.getMysqlUsername(), serverConfig.getMysqlPassword());
		List<StagePve> stagePves = StagePveDao.getInstance().getAllStageRecord();
		int currentTimeSec = (int) (System.currentTimeMillis() / 1000);
		for (StagePve stagePve : stagePves) {
			// normal
			DBPlayerStageRecordOld dbPlayerStageRecordOld = stagePve.getStageRecordDbOld();
			if (dbPlayerStageRecordOld != null) {
				DBPlayerStageRecord dbPlayerStageRecord = new DBPlayerStageRecord();
				stagePve.setStageRecordDb(dbPlayerStageRecord);
				List<DBChapterRecordOld> chapterRecordOlds = dbPlayerStageRecordOld.getChapterListList();
				List<DBChapterRecord> chapterRecords = new ArrayList<>();
				dbPlayerStageRecord.setChapterListList(chapterRecords);
				for (DBChapterRecordOld dbChapterRecordOld : chapterRecordOlds) {
					DBChapterRecord chapterRecord = new DBChapterRecord();
					chapterRecord.setLimitStartTime(currentTimeSec);
					chapterRecord.setLimitFinishTime(currentTimeSec);
					chapterRecord.setLimitRewardGet(true);
					chapterRecord.setBoxAwardList(dbChapterRecordOld.getBoxAwardList());
					chapterRecord.setChapterId(dbChapterRecordOld.getChapterId());
					chapterRecord.setStageListList(dbChapterRecordOld.getStageListList());
					chapterRecords.add(chapterRecord);
				}
				StagePveDao.getInstance().updateNormalStageRecord(stagePve);
			}
			// elite
			dbPlayerStageRecordOld = stagePve.getEliteStageRecordDbOld();
			if (dbPlayerStageRecordOld != null) {
				DBPlayerStageRecord dbPlayerStageRecord = new DBPlayerStageRecord();
				stagePve.setEliteStageRecordDb(dbPlayerStageRecord);
				List<DBChapterRecordOld> chapterRecordOlds = dbPlayerStageRecordOld.getChapterListList();
				List<DBChapterRecord> chapterRecords = new ArrayList<>();
				dbPlayerStageRecord.setChapterListList(chapterRecords);
				for (DBChapterRecordOld dbChapterRecordOld : chapterRecordOlds) {
					DBChapterRecord chapterRecord = new DBChapterRecord();
					chapterRecord.setLimitStartTime(currentTimeSec);
					chapterRecord.setLimitFinishTime(currentTimeSec);
					chapterRecord.setLimitRewardGet(true);
					chapterRecord.setBoxAwardList(dbChapterRecordOld.getBoxAwardList());
					chapterRecord.setChapterId(dbChapterRecordOld.getChapterId());
					chapterRecord.setStageListList(dbChapterRecordOld.getStageListList());
					chapterRecords.add(chapterRecord);
				}
				StagePveDao.getInstance().updateEliteStageRecord(stagePve);
			}
		}
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
