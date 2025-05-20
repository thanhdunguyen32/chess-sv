package login.http;

import java.io.BufferedReader;
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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoginControlHandler implements HttpHandler {

	public static final Logger logger = LoggerFactory.getLogger(LoginControlHandler.class);

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		StringBuilder responseStrBuilder = new StringBuilder();

		// response body
		responseStrBuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
		responseStrBuilder.append("<HTML>\n");
		responseStrBuilder.append("<HEAD>\n");
		responseStrBuilder.append("<TITLE>服务器管理</TITLE>\n");
		responseStrBuilder.append("<META NAME=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
		responseStrBuilder.append("</HEAD>\n");
		responseStrBuilder.append("<BODY>\n");

		responseStrBuilder.append("<a href=\"console?op=restartGameServer\">重启游戏服务器</a>\n");

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
