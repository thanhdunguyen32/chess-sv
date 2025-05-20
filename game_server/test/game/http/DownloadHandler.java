package game.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import game.module.user.logic.PlayerManager;

public class DownloadHandler implements HttpHandler {

	private static Logger logger = LoggerFactory.getLogger(DownloadHandler.class);
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		logger.info("http download hanlder!");
		Headers responseHeaders = httpExchange.getResponseHeaders();

		String requestMethod = httpExchange.getRequestMethod();
		Map<String, String> paramMap = null;
		if (requestMethod.equalsIgnoreCase("get")) {
			paramMap = extractGetParameters(httpExchange);
		} else if (requestMethod.equalsIgnoreCase("post")) {
			paramMap = extractPostParameters(httpExchange);
		}
		String opValue = paramMap.get("file");
		if (opValue == null) {
			// do nothing
		} else {
			OutputStream os = httpExchange.getResponseBody();

			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String mimeType = fileNameMap.getContentTypeFor("file/" + opValue);
			if (StringUtils.isEmpty(mimeType)) {
				mimeType = "application/octet-stream";
			}
			responseHeaders.set("Content-Type", mimeType);
			if (!(mimeType.equals("image/jpeg") || mimeType.equals("text/html"))) {
				responseHeaders.set("Content-Disposition", "attachment; filename=\"" + opValue + "\"");
			}
			File aFile = new File("file/" + opValue);
			byte[] pomByte = Files.toByteArray(aFile);
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, pomByte.length);
			os.write(pomByte);
			os.close();
		}

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
