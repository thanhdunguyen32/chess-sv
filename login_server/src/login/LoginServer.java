package login;

import game.db.DataSourceManager;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lion.common.*;
import lion.http.HttpMapUri;
import lion.http.HttpPostProtoHandler;
import lion.http.Netty4HttpServer;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.core.MyIoExecutor;
import lion.netty4.core.ServerStat;
import lion.netty4.filter.MsgAccessLimitCache;
import lion.netty4.message.*;
import lion.netty4.processor.HttpProcessor;
import lion.netty4.processor.MsgProcessor;
import lion.session.GlobalTimer;
import lion.websocket.WebsocketServer;
import login.bean.ServerList4Db;
import login.http.*;
import login.logic.*;
import login.processor_http.HttpFileHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class LoginServer implements IGameServer {

	private static Logger logger = LoggerFactory.getLogger(LoginServer.class);

	private static final boolean IS_SSL = System.getProperty("ssl") != null;

	private WebsocketServer netty4ServerSSL;

	private WebsocketServer netty4Server;

	private static LoginServer instance;

	private volatile LoginServerConfig serverConfig;

	private Thread shutdownHook;

	private Netty4HttpServer netty4HttpServer;

	public static MsgDispatcher<MsgProcessor> msgDispatcher = new MsgDispatcher<MsgProcessor>();
	
	public static MsgDispatcher<HttpProcessor> httpMsgDispatcher = new MsgDispatcher<HttpProcessor>();

	private LoginLanClientManager lanServerManager;

	/** 数据库操作使用的线程池 */
	public static ScheduledExecutorService executorService;

	private IdWorker uuIdGenerator;

	public static LoginServer getInstance() {
		return instance;
	}

	public LoginLanClientManager getLanClientManager() {
		return lanServerManager;
	}

	public LoginServerConfig getLoginServerConfig() {
		return serverConfig;
	}

	public static void main(String[] args) {
		try {
			new LoginServer().start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public long generateRandomId() {
		return uuIdGenerator.nextId();
	}

	private void start() throws Exception {
		instance = this;
		long startTime = System.currentTimeMillis();
		uuIdGenerator = new IdWorker(1);
		serverConfig = loadConfig();
		DataSourceManager.init(serverConfig.getMysqlUrl(), serverConfig.getMysqlClassname(),
				serverConfig.getMysqlUsername(), serverConfig.getMysqlPassword());
		registMessageProcessor();
		BaseProtoIoExecutor.setGameServer(this);
		MyIoExecutor.setGameServer(this);
		logger.warn("start netty server");
		ThreadFactory bossThreadFactory = new NamedThreadFactory("@+netty_boss");
		ThreadFactory workerThreadFactory = new NamedThreadFactory("@+netty_I/O");
		int workerThreadCount = Runtime.getRuntime().availableProcessors() * 3;
		netty4Server = new WebsocketServer(serverConfig.getSocketPort(), false);
		netty4Server.run(workerThreadCount, bossThreadFactory, workerThreadFactory);
		if (IS_SSL) {
			netty4ServerSSL = new WebsocketServer(serverConfig.getSocketPortSsl(), true);
			netty4ServerSSL.run(workerThreadCount, bossThreadFactory, workerThreadFactory,
					serverConfig.getSslCertPath(), serverConfig.getSslKeyPath());
		}
		// init login session manager
		LoginSessionManager.getInstance();
		// load template
		loadTemplates();
		logger.warn("init lan connection");
		startLanServer();
		initHttpServer();
		// init lan server
		LoginLanServerManager.getInstance().startServer(serverConfig.getLanPort());
		// 初始化线程池
		executorService = new ScheduledThreadPoolExecutorLog(Runtime.getRuntime().availableProcessors() * 6,
				new NamedThreadFactory("executorPool"));
		addShutDownHook();
		long endTime = System.currentTimeMillis();
		logger.warn("login server startup success! in {} s", (endTime - startTime) / 1000f);
		ServerStat.startUpTime = endTime;
	}

	private void startLanServer() {
		logger.info("start lan server!");
		lanServerManager = new LoginLanClientManager();
		List<ServerList4Db> list4Dbs = ServerListManager.getInstance().getServerList();
		for (ServerList4Db serverList4Db : list4Dbs) {
			lanServerManager.connect(serverList4Db.getIp(), serverList4Db.getLanPort());
		}
	}

	private void loadTemplates() {
		logger.info("load templates:ServerListManager");
		ServerListManager.getInstance().reload();
		ServerHasRoleManager.getInstance().reload();
		AnnounceManager.getInstance().reload();
	}

	private LoginServerConfig loadConfig() {
		logger.warn("load executer");
		InputStream propStream = null;
		Properties properties = new Properties();
		String serverConfStr = LoginServerConfig.CONFIG_FILE_NAME;
		try {
			propStream = LoginServer.class.getClassLoader().getResourceAsStream(serverConfStr);
			if (propStream == null) {
				propStream = LoginServer.class.getClassLoader().getResourceAsStream("/" + serverConfStr);
				properties.load(propStream);
			} else {
				properties.load(propStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LoginServerConfig serverConfig = new LoginServerConfig();
		serverConfig.setSocketPort(Integer.parseInt(properties.getProperty(LoginServerConfig.KEY_SOCKET_PORT)));
		serverConfig.setSocketPortSsl(Integer.parseInt(properties.getProperty("socket_port_ssl")));
		serverConfig.setLanPort(Integer.parseInt(properties.getProperty(LoginServerConfig.KEY_LAN_PORT)));
		serverConfig.setHttpPort(Integer.parseInt(properties.getProperty(LoginServerConfig.KEY_HTTP_PORT)));
		serverConfig.setMysqlUrl(properties.getProperty(LoginServerConfig.KEY_MYSQL_URL));
		serverConfig.setMysqlClassname(properties.getProperty(LoginServerConfig.KEY_MYSQL_CLASSNAME));
		serverConfig.setMysqlUsername(properties.getProperty(LoginServerConfig.KEY_MYSQL_USERNAME));
		serverConfig.setMysqlPassword(properties.getProperty(LoginServerConfig.KEY_MYSQL_PASSWORD));
		serverConfig.setAnnouncement(properties.getProperty(LoginServerConfig.KEY_announcement));
		serverConfig.setCodeVersion(Integer.valueOf(properties.getProperty(LoginServerConfig.KEY_CODE_VERSION)));
		serverConfig.setCodeReview(Integer.valueOf(properties.getProperty(LoginServerConfig.KEY_CODE_REVIEW)));
		serverConfig.setLoginServerHost(properties.getProperty(LoginServerConfig.KEY_LOGIN_SERVER_HOST));
		serverConfig.setLoginServerPort(Integer.valueOf(properties.getProperty("login.server.port")));
		String lanAllowIps = properties.getProperty(LoginServerConfig.KEY_LAN_ALLOW_IP);
		String[] allowIps = StringUtils.split(lanAllowIps, StringConstants.SEPARATOR_DI);
		Set<String> allowIpSet = new HashSet<>();
		Collections.addAll(allowIpSet, allowIps);
		serverConfig.setLanAllowIps(allowIpSet);
		// 设置白名单
		String whiteListStr = properties.getProperty(LoginServerConfig.KEY_LOGIN_WHITE_LIST);
		String[] whiteListStrs = StringUtils.split(whiteListStr, StringConstants.SEPARATOR_DI);
		Set<String> whiteListSet = new HashSet<>();
		Collections.addAll(whiteListSet, whiteListStrs);
		serverConfig.setLoginWhiteList(whiteListSet);
		serverConfig.setSslCertPath(properties.getProperty("ssl.cert.path"));
		serverConfig.setSslKeyPath(properties.getProperty("ssl.key.path"));
		return serverConfig;
	}

	public void reloadServerConfig() {
		serverConfig = loadConfig();
	}

	private void registMessageProcessor() {
		// 注册消息
		logger.warn("regist message processors!");
		Map<Integer, Integer> accessIntervalLimitMap = new HashMap<Integer, Integer>();
		FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("login");
		List<String> list1 = fastClasspathScanner.scan().getNamesOfClassesWithAnnotation(MsgCodeAnn.class);
		try {
			for (String processorClassName : list1) {
				Class<?> processorClass = Class.forName(processorClassName);
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
		logger.warn("regist http processors!");
		accessIntervalLimitMap = new HashMap<Integer, Integer>();
		list1 = fastClasspathScanner.scan().getNamesOfClassesWithAnnotation(MsgCodeAnnHttp.class);
		try {
			for (String processorClassName : list1) {
				Class<?> processorClass = Class.forName(processorClassName);
				MsgCodeAnnHttp ann = processorClass.getAnnotation(MsgCodeAnnHttp.class);
				Object msgProcessorObj = processorClass.getDeclaredConstructor().newInstance();
				if (httpMsgDispatcher.containsMsgcode(ann.msgcode())) {
					logger.error("duplicate msgcode={}", ann.msgcode());
				}
				httpMsgDispatcher.addMsgProcessor(ann.msgcode(), (HttpProcessor) msgProcessorObj);
				accessIntervalLimitMap.put(ann.msgcode(), ann.accessLimit());
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		// 注册IO消息访问频率控制
		MsgAccessLimitCache.getInstance().initMsgTimeTemplate(accessIntervalLimitMap);
		// 打印processor信息
		msgDispatcher.dumpProcessors();
		httpMsgDispatcher.dumpProcessors();
	}

	private void addShutDownHook() {
		shutdownHook = new Thread("shutdown-hook") {
			public void run() {
				shutdown();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	private void initHttpServer() {
		try {
			logger.warn("init http server,port={}!", serverConfig.getHttpPort());
			// netty4server
			netty4HttpServer = new Netty4HttpServer(serverConfig.getHttpPort(),
					Runtime.getRuntime().availableProcessors() * 3);
			HttpMapUri mapHanlder = new HttpMapUri(
					new ScriptRunNettyHandler("/script"),
					new StatHandler("/stat"),
					new QunHeiPayHandler("/qunhei/charge"),
					new ZhangMengPayHandler("/zhangmeng_pay"),
					new Three33PayHandler("/333_pay"),
					new ChongChongPayHandler("/chongchong_pay"),
					new F4399PayHandler("/4399_pay"),
					new YijiePayCallbackHanlder("/yijie_pay","C1IXNVRK1ENEUVHV4C2AYEUZL5VI670I"),//广州秒乐
					new YijiePayCallbackHanlder("/yijie_ly","9WF7FVXGEDX2NFQ2VL6A5EIVIWTGPWEO"),//福州蓝月
					new YijiePayCallbackHanlder("/yijie_lyios","SOXNDBPSIW6VCSDSLHTF4FV5YQ6LBC4K"),//福州蓝月ios
					new YijiePayCallbackHanlder("/yijie_douwan","XVHKXDM7FXDDY41SOEASVNTBH7Q5H3DD"),//广州逗游
					new YijiePayCallbackHanlder("/yijie_douwanios","PYV1Q7XA8CNQJ88Z8GRMTRX7LIAO49VV"),//广州逗游ios
					new YijiePayCallbackHanlder("/yijie_liuye","3P6T9YHQ8KXGP8N4VRAP1OED0YETS168"),//北京流烨
					new HttpForwardHandler("/yijie_liuye_oppo","http://sync.imooffice.cn:81/cb/oppo/D73772674BFDF06B/sync.html"),//北京流烨oppo
					new GNetopPayCallbackHanlder("/gnetop_pay"),
					new YunbeePayCallbackHanlder("/yunbee_pay","9m53YajhVGsdLtUr"),//上海典游ios
					new YunbeePayCallbackHanlder("/yunbee_pay2","EVN1CO3yXzAS8Rlx"),//上海典游ios2
					new YunbeePayCallbackHanlder("/yunbee_pay3","EJGiiM955iKZceey"),//上海典游ios3
					new YunbeePayCallbackHanlder("/yunbee_pay4","XEodPhnKAuHjw1rX"),//上海典游ios4
					new YunbeePayCallbackHanlder("/yunbee_pay5","KnxjE5jsSTftPjvn"),//上海典游ios5
					new YunbeePayCallbackHanlder("/yunbee_pay6","0zKXFGQel96JaO2x"),//上海典游ios6
					new YunbeePayCallbackHanlder("/yunbee_pay7","KwVnYk3DrmYXbUt2"),//上海典游ios7
					new YunbeePayCallbackHanlder("/yunbee_pay8","mFoP3HgFtO5Wc9SN"),//上海典游ios8
					new YunbeePayCallbackHanlder("/yunbee_pay9","vfNqUg0ion29w7iw"),//上海典游ios9
					new YunbeePayCallbackHanlder("/yunbee_pay10","qd1mQS9OAi3McX9a"),//上海典游ios10
					new YunbeePayCallbackHanlder("/yunbee_pay11","9bJO8K7uTqTZO7CS"),//上海典游ios-真三国萌将1031(苹果版)
					new YunbeePayCallbackHanlder("/yunbee_cszsg","pV0vfCSJuM2bCN18"),//上海典游ios-超神战三国(苹果版)
					new YunbeePayCallbackHanlder("/yunbee_qmcsg","dZIFwYh0XDchLhTM"),//上海典游ios-Q萌闯三国(苹果版)
					new YunbeePayCallbackHanlder("/yunbee_sjcsg","Vw9d8cOs6RGvuCe7"),//上海典游ios-神将闯三国
					new LiuyePayHandler("/liuye_pay"),//北京流烨ios
					new PingPingPayHandler("/pingping","55b02918a9caa3c1d6fbea0a139cc897"),//广州平平
					new PingPingPayHandler("/pingping_qbsgz", "4547d293e60aedb5646f10e9c8777dac"),//广州平平-Q版三国传
					new YiJieSwitchPayCallbackHanlder("/xingteng_pay","37ecc2c1cbf445d6bc4782a2058df2af","xt-"),//星腾ios
					new YiJieSwitchPayCallbackHanlder("/xingteng_pay2","2e36509a88054b749a7679d01a1efc90","xt-"),//星腾ios2
					new YiJieSwitchPayCallbackHanlder("/xingteng_pay3","402715705a0a45648e0ab3ba03b0acf0","xt-"),//星腾ios3
					new YiJieSwitchPayCallbackHanlder("/chuangfu_xysg","7d88d088de2645af8ddf056b7223898b","cf-"),//创富-逍遥三国
					new QulelePayCallbackHanlder("/qulele_mxsg","9e8c1c2f2dc78796c94a78f760526f42","qll-"),//逗玩-梦寻三国
					new MoXiPayCallbackHanlder("/moxi_mjsgz","2db5f5bd5112e4a3a5ef9822ef8e871c","mr-"),//喵人-名将三国志
					new O185SyRhPayHandler("/185sy_pay","7d2b3dfbb8c11a8f2c5551cd06cd5c23"),//185sy rh pay
					new O185SyRhFanliHandler("/185sy_fanli","7d2b3dfbb8c11a8f2c5551cd06cd5c23"),//185sy rh 返利
					new O185syRhServerListHandler("/185sy_serverlist"),//185sy rh SDK server list
					new HttpPostProtoHandler("/login.do"),
					new QuickPayCallbackHanlder("/manv_pay","cevxlwdi8rujpa4bsfw4h1swlahhxsaa","98863606966169137219244323179378"),//广州天战 满V
					new QuickPayCallbackHanlder("/hpwmjcscw","qjxm3jiajlzchvl4uf88s6g8253chxr7","28213335500989957940629569395648"),//嗨皮玩 名将传说 长尾
					new QuickPayCallbackHanlder("/yizhicw","t9cn9zuhapqq7elsjprrf4zwerwtxbbq","03033931159522893603582785292002"),//倚智常规 长尾 h5
					HttpFileHandler.initInstance("/version/notice/qqGroup.json","/glory_account/account/getdirty.do","/getnotice.do","/version/APK_VERSION","/version/IPA_VERSION")
					);
			netty4HttpServer.start(null, mapHanlder);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void removeShutDownHook() {
		if (shutdownHook != null) {
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
		}
	}

	public void shutdown() {
		logger.warn("shutdown login server!");
		if(netty4ServerSSL != null) {
			netty4ServerSSL.shutdown();
		}
		netty4Server.shutdown();
		logger.warn("shutdown lan server!");
		lanServerManager.shutdown();
		LoginLanServerManager.getInstance().shutdown();
		logger.warn("close http server!");
		try {
			netty4HttpServer.shutdown();
		} catch (Exception e) {
			logger.error("stop jetty server error!", e);
		}
		// close timer
		logger.warn("global timmer shutdown!");
		GlobalTimer.getInstance().shutdown();
		// logger.warn("begin threadpool shutdown!");
		// try {
		// executorService.shutdown();
		// while (!executorService.isTerminated()) {
		// executorService.awaitTermination(5, TimeUnit.SECONDS);
		// }
		// logger.warn("executorServiceForDB terminate successfully!");
		// } catch (Exception e) {
		// logger.error("", e);
		// }
		// close db
		DataSourceManager.getInstance().shutdown();
		logger.warn("successfully close login server!");
	}

	@Override
	public boolean checkIP(String address) {
		return true;
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestByteMessage request) {
		// NULL
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, RequestProtoMessage request) {
		// NULL
	}

	@Override
	public MsgProcessor getMsgProcessor(int msgCode) {
		return msgDispatcher.getMsgProcessor(msgCode);
	}

	@Override
	public void syncExecuteIoRequest(GamePlayer player, MyRequestMessage requestMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HttpProcessor getHttpProcessor(int msgCode) {
		return httpMsgDispatcher.getMsgProcessor(msgCode);
	}

}
