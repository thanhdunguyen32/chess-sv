//package login.http;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URI;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.eclipse.jetty.server.Request;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.sun.net.httpserver.HttpExchange;
//
//import login.LoginServer;
//import login.bean.ServerList4Db;
//import login.logic.ServerListManager;
//import sdk.common.RSAUtils;
//
//public class DalanPayCallbackHanlder extends AbstractHandler {
//
//	public static final Logger logger = LoggerFactory.getLogger(DalanPayCallbackHanlder.class);
//
//	static class SingletonHolder {
//		static DalanPayCallbackHanlder instance = new DalanPayCallbackHanlder();
//	}
//
//	public static DalanPayCallbackHanlder getInstance() {
//		return SingletonHolder.instance;
//	}
//
//	public static Map<String, String> extractGetParameters(HttpExchange httpExchange) {
//		URI requestUri = httpExchange.getRequestURI();
//		String queryString = requestUri.getRawQuery();
//		return extractParameters(queryString);
//	}
//
//	public static Map<String, String> extractPostParameters(HttpExchange httpExchange) throws IOException {
//		InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
//		BufferedReader br = new BufferedReader(isr);
//		String queryString = br.readLine();
//		return extractParameters(queryString);
//	}
//
//	public String getHttpBody(Request baseRequest) throws IOException {
//		InputStreamReader isr = new InputStreamReader(baseRequest.getInputStream(), "utf-8");
//		BufferedReader br = new BufferedReader(isr);
//		String queryString = br.readLine();
//		return queryString;
//	}
//
//	public static Map<String, String> extractParameters(String queryString) {
//		Map<String, String> retMap = new HashMap<String, String>();
//		String[] retList = StringUtils.split(queryString, "&");
//		if (ArrayUtils.isNotEmpty(retList)) {
//			for (String parameterOne : retList) {
//				String[] paramPair = StringUtils.split(parameterOne, "=");
//				if (ArrayUtils.isNotEmpty(paramPair)) {
//					if (paramPair.length == 2) {
//						retMap.put(paramPair[0], paramPair[1]);
//					} else {
//						retMap.put(paramPair[0], "");
//					}
//				}
//			}
//		}
//		return retMap;
//	}
//
//	@Override
//	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
//			throws IOException, ServletException {
//		String queryStr = request.getQueryString();
//		logger.info("dalan paymanet callback,request params={}", queryStr);
//		Map<String, String> paramMap = extractParameters(queryStr);
//		String signStr = paramMap.get("sign");
//		paramMap.remove("sign");
//		paramMap.put("app_secret", LoginServer.getInstance().getLoginServerConfig().getDalan_app_secret());
//		Set<String> keySet = paramMap.keySet();
//		List<String> keyList = new ArrayList<>(keySet);
//		keyList.sort(new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				return o1.compareTo(o2);
//			}
//		});
//		StringBuilder sb = new StringBuilder();
//		int i = 0;
//		for (String aKey : keyList) {
//			if (i > 0) {
//				sb.append("&");
//			}
//			sb.append(aKey).append("=").append(paramMap.get(aKey));
//			i++;
//		}
//		String sourceStr = URLDecoder.decode(sb.toString(), "utf-8");
//		String mySighStr = DigestUtils.md5Hex(sourceStr);
//		// exist key
//		logger.info("sign str,sourceStr={},mySign={},destSign={}", sourceStr, mySighStr, signStr);
//		String responseStr = "";
//		if (mySighStr.equalsIgnoreCase(signStr)) {
//			responseStr = "{\"ret\":1}";
//			// get params
//			// String fromStr = paramMap.get("from");
//			String userid = paramMap.get("user_id");
//			String orderid = paramMap.get("jh_order_id");
//			int money = Integer.valueOf(paramMap.get("total_fee"));
//			int time = Integer.valueOf(paramMap.get("time"));
//			int ext1 = Integer.valueOf(paramMap.get("product_id"));
//			// int gameid = Integer.valueOf(paramMap.get("gameid"));
//			int serverid = Integer.valueOf(paramMap.get("server_id"));
//			// send to gs
//			ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
//			if (targetGs != null) {
//				String gsHostName = targetGs.getIp();
//				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
//						targetGs.getLanPort());
//				if (reconnectRet) {
//					LoginServer.getInstance().getLanClientManager().sendZyPayment(gsHostName, targetGs.getLanPort(),
//							userid, orderid, money, time, ext1,serverid);
//				}
//			}
//		} else {
//			responseStr = "{\"ret\":0}";
//		}
//		// ret
//		response.setContentType("text/html; charset=utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		PrintWriter out = response.getWriter();
//		out.print(responseStr);
//		baseRequest.setHandled(true);
//	}
//
//	private void sendResponse(HttpServletResponse response, Request baseRequest, String responseStr) {
//		response.setContentType("text/html; charset=utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.print(responseStr);
//		} catch (IOException e) {
//			logger.error("", e);
//		}
//		baseRequest.setHandled(true);
//	}
//
//}
