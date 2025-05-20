package game;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

import cross.boss.logic.BossBattleManager;
import cross.boss.logic.CrossBossRewardTemplateCache;
import cross.logic.CrossCraftManager;
import game.bean.CrossGsBean;
import game.db.DataSourceManager;
import game.http.ScriptRunHandler;
import game.http.StatHandler;
import game.lan.CrossLanClientManager;
import game.lan.CrossLanServerManager;
import game.logic.CrossGsListManager;
import game.logic.CrossScheduleManager;
import game.module.craft.logic.CraftManager;
import game.module.craft.logic.CraftMatchManager;
import game.module.cross_boss.logic.CrossBossTemplateCache;
import game.module.other.OtherConfigCache;
import game.module.pvp.dao.PvpSkillTemplateCache;
import game.module.robot.logic.MonsterTemplateCache;
import game.module.user.dao.ExpTemplateCache;
import lion.common.MsgCodeAnn;
import lion.common.NamedThreadFactory;
import lion.common.ScheduledThreadPoolExecutorLog;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.core.Netty4Server;
import lion.netty4.filter.MsgAccessLimitCache;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.IGameServer;
import lion.netty4.message.MsgDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;

/**
 * 总线服务器，用来存储全局信息
 * 
 * @author hexuhui
 *
 */
public class CrossServer implements IGameServer {

	private Netty4Server netty4Server;

	private static CrossServer instance;

	private CrossLanClientManager lanServerManager;

	private HttpServer httpServer;

	private static Logger logger = LoggerFactory.getLogger(CrossServer.class);

	private volatile CrossServerConfig serverConfig;

	private Thread shutdownHook;

	/** 数据库操作使用的线程池 */
	public static ScheduledExecutorService executorService;

	public static MsgDispatcher<MsgProcessor> msgDispatcher = new MsgDispatcher<MsgProcessor>();

