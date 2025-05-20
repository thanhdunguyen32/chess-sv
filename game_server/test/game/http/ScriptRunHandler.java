package game.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import game.GameServer;
import lion.common.GameScriptEngineManager;

public class ScriptRunHandler implements HttpHandler {

	private GameScriptEngineManager scriptManager = GameScriptEngineManager.getInstance(GameServer.getInstance());

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String responseStr = "";
		String requestMethod = httpExchange.getRequestMethod();
		Map<String, String> paramMap = null;
		if (requestMethod.equalsIgnoreCase("get")) {
			paramMap = ConsoleHandler.extractGetParameters(httpExchange);
		} else if (requestMethod.equalsIgnoreCase("post")) {
			paramMap = ConsoleHandler.extractPostParameters(httpExchange);
		}
		String opType = paramMap.get("type");
		if (opType != null && opType.equals("input")) {
			jumpInput(httpExchange);
		} else {
			String queryString = paramMap.get("jsContent");
			if (!StringUtils.isEmpty(queryString)) {
				try {
					responseStr = scriptManager.dynamicExecute(queryString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseStr.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(responseStr.getBytes());
			os.close();
		}
	}

	private void jumpInput(HttpExchange httpExchange) throws IOException {
		StringBuilder responseStrBuilder = new StringBuilder();

		// response body
		responseStrBuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
		responseStrBuilder.append("<HTML>\n");
		responseStrBuilder.append("<HEAD>\n");
		responseStrBuilder.append("<TITLE>脚本动态执行</TITLE>\n");
		responseStrBuilder.append("<META NAME=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
		responseStrBuilder.append("</HEAD>\n");
		responseStrBuilder.append("<BODY>\n");

		responseStrBuilder.append("<h1 style=\"color:blue;\">脚本内容</h1>\n");
		responseStrBuilder.append("<form action=\"script\" method=\"post\">\n");
		responseStrBuilder.append("<textarea name=\"jsContent\" rows=\"20\" cols=\"100\">\n");
		responseStrBuilder.append("</textarea>\n");
		responseStrBuilder.append("<br /><input type=\"submit\" />&nbsp;&nbsp;&nbsp;<input type=\"reset\" />\n");
		responseStrBuilder.append("</form>\n");

		responseStrBuilder.append("</BODY>\n");
		responseStrBuilder.append("</HTML>\n");

		String responseStr = responseStrBuilder.toString();
		Headers responseHeaders = httpExchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/html; charset=utf-8");
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		OutputStream os = httpExchange.getResponseBody();
		os.write(responseStr.getBytes("UTF-8"));
		os.close();
	}

}
