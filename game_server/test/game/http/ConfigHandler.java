package game.http;

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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ConfigHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String responseStr = "ok";
		String requestMethod = httpExchange.getRequestMethod();
		Map<String, String> paramMap = null;
		if (requestMethod.equalsIgnoreCase("get")) {
			paramMap = extractGetParameters(httpExchange);
		} else if (requestMethod.equalsIgnoreCase("post")) {
			paramMap = extractPostParameters(httpExchange);
		}
		if (paramMap.get("add_robot_time") != null) {
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

	public static Map<String, String> extractPostParameters(HttpExchange httpExchange)
			throws IOException {
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
