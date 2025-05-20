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
//import login.logic.Xiao7PayListManager;
//import login.logic.Xiao7PayListManager.Xiao7PayEntity;
//
//public class Xiao7PayCallbackHanlder extends AbstractHandler {
//
//	public static final Logger logger = LoggerFactory.getLogger(Xiao7PayCallbackHanlder.class);
//
//	static class SingletonHolder {
//		static Xiao7PayCallbackHanlder instance = new Xiao7PayCallbackHanlder();
//	}
//
//	public static Xiao7PayCallbackHanlder getInstance() {
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
//		Map<String,String[]> queryStr = request.getParameterMap();
//		logger.info("xiao7 paymanet callback,request params={}", queryStr);
//		Map<String, String> paramMap = extractParameters(queryStr);
//		logger.info("xiao7 paymanet callback,extract params={}", paramMap);
//		String signStr = paramMap.get("sign_data");
//		paramMap.remove("sign_data");
//		String sourceStr = Xiao7VerifyUtil.buildHttpQuery(paramMap);
//		String responseStr = "failed";
//		try {
//			// 验签
//			logger.info("xiap7 verify,source={},sign={}", sourceStr, signStr);
//			if (!Xiao7VerifyUtil.doCheck(sourceStr, signStr, Xiao7VerifyUtil.loadPublicKeyByStr())) {
//				responseStr = "failed";
//			} else {
//				// 解密
//				String decryptData = new String(Xiao7VerifyUtil.decrypt(Xiao7VerifyUtil.loadPublicKeyByStr(),
//						Xiao7VerifyUtil.decode(paramMap.get("encryp_data"))));
//				Map<String, String> decryptMap = Xiao7VerifyUtil.decodeHttpQuery(decryptData);
//				// 这里是比较解密后的订单号与我们通过POST传递过来的订单号是否一致
//				if (decryptMap.containsKey("game_orderid")
//						&& decryptMap.get("game_orderid").equals(paramMap.get("game_orderid"))) {
//					/***********************************************************************************************************************************/
//					// 如果解密后比较订单号一致的话，需要通过当前订单号查找数据库，判断这个订单号对应的订单金额和用户guid是否与解密后或者传递过来的内容一致
//					// 用户的guid是通过POST直接传递过来，而订单金额是上面解密后得到的。
//					// 通过POST传递过来的guid与通过订单号在服务器中查找的订单对应的guid相同
//					// 解密后得到的订单金额pay与通过订单号在服务器中查找的订单对应的订单金额相同
//					// .....................................................
//					// 麻烦各位游戏厂商的技术朋友实现一下，如果一切都匹配就返回success
//					/**********************************************************************************************************************************/
//					responseStr = "success";
//					String userid = paramMap.get("guid");
//					String orderid = decryptMap.get("game_orderid");
//					int money = Float.valueOf(decryptMap.get("pay")).intValue();//moneu以元为单位
//					money = money * 100;// 转化成分
////					int ext1 = Integer.valueOf(paramMap.get("product_id"));
//					//判断订单是否存在
//					Xiao7PayEntity xiao7PayEntity = Xiao7PayListManager.getInstance()
//							.getPayEntity(Long.valueOf(orderid));
//					if (xiao7PayEntity == null) {
//						logger.info("order not exist,orderid={}", orderid);
//						sendResponse(response, baseRequest, "failed");
//						return;
//					}
//					int ext1 = xiao7PayEntity.productId;
//					int serverid = xiao7PayEntity.serverId;
//					// send to gs
//					ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
//					if (targetGs != null) {
//						String gsHostName = targetGs.getIp();
//						boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
//								targetGs.getLanPort());
//						if (reconnectRet) {
//							int time = (int)(System.currentTimeMillis()/1000);
//							LoginServer.getInstance().getLanClientManager().sendZyPayment(gsHostName,
//									targetGs.getLanPort(), userid, orderid, money, time, ext1, serverid);
//							Xiao7PayListManager.getInstance().remove(Long.valueOf(orderid));
//						}
//					}
//				} else {
//					responseStr = "failed";
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
