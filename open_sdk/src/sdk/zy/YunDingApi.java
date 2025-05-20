package sdk.zy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import sdk.garena.SDKConstants;

public class YunDingApi {

	private static Logger logger = LoggerFactory.getLogger(YunDingApi.class);

	static class SingletonHolder {
		static YunDingApi instance = new YunDingApi();
	}

	public static YunDingApi getInstance() {
		return SingletonHolder.instance;
	}

	public Map<String, Object> verifylogin(String urlBase, int v, int gact, int appid, int userid, String usertoken) {
		String requestUrlPath = urlBase + "/gact.aspx";
		try {
			String encodedUsertoken = URLEncoder.encode(usertoken, "UTF-8");
			String requestParams = String.format("v=%1$d&gact=%2$d&userid=%3$d&usertoken=%4$s&appid=%5$d", v, gact,
					userid, encodedUsertoken, appid);
			Map<String, Object> retMap = doHttpGet(requestUrlPath, requestParams);
			return retMap;
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
			return null;
		}
	}

	public Map<String, Object> rechargeReport(String urlBase, int gact, int appid, String apple_order, String userid,
			String nickname, String area_server, int price, int paytime, String ip)
			throws UnsupportedEncodingException {
		String requestUrlPath = urlBase + "/gact.aspx";
		// 发送请求参数
		String requestParams = String.format(
				"gact=%1$d&appid=%2$d&apple_order=%3$s&userid=%4$s&nickname=%5$s&area_server=%6$s&price=%7$d&currency=1&paystatus=1&paytime=%8$d&ip=%9$s",
				gact, appid, apple_order, userid, URLEncoder.encode(nickname, "UTF-8"),
				URLEncoder.encode(area_server, "UTF-8"), price, paytime, ip);
		// 生成sign
		Map<String, Object> retMap = doHttpPost(requestUrlPath, requestParams);
		logger.info("yunding recharge report,result:{}", retMap);
		if (retMap == null) {
			return null;
		}
		if (retMap.get("state") == null || (Integer) (retMap.get("state")) != 1) {
			return null;
		}
		Object dataObj = retMap.get("data");
		if (dataObj == null) {
			return null;
		}
		return (Map<String, Object>) dataObj;
	}

	private Map<String, Object> doHttpPost(String requestUrl, String requestParams) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			URL realUrl = new URL(requestUrl);

			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setConnectTimeout(SDKConstants.CONNECTION_TIMEOUT);
			connection.setReadTimeout(SDKConstants.CONNECTION_TIMEOUT);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(connection.getOutputStream());
			// 发送请求参数
			out.print(requestParams);
			// flush输出流的缓冲
			out.flush();

			// 建立实际的连接
			connection.connect();
			logger.info("send request,url={}://{}:{}{},requestParams={}", realUrl.getProtocol(), realUrl.getHost(),
					realUrl.getPort(), realUrl.getPath(), requestParams);
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			return extractPostParams(result);
		} catch (Exception e) {
			logger.error("发送POST请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				logger.error("关闭http请求出现异常！", e2);
			}
		}
		return null;
	}

	private Map<String, Object> doHttpGet(String requestUrlPath, String requestParams) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(requestUrlPath + "?" + requestParams);
			logger.info("open connection!,url={}://{}{}", realUrl.getProtocol(), realUrl.getHost(), realUrl.getPath());
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setConnectTimeout(SDKConstants.CONNECTION_TIMEOUT);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// GET请求
			connection.setRequestMethod("GET");
			// 建立实际的连接
			connection.connect();
			logger.info("send request,url={}://{}{}", realUrl.getProtocol(), realUrl.getHost(), realUrl.getPath());
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			return extractPostParams(result);
		} catch (Exception e) {
			logger.error("发送POST请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error("关闭http请求出现异常！", e2);
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> extractPostParams(String queryString) {
		// URLDecoder.decode(queryString, "UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		Map quizAnswer = null;
		try {
			quizAnswer = mapper.readValue(queryString, Map.class);
		} catch (IOException e) {
			logger.error("", e);
		}
		return quizAnswer;
	}

	public static void main(String[] args) {
		// Map<String, Object> retMap = ZyApi.getInstance().verifyLogin("18",
		// "7d62f54f707a4026e25b3dead0bea2e1",
		// "f32fdc02123a82524eb4ea95e1383d0b", "http://123.56.179.234:8085");
		// logger.info("ret={}", retMap);
		// try {
		// Map<String, Object> retMap =
		// YunDingApi.getInstance().getOrderId("18", 1, "diamond001",
		// "diamond001", 600,
		// 5, "herbert", 1, "xx", "", false, "f32fdc02123a82524eb4ea95e1383d0b",
		// "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO0m9rBaOFCEj4ncScPeC+6H63XMHhs4xb08lR2TbthAPKIZV3jZB0cuh91M3XJcpdhlHUGbLhbWlmG5xKgN1Lt8Z+QoebfNEyyKM06I9YeDSykwRyEjhhOUgLjeIVV3NI8T/awhl+tb/0yyld+5aoXJKxOx/pzqolzoDRs0omEzAgMBAAECgYBGzwt5PHb0E6CIGS4tPW9ymULEuV2D4z+ncR9U5WCDUSrJe6eSfbqellYazYiRTPh31DkYDa2FRC1CoKUHSJnrjeNR2TMw0WUBFvNcqYe2qOJZg3iOhyUDhIChhQiWWC9VrzAvqSU6tuyKGMy5rAWbfTneEnL7NHsTgRRDC+0JAQJBAPlRGW6T4TnRBtbOpRcMU+jdCyJAK3zwuRO13alhexDLq105D1osg2uP1d3+XvTQudwCGo1qRfBSp/W72fynz5kCQQDzgmLyxGzO1rugtJNMLQTqsRGg8ZUoUPmsEVGbmnHwRzd2OGHWbT1JuIEEb+ivrZV3PfeEObv7fDAT6qIhyiarAkAcd4ka2iG+U0KfpkqtXgf6r7qEt6T/iBDp0js0CuBdY5P2efxpxGlhD7RQu6ml9Gs0Vr0nZnoD3bw1z7QtKBAJAkBiqBjesqZCxs0NtxtWaYbsbwDta/M6elQtWnbtzA0NhEz8IKvC7E9AZvgejBiB1JoRzZFSiPGYWiBAcXduqTAxAkEAqG24ePhjesKoF1Us2ViqgJC7zDd96v+LI5eausw3TfKjO4jj5oMoQiyc+hZFxHYlkyZRfA6XEraF1Rdgngf65w==",
		// "http://114.55.54.78:8081/zy_callback",
		// "http://123.56.179.234:8085");
		// logger.info("ret={}", retMap);
		// } catch (UnsupportedEncodingException e) {
		// logger.error("", e);
		// }
	}

}