	public static void main(String[] args) {
		try {
			new CrossServer().start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static CrossServer getInstance() {
		return instance;
	}

	public CrossServerConfig getCrossServerConfig() {
		return serverConfig;
	}

	private void start() throws Exception {
		logger.info("--------start cross server------------");
		long startTime = System.currentTimeMillis();
		instance = this;
		logger.info("load configurations!");
		serverConfig = loadConfig();
		DataSourceManager.init(serverConfig.getMysqlUrl(), serverConfig.getMysqlClassname(),
				serverConfig.getMysqlUsername(), serverConfig.getMysqlPassword());
		registMessageProcessor();
		BaseProtoIoExecutor.setGameServer(this);
		logger.warn("start netty server");
		netty4Server = new Netty4Server(serverConfig.getSocketPort());
		netty4Server.run();
		// init login session manager
		CrossSessionManager.getInstance();
		// load template
		loadTemplates();
		logger.warn("init lan connection");
		startLanServer();
		// 初始化线程池
		executorService = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime().availableProcessors() * 3,
				new NamedThreadFactory("executorPool"));
		// 诸神争霸匹配
		logger.info("诸神争霸匹配线程");
		CraftMatchManager.getInstance().executorService = executorService;
		CraftMatchManager.getInstance().scheduleMatch();
		CraftManager.getInstance().executorService = executorService;
		CraftManager.getInstance().craftTimeoutManager = CrossCraftManager.getInstance();
		initHttpServer();
		initQuartzJobs();
		addShutDownHook();
		logger.warn("cross server startup success! in {} s", (System.currentTimeMillis() - startTime) / 1000);
	}

	private void startLanServer() {
		logger.info("init cross 2 gs connections!");
		lanServerManager = new CrossLanClientManager();
		List<CrossGsBean> list4Dbs = CrossGsListManager.getInstance().getServerList();
		for (CrossGsBean serverList4Db : list4Dbs) {
			lanServerManager.connect(serverList4Db.getIp(), serverList4Db.getLanPort());
		}
		logger.info("start lan server!");
		CrossLanServerManager.getInstance().startServer(serverConfig.getLanPort());
	}

	private void loadTemplates() {
		logger.info("load templates:ServerListManager");
		CrossGsListManager.getInstance().reload();
		OtherConfigCache.getInstance().reload();
		ExpTemplateCache.getInstance().reload();
		PvpSkillTemplateCache.getInstance().reload();
		MonsterTemplateCache.getInstance().reload();
		CrossBossTemplateCache.getInstance().reload();
		CrossBossRewardTemplateCache.getInstance().reload();
	}

	private void registMessageProcessor() {
		// 注册消息
		logger.warn("regist message processors!");
		Map<Integer, Integer> accessIntervalLimitMap = new HashMap<Integer, Integer>();
		Reflections reflections = new Reflections("cross");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MsgCodeAnn.class);
		try {
			for (Class<?> processorClass : annotated) {
				MsgCodeAnn ann = processorClass.getAnnotation(MsgCodeAnn.class);
				Object msgProcessorObj = processorClass.newInstance();
				if (msgDispatcher.containsMsgcode(ann.msgcode())) {
					logger.error("duplicate msgcode={}", ann.msgcode());
				}
				msgDispatcher.addMsgProcessor(ann.msgcode(), (MsgProcessor) msgProcessorObj);
				accessIntervalLimitMap.put(ann.msgcode(), ann.accessLimit());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		// 注册IO消息访问频率控制
		MsgAccessLimitCache.getInstance().initMsgTimeTemplate(accessIntervalLimitMap);
		// 打印processor信息
		msgDispatcher.dumpProcessors();
	}

	public void initHttpServer() {
		try {
			logger.warn("init http server,port={}!", serverConfig.getHttpPort());
			// use default backlog value
			httpServer = HttpServer.create(new InetSocketAddress(serverConfig.getHttpPort()), 0);
			// httpServer.createContext("/console", new ConsoleHandler());
			 httpServer.createContext("/script", new ScriptRunHandler());
			 httpServer.createContext("/stat", new StatHandler());
			// httpServer.createContext("/config", new ConfigHandler());
			httpServer.setExecutor(null); // creates a default executor
			httpServer.start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private void initQuartzJobs() {
		logger.warn("init quartz schedule jobs!");
		CrossScheduleManager.getInstance();
	}

	private void addShutDownHook() {
		shutdownHook = new Thread("shutdown-hook") {
			public void run() {
				try {
					shutdown();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	public void shutdown() {
		logger.warn("shutting down game server!");
		netty4Server.shutdown();
		logger.warn("shot down lan server!");
		lanServerManager.shutdown();
		logger.warn("close http server!");
		httpServer.stop(1);
		logger.warn("begin threadpool shutdown!");
		try {
			executorService.shutdown();
			while (!executorService.isTerminated()) {
				executorService.awaitTermination(5, TimeUnit.SECONDS);
			}
			logger.warn("logic threadpool terminate successfully!");
		} catch (Exception e) {
			logger.error("", e);
		}
		BossBattleManager.getInstance().shutdown();
		logger.warn("close quartz scheduler!");
		CrossScheduleManager.getInstance().shutdown();
		// close db
		DataSourceManager.getInstance().shutdown();
		logger.warn("successfully close cross server!");
	}

	public CrossServerConfig loadConfig() {
		logger.warn("load executer");
		InputStream propStream = null;
		Properties properties = new Properties();
		String serverConfStr = "cross.properties";
		try {
			propStream = CrossServer.class.getClassLoader().getResourceAsStream(serverConfStr);
			if (propStream == null) {
				propStream = CrossServer.class.getClassLoader().getResourceAsStream("/" + serverConfStr);
				properties.load(propStream);
			} else {
				properties.load(propStream);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		CrossServerConfig serverConfig = new CrossServerConfig();
		serverConfig.setSocketPort(Integer.valueOf(properties.getProperty(CrossServerConfig.KEY_SOCKET_PORT)));
		serverConfig.setHttpPort(Integer.valueOf(properties.getProperty(CrossServerConfig.KEY_HTTP_PORT)));
		serverConfig.setMysqlUrl(properties.getProperty(CrossServerConfig.KEY_MYSQL_URL));
		serverConfig.setMysqlClassname(properties.getProperty(CrossServerConfig.KEY_MYSQL_CLASSNAME));
		serverConfig.setMysqlUsername(properties.getProperty(CrossServerConfig.KEY_MYSQL_USERNAME));
		serverConfig.setMysqlPassword(properties.getProperty(CrossServerConfig.KEY_MYSQL_PASSWORD));
		serverConfig.setLanPort(Integer.valueOf(properties.getProperty(CrossServerConfig.KEY_LAN_PORT)));
		String lanAllowIps = properties.getProperty(CrossServerConfig.KEY_LAN_ALLOW_IP);
		String[] allowIps = StringUtils.split(lanAllowIps, "_");
		Set<String> allowIpSet = new HashSet<>();
		for (String aIp : allowIps) {
			allowIpSet.add(aIp);
		}
		serverConfig.setLanAllowIps(allowIpSet);
		return serverConfig;
	}

	public void reloadServerConfig() {
		serverConfig = loadConfig();
	}

	@Override
	public boolean checkIP(String address) {
		return true;
	}

	@Override
	public MsgProcessor getMsgProcessor(int msgCode) {
		return msgDispatcher.getMsgProcessor(msgCode);
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestMessage request) {
		// NULL
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestProtoMessage request) {
		// NULL
	}
	
	public CrossLanClientManager getLanClientManager(){
		return lanServerManager;
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, MyRequestMessage requestMsg) {
		// TODO Auto-generated method stub
		
	}

}
