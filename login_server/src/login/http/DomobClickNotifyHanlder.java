//package login.http;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
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
//import login.dao.LogDomobIdfaDao;
//
//public class DomobClickNotifyHanlder extends AbstractHandler {
//
//	public static final Logger logger = LoggerFactory.getLogger(DomobClickNotifyHanlder.class);
//
//	static class SingletonHolder {
//		static DomobClickNotifyHanlder instance = new DomobClickNotifyHanlder();
//	}
//
//	public static DomobClickNotifyHanlder getInstance() {
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
//		logger.info("domob click ad notify,request params={}", queryStr);
//		Map<String, String> paramMap = extractParameters(queryStr);
//		final String appkey = paramMap.get("appkey");
//		final String ifa = paramMap.get("ifa");
//		final String ifamd5 = paramMap.get("ifamd5");
//		if (StringUtils.isEmpty(ifa) && StringUtils.isEmpty(ifamd5)) {
//			sendResponse(response, baseRequest, "{'message':'param null'，'success':false}");
//			return;
//		}
//		// 保存到数据库
//		LoginServer.executorService.execute(new Runnable() {
//			public void run() {
//				LogDomobIdfaDao.getInstance().addLogIdfa(appkey, ifa, ifamd5);
//			}
//		});
//		String responseStr = "{'message':'OK'，'success':true}";
//		// ret
//		sendResponse(response, baseRequest, responseStr);
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
