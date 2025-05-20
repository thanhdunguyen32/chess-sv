package login.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import login.LoginServer;
import login.ServerList4Db;
import login.logic.ServerListManager;
import sdk.garena.SDKConstants;

public class PaymentCallbackHanlder implements HttpHandler {

	public static final Logger logger = LoggerFactory.getLogger(PaymentCallbackHanlder.class);

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
		logger.info("sifuba paymanet callback,request params={}", paramMap);
		String signStr = paramMap.get("sign");
		paramMap.remove("sign");
		Set<String> keySet = paramMap.keySet();
		List<String> keyList = new ArrayList<>(keySet);
		keyList.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String aKey : keyList) {
			if (i > 0) {
				sb.append("&");
			}
			sb.append(aKey).append("=").append(paramMap.get(aKey));
			i++;
		}
		String sourceStr = URLDecoder.decode(sb.toString(), "utf-8");
		sourceStr += "&" + SDKConstants.SIFUBA_PAY_KEY;
		String mySighStr = DigestUtils.md5Hex(sourceStr);
		// exist key
		logger.info("sign str,sourceStr={},mySign={},destSign={}", sourceStr, mySighStr, signStr);
		if (mySighStr.equalsIgnoreCase(signStr)) {
			responseStr = "success";
			// get params
			// String fromStr = paramMap.get("from");
			String userid = paramMap.get("userid");
			String orderid = paramMap.get("orderid");
			int money = Integer.valueOf(paramMap.get("money"));
			int time = Integer.valueOf(paramMap.get("time"));
			String ext1 = paramMap.get("ext1");
			// int gameid = Integer.valueOf(paramMap.get("gameid"));
			int serverid = Integer.valueOf(paramMap.get("serverid"));
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendZyPayment(gsHostName, targetGs.getLanPort(),
							userid, orderid, money, time, 0,serverid);
				}
			}
		} else {
			responseStr = "sign fail";
		}
		// response
		Headers responseHeaders = httpExchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/html; charset=utf-8");
		httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
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

	public String getHttpBody(HttpExchange httpExchange) throws IOException {
		InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String queryString = br.readLine();
		return queryString;
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

	public static void main(String[] args) throws UnsupportedEncodingException {
		String AuthorizationRawStr = "Signature sd3l0f123fg09";
		String AuthorizationStr = AuthorizationRawStr.substring("Signature ".length());
		System.out.println(AuthorizationStr);
		String ret = URLDecoder.decode(
				"ext1=0&ext2=ext2&from=sdk&gameid=10044&money=600&orderid=2016030420224676311231&outorderid=c510a988-c415-47d6-a07f-b3a9d4b1a98b&role=%E8%B6%85%E8%90%8C%E7%9A%84%E5%8D%A1%E5%88%A9%E7%BF%81&serverid=1&time=1457094910&userid=100001&9ade1d4a413bb861cd4d8f4bf5e76dea",
				"utf-8");
		System.out.println(ret);
	}

}
