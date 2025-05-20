package game;

import game.db.DataSourceManager;
import game.http.ScriptRunNettyHandler;
import game.http.StatHandler;
import game.module.activity.dao.*;
import game.module.badword.dao.BadWordTemplateCache;
import game.module.badword.logic.BadWordFilter;
import game.module.cdkey.dao.CdkeyCache;
import game.module.chapter.dao.PowerFormationCache;
import game.module.chat.logic.ChatManager;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendshipSendCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.item.dao.RBoxTemplateCache;
import game.module.kingpvp.dao.KingPvpCache;
import game.module.lan.logic.GameLanClientManager;
import game.module.lan.logic.GameLanServerManager;
import game.module.legion.dao.LegionCache;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineManager;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pay.logic.PaymentManager;
import game.module.pvp.dao.PvpCache;
import game.module.quartz.QuartzSchedulerManager;
import game.module.rank.dao.RankCache;
import game.module.rank.dao.RankLikeCache;
import game.module.season.dao.SeasonCache;
import game.module.template.BadWordTemplate;
import game.module.tower.dao.TowerReplayCache;
import game.module.user.logic.TopupFeedbackManager;
import game.module.worldboss.dao.WorldBossCache;
import game.session.PlayerDownlineManager;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.netty.util.Timeout;
import lion.common.*;
import lion.http.HttpMapUri;
import lion.http.Netty4HttpServer;
import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.core.MyIoExecutor;
import lion.netty4.core.ServerStat;
import lion.netty4.filter.MsgAccessLimitCache;
import lion.netty4.message.*;
import lion.netty4.processor.HttpProcessor;
import lion.netty4.processor.MsgProcessor;
import lion.session.GlobalTimer;
import lion.websocket.WebsocketServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class GameServer implements IGameServer {

	private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

	public static MsgDispatcher<MsgProcessor> msgDispatcher = new MsgDispatcher<MsgProcessor>();

//	private DisruptorExecutor serialExecuter;

	private ServerConfig serverConfig;

	private static final boolean IS_SSL = System.getProperty("ssl") != null;

	private WebsocketServer websocketServer;

	private WebsocketServer websocketServerSSL;

	private Netty4HttpServer netty4HttpServer;

	private Thread shutdownHook;

	/** 数据库操作使用的线程池 */
	public static ScheduledExecutorService executorService;

	private static GameServer instance;

	private GameLanClientManager lanClientManager;

	private Map<Integer, Timeout> timeouts = new HashMap<Integer, Timeout>();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new GameServer().start();
	}

	public static GameServer getInstance() {
		return instance;
	}

	private void start() throws Exception {
		logger.warn("game server start!");
		instance = this;
		long startTime = System.currentTimeMillis();
		serverConfig = loadConfig();
		DataSourceManager.init(serverConfig.getMysqlGameUrl(), serverConfig.getMysqlGameClassname(),
				serverConfig.getMysqlGameUsername(), serverConfig.getMysqlGamePassword(),
				serverConfig.getMysqlLoginUrl(), serverConfig.getMysqlLoginClassname(),
				serverConfig.getMysqlLoginUsername(), serverConfig.getMysqlLoginPassword());
		initExecuter();
		registMessageProcessor();
		loadTemplates();
		BaseIoExecutor.setGameServer(this);
		BaseProtoIoExecutor.setGameServer(this);
		MyIoExecutor.setGameServer(this);
		logger.warn("start netty server");
		ThreadFactory bossThreadFactory = new NamedThreadFactory("@+netty_boss");
		ThreadFactory workerThreadFactory = new NamedThreadFactory("@+netty_I/O");
		int workerThreadCount = Runtime.getRuntime().availableProcessors() * 3;
		websocketServer = new WebsocketServer(serverConfig.getSocketPort(), false);
		websocketServer.run(workerThreadCount, bossThreadFactory, workerThreadFactory);
		if (IS_SSL) {
			websocketServerSSL = new WebsocketServer(serverConfig.getSocketPortSsl(), true);
			websocketServerSSL.run(workerThreadCount, bossThreadFactory, workerThreadFactory, serverConfig.getSslCertPath(),
					serverConfig.getSslKeyPath());
		}
		logger.info("init http server!");
		initHttpServer();
		initQuartzJobs();
		// init bad word
		logger.warn("init bad word!");
		List<BadWordTemplate> badWordTemplates = BadWordTemplateCache.getInstance().loadFromDb();
		BadWordFilter.getInstance().reload(badWordTemplates);
		// init lan server
		GameLanServerManager.getInstance().startServer(serverConfig.getLanPort());
		//init lan client
		lanClientManager = new GameLanClientManager();
		// player offline cache
		logger.warn("init player offline cache!");
		PlayerOfflineManager.getInstance().init();
		logger.warn("init rank!");
		RankCache.getInstance().load();
		logger.warn("load player chat visit data!");
		ChatManager.getInstance().loadFromDb();
		RankLikeCache.getInstance().loadFromDb();
//		PvpRankManager.getInstance().execute(null);
		logger.warn("stage battle force rank init!");
//		StagePveManager.getInstance().rankStageBattleForce();
		logger.warn("mine init!");
		MineCache.getInstance().loadFromDb();
		MineManager.getInstance().serverStartupInit(MineCache.getInstance().getDBMine());
		logger.warn("guozhan init!");
		GuozhanCache.getInstance().loadFromDb();
		logger.warn("load tcdk data!");
		CdkeyCache.getInstance().loadFromDb(1);
		ActivityCache.getInstance().loadFromDb();
		PaymentManager.getInstance().loadPayPlayers();
		FriendshipSendCache.getInstance().loadFromDb();
		SeasonCache.getInstance().loadFromDb();
		PvpCache.getInstance().loadFromDb();
		LegionCache.getInstance().loadFromDb();
		TowerReplayCache.getInstance().loadFromDb();
		FriendBossCache.getInstance().loadFromDb();
		PowerFormationCache.getInstance().loadFromDb();
		WorldBossCache.getInstance().loadFromDb();
		ActivityWeekCache.getInstance().initZhdTime();
		KingPvpCache.getInstance().loadFromDb();
		logger.warn("player offline cache size={}", PlayerOfflineManager.getInstance().getPlayerOfflineCacheSize());
		addShutDownHook();
		// online stat
		//OnlineUserManager.getInstance().run();
		long endTime = System.currentTimeMillis();
		logger.warn("game server startup success! in {} s", (endTime - startTime) / 1000f);
		ServerStat.startUpTime = endTime;
	}

	private void initQuartzJobs() {
		logger.warn("init quartz jobs!");
		QuartzSchedulerManager.getInstance();
	}

	/**
	 * 加载模版表
	 */
	public void loadTemplates() {
		logger.warn("load templates");
		// ItemTemplateCache.getInstance().reload();
		// UserLevelTemplateCache.getInstance().reload();
		// load language template
		FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("game.module");
		List<String> list1 = fastClasspathScanner.scan().getNamesOfClassesImplementing(Reloadable.class);
		try {
			for (String processorClassName : list1) {
				Class<?> class1 = Class.forName(processorClassName);
				Method getInstanceMethod = class1.getMethod("getInstance");
				Reloadable aInstance = (Reloadable) getInstanceMethod.invoke(null);
				logger.warn("load template={}", class1);
				aInstance.reload();
			}
			GeneralChipTemplateCache.getInstance().init5StarCamp();
			GeneralChipTemplateCache.getInstance().initStar4Camp();
			RBoxTemplateCache.getInstance().initElite5Star();
			RBoxTemplateCache.getInstance().initLegendStar();
			RBoxTemplateCache.getInstance().initNormal5Star();
		} catch (Exception e) {
			logger.error("", e);
		}
		//加载充值回馈
		TopupFeedbackManager.getInstance().reload();
	}

	public void registMessageProcessor() {
		// 注册消息
		logger.warn("regist message processors!");
		Map<Integer, Integer> accessIntervalLimitMap = new HashMap<Integer, Integer>();
		FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("game.module");
		List<String> list1 = fastClasspathScanner.scan().getNamesOfClassesWithAnnotation(MsgCodeAnn.class);
		logger.info("IO processor,size={}",list1.size());
		try {
			for (String processorClassName : list1) {
                Class<?> processorClass = Class.forName(processorClassName);
                logger.info("processorClass={}", processorClass);
				MsgCodeAnn ann = processorClass.getAnnotation(MsgCodeAnn.class);
				Object msgProcessorObj = processorClass.getDeclaredConstructor().newInstance();
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
		// msgDispatcher.dumpProcessors();
	}

	@Override
	public boolean checkIP(String address) {
		return true;
	}

	public void initExecuter() {
		logger.warn("init executer");
//		serialExecuter = new DisruptorExecutor();
//		serialExecuter.startService();
		// 初始化线程池
		executorService = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime().availableProcessors() * 3,
				new NamedThreadFactory("executorPool"));
	}

	public void shutdown() {
		logger.warn("shutting down game server!");
		if(websocketServerSSL != null) {
			websocketServerSSL.shutdown();
		}
		websocketServer.shutdown();
		logger.warn("close lan server!");
		GameLanServerManager.getInstance().shutdown();
		logger.warn("close http server!");
//		httpServer.stop(1);
		try {
			netty4HttpServer.shutdown();
		} catch (Exception e) {
			logger.error("stop jetty server error!", e);
		}
		// close timer
		logger.warn("global timmer shutdown!");
		GlobalTimer.getInstance().shutdown();
		// 逻辑处理
		shutdownGameLogic();
//		logger.warn("serial disruptor executer shutdown!");
//		serialExecuter.shutdown();
		logger.warn("begin threadpool shutdown!");
		try {
			executorService.shutdownNow();
			// executorService.shutdown();
			while (!executorService.isTerminated()) {
				logger.warn("thread pool info:{}", executorService);
				executorService.awaitTermination(5, TimeUnit.SECONDS);
			}
			logger.warn("logic threadpool terminate successfully!");
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.warn("close player offline cache!");
		PlayerOfflineManager.getInstance().clear();
		logger.warn("close quartz scheduler!");
		QuartzSchedulerManager.getInstance().shutdown();
		// close db
		DataSourceManager.getInstance().shutdown();
		logger.warn("successfully close game server!");

	}

	private void shutdownGameLogic() {
		logger.warn("arena save to db!");
		ActivityQZYBCache.getInstance().save2Db();
		ActivityChongZhiBangCache.getInstance().save2Db();
		ActivityXiaoFeiBangBangCache.getInstance().save2Db();
		ChatManager.getInstance().saveToDb();
		RankLikeCache.getInstance().saveToDb();
		FriendshipSendCache.getInstance().saveToDb();
		PvpCache.getInstance().save2Db();
		MineCache.getInstance().save2Db();
		GuozhanCache.getInstance().save2Db();
		// 保存玩家数据
		logger.warn("save all player data!");
		PlayerDownlineManager.getInstance().serverShutdown();
	}

	public ServerConfig loadConfig() throws ParseException {
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
		serverConfig.setSocketPort(Integer.parseInt(properties.getProperty(ServerConfig.KEY_SOCKET_PORT)));
		serverConfig.setSocketPortSsl(Integer.parseInt(properties.getProperty("socket.port.ssl")));
		serverConfig.setHttpPort(Integer.parseInt(properties.getProperty(ServerConfig.KEY_HTTP_PORT)));
        serverConfig.setLanPort(Integer.parseInt(properties.getProperty(ServerConfig.KEY_GS_LAN_PORT)));
        // Init Game Database
		serverConfig.setMysqlGameUrl(properties.getProperty(ServerConfig.KEY_MYSQL_GAME_URL));
		serverConfig.setMysqlGameClassname(properties.getProperty(ServerConfig.KEY_MYSQL_GAME_CLASSNAME));
		serverConfig.setMysqlGameUsername(properties.getProperty(ServerConfig.KEY_MYSQL_GAME_USERNAME));
        serverConfig.setMysqlGamePassword(properties.getProperty(ServerConfig.KEY_MYSQL_GAME_PASSWORD));
        
        // Init Login Database
		serverConfig.setMysqlLoginUrl(properties.getProperty(ServerConfig.KEY_MYSQL_LOGIN_URL));
		serverConfig.setMysqlLoginClassname(properties.getProperty(ServerConfig.KEY_MYSQL_LOGIN_CLASSNAME));
		serverConfig.setMysqlLoginUsername(properties.getProperty(ServerConfig.KEY_MYSQL_LOGIN_USERNAME));
        serverConfig.setMysqlLoginPassword(properties.getProperty(ServerConfig.KEY_MYSQL_LOGIN_PASSWORD));
        
        // Init Cross Server
		serverConfig.setCrossHost(properties.getProperty(ServerConfig.KEY_CROSS_HOST));
        serverConfig.setCrossLanPort(Integer.parseInt(properties.getProperty(ServerConfig.KEY_CROSS_LAN_PORT)));
        
        // Init Login Server
		serverConfig.setLoginHost(properties.getProperty("login.host"));
		serverConfig.setLoginLanPort(Integer.parseInt(properties.getProperty("login.lan.port")));
		serverConfig.setGmEnable(Boolean.parseBoolean(properties.getProperty(ServerConfig.KEY_GM_ENABLE)));
		String lanAllowIps = properties.getProperty(ServerConfig.KEY_LAN_ALLOW_IP);
		String[] allowIps = StringUtils.split(lanAllowIps, StringConstants.SEPARATOR_DI);
		Set<String> allowIpSet = new HashSet<>(Arrays.asList(allowIps));
		serverConfig.setLanAllowIps(allowIpSet);
		serverConfig.setIosIapIsSandbox(Boolean.parseBoolean(properties.getProperty(ServerConfig.KEY_IOS_IAP_SANDBOX)));
		serverConfig.setSslCertPath(properties.getProperty("ssl.cert.path"));
		serverConfig.setSslKeyPath(properties.getProperty("ssl.key.path"));
		serverConfig.setOpenTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(properties.getProperty("open_time")));
		String scorllAnnoStr = properties.getProperty("scroll.anno");
        serverConfig.setScrollAnno(Arrays.asList(StringUtils.split(scorllAnnoStr, "||")));
        
		return serverConfig;
	}

	public void initHttpServer() {
		try {
			logger.warn("init http server,port={}!", serverConfig.getHttpPort());
			// use default backlog value
//			httpServer = HttpServer.create(new InetSocketAddress(serverConfig.getHttpPort()), 0);
//			// httpServer.createContext("/console", new ConsoleHandler());
//			httpServer.createContext("/script", new ScriptRunHandler());
//			httpServer.createContext("/stat", new StatHandler());
//			httpServer.createContext("/config", new ConfigHandler());
//			// httpServer.createContext("/download", new DownloadHandler());
//			httpServer.setExecutor(null); // creates a default executor
//			httpServer.start();

			// netty4server
			netty4HttpServer = new Netty4HttpServer(serverConfig.getHttpPort());
//			HttpMapUri mapHanlder = new HttpMapUri(new ScriptRunNettyHandler("/script"),
//					new PaymentCallbackJettyHanlder("/pay_notify"), new YijiePayCallbackHanlder("/zl_callback"));
			HttpMapUri mapHanlder = new HttpMapUri(new ScriptRunNettyHandler("/script"),new StatHandler("/stat"));
			netty4HttpServer.start(null, mapHanlder);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public MsgProcessor getMsgProcessor(int msgCode) {
		return msgDispatcher.getMsgProcessor(msgCode);
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestByteMessage requestMsg) {
//		 serialExecuter.submitRawIoTask(player, requestMsg);
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestProtoMessage requestMsg) {
//		serialExecuter.submitProtoTask(player, requestMsg);
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, MyRequestMessage requestMsg) {
//		serialExecuter.submitWsIoTask(player, requestMsg);
	}

	public void syncExecuteRunnableTask(Runnable runnableTask) {
//		serialExecuter.submitRunnableTask(null, runnableTask);
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

	public void removeShutDownHook() {
		if (shutdownHook != null) {
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
		}
	}

	public void scriptTest() {
		logger.info("i am in script test!");
	}

	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public Timeout getTimeouts(int type) {
		return timeouts.get(type);
	}

	public void removeTimeout(int type){
		timeouts.remove(type);
	}

	public void setTimeouts(int type, Timeout timeout) {
		if (timeout != null) {
			timeouts.put(type, timeout);
		}
	}

	public GameLanClientManager getLanClientManager() {
		return lanClientManager;
	}

	@Override
	public HttpProcessor getHttpProcessor(int msgCode) {
		// TODO Auto-generated method stub
		return null;
	}


}
