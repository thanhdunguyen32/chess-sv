package game.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.mail.logic.MailManager;
import game.module.other.PushManager;
import game.module.rank.logic.RankManager;
import game.session.SessionManager;
import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;

public class ConsoleHandler implements HttpHandler {

	public static final Logger logger = LoggerFactory.getLogger(ConsoleHandler.class);

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		logger.info("admin operation,ip={},query={}", httpExchange.getRemoteAddress(),
				httpExchange.getRequestURI().getRawQuery());
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
			GameServer.getInstance().removeShutDownHook();
			GameServer.getInstance().shutdown();
			responseStr = "game server closing!";
		} else if (opValue.equals("kick")) {
			responseStr = "kick player,count=" + BaseIoExecutor.getOnlineCount();
			BaseIoExecutor.kickAll();
			BaseProtoIoExecutor.kickAll();
		} else if (opValue.equals("reload")) {

			StringBuilder sb = new StringBuilder();
			Process process;
			String cmd = "sh /home/tool/run_design_data.sh";// 这里必须要给文件赋权限 chmod u+x fileName;
			try {
				// 使用Runtime来执行command，生成Process对象
				Runtime runtime = Runtime.getRuntime();
				process = runtime.exec(cmd);
				// 取得命令结果的输出流
				InputStream is = process.getInputStream();
				// 用一个读输出流类去读
				InputStreamReader isr = new InputStreamReader(is);
				// 用缓冲器读行
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				boolean skipRead = false;
				while ((line = br.readLine()) != null) {
					logger.info(line);
					if (line.contains("--------excel 2 java------------")) {
						skipRead = true;
					}
					if (!skipRead) {
						sb.append(line + "\n");
					}
				}
				// 执行关闭操作
				is.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				logger.error("", e);
			}
			sb.append("reload memory data!\n");
			logger.info("reload template start!");
			GameServer.getInstance().loadTemplates();
			logger.info("reload template end!");
			sb.append("reload success!");
			responseStr = sb.toString();
			logger.info("ret str:{}", sb.toString());
		} else if (opValue.equals("mail")) {
			MailManager.getInstance().checkPastDueMail();
			responseStr = "check past due mail";
		} else if (opValue.equals("closeTip")) {
			logger.info("close server notification!");
			Collection<PlayingRole> playingRoles = SessionManager.getInstance().getAllPlayers();
			for (PlayingRole playingRole : playingRoles) {
				if (StringUtils.isNotBlank(playingRole.getPlayerBean().getAccountId())) {
					PushManager.getInstance().sendStopServer(5, playingRole.getPlayerBean().getAccountId());
				}
			}
		} else if (opValue.equals("rank")) {
			try {
				RankManager.getInstance().execute(null);
				responseStr = "rank success!";
			} catch (JobExecutionException e) {
				logger.error("", e);
			}
		} else if (opValue.equals("clientData")) {

			Process process;
			String cmd = "sh /home/tool/run_data.sh";// 这里必须要给文件赋权限 chmod u+x fileName;

			try {
				// 使用Runtime来执行command，生成Process对象
				Runtime runtime = Runtime.getRuntime();
				process = runtime.exec(cmd);
				// 取得命令结果的输出流
				InputStream is = process.getInputStream();
				// 用一个读输出流类去读
				InputStreamReader isr = new InputStreamReader(is);
				// 用缓冲器读行
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					logger.info(line);
				}
				// 执行关闭操作
				is.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				logger.error("", e);
			}
			responseStr = new Date() + ":parse data success!";
		}
		Headers responseHeaders = httpExchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");

		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseStr.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(responseStr.getBytes());
		os.close();
	}

	public static Map<String, String> extractGetParameters(HttpExchange httpExchange) throws IOException {
		URI requestUri = httpExchange.getRequestURI();
		String queryString = requestUri.getRawQuery();
		return extractParameters(queryString);
	}

	public static Map<String, String> extractPostParameters(HttpExchange httpExchange) throws IOException {
		InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String queryString = null;
		StringBuilder sb = new StringBuilder();
		while ((queryString = br.readLine()) != null) {
			sb.append(queryString);
		}
		return extractParameters(sb.toString());
	}

	public static Map<String, String> extractParameters(String queryString) throws IOException {
		Map<String, String> retMap = new HashMap<String, String>();
		String[] retList = StringUtils.split(queryString, "&");
		if (ArrayUtils.isNotEmpty(retList)) {
			for (String parameterOne : retList) {
				String[] paramPair = StringUtils.split(parameterOne, "=");
				if (ArrayUtils.isNotEmpty(paramPair) && paramPair.length == 2) {
					retMap.put(paramPair[0], URLDecoder.decode(paramPair[1], "UTF-8"));
				}
			}
		}
		return retMap;
	}

}
