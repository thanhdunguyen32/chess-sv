//package login.http;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TreeMap;
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
//import login.http.logic.Xiao7VerifyUtil;
//import login.logic.ServerListManager;
//
//public class DouyuPayCallbackHanlder extends AbstractHandler {
//
//	public static final Logger logger = LoggerFactory.getLogger(DouyuPayCallbackHanlder.class);
//
//	static class SingletonHolder {
//		static DouyuPayCallbackHanlder instance = new DouyuPayCallbackHanlder();
//	}
//
//	public static DouyuPayCallbackHanlder getInstance() {
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
//		Map<String, String[]> queryStr = request.getParameterMap();
//		logger.info("douyu paymanet callback,request params={}", queryStr);
//		Map<String, String> paramMap = extractParameters(queryStr);
//		logger.info("douyu paymanet callback,extract params={}", paramMap);
//		String signStr = paramMap.get("sign");
//		paramMap.remove("sign");
//		String sourceStr = Xiao7VerifyUtil.buildHttpQuery(paramMap);
//		String responseStr = "failed";
//		try {
//			// 验签
//			String payKey = "55f65b400b757b045aca281fd2ed4728";
//			logger.info("douyu verify,source={},sign={}", sourceStr, signStr);
//			String mySign = DigestUtils.md5Hex(sourceStr + payKey);
//			if (!signStr.equalsIgnoreCase(mySign)) {
//				responseStr = "failed";
//			} else {
//				responseStr = "OK";
//				String userid = paramMap.get("uin");
//				String amountStr = paramMap.get("amount");
//				String orderid = paramMap.get("order_id");
//				String extra_param = paramMap.get("extra_param");
//				int money = Float.valueOf(amountStr).intValue();// moneu以元为单位
//				money = money * 100;// 转化成分
//				// 参数解压
//				String[] extraStrs = StringUtils.split(extra_param, "_");
//				int ext1 = Integer.valueOf(extraStrs[0]);
//				int serverid = Integer.valueOf(extraStrs[1]);
//				// send to gs
//				ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
//				if (targetGs != null) {
//					String gsHostName = targetGs.getIp();
//					boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
//							targetGs.getLanPort());
//					if (reconnectRet) {
//						int time = (int) (System.currentTimeMillis() / 1000);
//						LoginServer.getInstance().getLanClientManager().sendZyPayment(gsHostName, targetGs.getLanPort(),
//								userid, orderid, money, time, ext1, serverid);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//		// ret
//		sendResponse(response, baseRequest, responseStr);
//	}
//
//	private Map<String, String> extractParameters(Map<String, String[]> queryStr) {
//		Map<String, String> retMap = new TreeMap<>();
//		for (Map.Entry<String, String[]> aMapEntry : queryStr.entrySet()) {
//			retMap.put(aMapEntry.getKey(), aMapEntry.getValue()[0]);
//		}
//		return retMap;
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
