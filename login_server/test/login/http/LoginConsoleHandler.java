package login.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;
import login.LoginServer;
import login.logic.ServerListManager;

public class LoginConsoleHandler implements HttpHandler {

	public static final Logger logger = LoggerFactory.getLogger(LoginConsoleHandler.class);

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String responseStr = "";
		String requestMethod = httpExchange.getRequestMethod();
		Map<String, String> paramMap = null;
		if (requestMethod.equalsIgnoreCase("get")) {
			paramMap = extractGetParameters(httpExchange);
		} else if (requestMethod.equalsIgnoreCase("post")) {
			paramMap = extractPostParameters(httpExchange);
		}
		String opValue = paramMap.get("op");
		if (opValue == null) {
			responseStr = "parameter error!";
		} else if (opValue.equals("close")) {
			LoginServer.getInstance().removeShutDownHook();
			LoginServer.getInstance().shutdown();
			responseStr = "login server closing!";
		} else if (opValue.equals("kick")) {
			responseStr = "kick player,count=" + BaseIoExecutor.getOnlineCount();
			BaseIoExecutor.kickAll();
			BaseProtoIoExecutor.kickAll();
		} else if (opValue.equals("reload")) {
			logger.info("reload template start!");
			ServerListManager.getInstance().reload();
			logger.info("reload template end!");
			responseStr = "reload success!";
		} else if (opValue.equals("reconnect")) {
//			String gsHostName = LoginServer.getInstance().getLoginServerConfig().getGsHostName();
//			int gsPort = LoginServer.getInstance().getLoginServerConfig().getGsPort();
//			boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName, gsPort);
//			if (reconnectRet) {
//				responseStr = "reconect GameServer success!";
//			} else {
//				responseStr = "reconect GameServer fail!";
//			}
		} else if (opValue.equals("login")) {
			// String gsHostName =
			// LoginServer.getInstance().getLoginServerConfig().getGsHostName();
			// int gsPort =
			// LoginServer.getInstance().getLoginServerConfig().getGsPort();
			// boolean reconnectRet =
			// LoginServer.getInstance().getLanClientManager().connect(gsHostName,
			// gsPort);
			// if (reconnectRet) {
			// long sessionId = LoginServer.getInstance().generateRandomId();
			// int uid = 10086;
			// LoginServer.getInstance().getLanClientManager().sendMessage(gsHostName,
			// LoginServer.getInstance().getLoginServerConfig().getGsPort(),
			// sessionId, uid);
			// }
		} else if (opValue.equals("restartGameServer")) {
			ProcessBuilder pb = new ProcessBuilder("/home/tool/run_seed.sh");
			pb.redirectOutput(new File("/home/tool/build.log"));
			pb.start();
		}
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseStr.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(responseStr.getBytes());
		os.close();
	}

	public static Map<String, String> extractGetParameters(HttpExchange httpExchange) {
		URI requestUri = httpExchange.getRequestURI();
		String queryString = requestUri.getRawQuery();
		return extractParameters(queryString);
	}

	public static Map<String, String> extractPostParameters(HttpExchange httpExchange) throws IOException {
		InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String queryString = br.readLine();
		return extractParameters(queryString);
	}

	public static Map<String, String> extractParameters(String queryString) {
		Map<String, String> retMap = new HashMap<String, String>();
		String[] retList = StringUtils.split(queryString, "&");
		if (ArrayUtils.isNotEmpty(retList)) {
			for (String parameterOne : retList) {
				String[] paramPair = StringUtils.split(parameterOne, "=");
				if (ArrayUtils.isNotEmpty(paramPair) && paramPair.length == 2) {
					retMap.put(paramPair[0], paramPair[1]);
				}
			}
		}
		return retMap;
	}

}
