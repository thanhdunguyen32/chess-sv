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

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import sdk.common.RSAUtils;
import sdk.garena.SDKConstants;

public class O185SyRhApi {

	private static Logger logger = LoggerFactory.getLogger(O185SyRhApi.class);

	static class SingletonHolder {
		static O185SyRhApi instance = new O185SyRhApi();
	}

	public static O185SyRhApi getInstance() {
		return SingletonHolder.instance;
	}

	public Map<String, Object> verifylogin(String userID, String token, String RH_AppSecret, String requestUrl) {
		// 生成sign
		String toSignParams = String.format("userID=%1$s&token=%2$s&key=%3$s", userID, token, RH_AppSecret);
		String signStr = DigestUtils.md5Hex(toSignParams);
		String requestParams = String.format("userID=%1$s&token=%2$s&sign=%3$s", userID, token, signStr);
		Map<String, Object> retMap = doHttpGet(requestUrl, requestParams);
		return retMap;
	}

	public Map<String, Object> getOrderId(String userId, int productId, String productName, String projectDesc,
			int money, int playerId, String roleName, int serverId, String serverName, String ec_pp, boolean isYueka,
			String appKey, String m_AppRSAPriKey, String callbackUrl, String zyUrlBase)
			throws UnsupportedEncodingException {
		String requestUrlPath = "/pay/getOrderID";
		// 发送请求参数
		String requestParams = String.format(
				"userID=%1$s&productID=%2$s&productName=%3$s&productDesc=%4$s&money=%5$s&roleID=%6$s&roleName=%7$s&serverID=%8$s&serverName=%9$s&cpPayCallBackUrl=%10$s&extension=%11$s&ec_pp=%12$s",
				userId, productId, productName, projectDesc, money, playerId, roleName, serverId, serverName,
				callbackUrl, isYueka ? "1" : "", ec_pp);
		// 生成sign
		String tempStr = requestParams + appKey;
		logger.info("source str:{}", tempStr);
		String encoded = URLEncoder.encode(tempStr, "UTF-8");
		String signtmp = RSAUtils.sign(encoded, m_AppRSAPriKey, "UTF-8");
		String sign = signtmp.replace("+", "%2B");
		String requestUrl = zyUrlBase + requestUrlPath;
		requestParams += "&sign=" + sign;
		Map<String, Object> retMap = doHttpPost(requestUrl, requestParams);
		logger.info("zy get order id,result:{}", retMap);
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
			} catch (Exception e2) {
				logger.error("关闭http请求出现异常！", e2);
			}
		}
		return null;
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
//		try {
//			Map<String, Object> retMap = YijieApi.getInstance().getOrderId("18", 1, "diamond001", "diamond001", 600, 5,
//					"herbert", 1, "xx", "", false, "f32fdc02123a82524eb4ea95e1383d0b",
//					"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAO0m9rBaOFCEj4ncScPeC+6H63XMHhs4xb08lR2TbthAPKIZV3jZB0cuh91M3XJcpdhlHUGbLhbWlmG5xKgN1Lt8Z+QoebfNEyyKM06I9YeDSykwRyEjhhOUgLjeIVV3NI8T/awhl+tb/0yyld+5aoXJKxOx/pzqolzoDRs0omEzAgMBAAECgYBGzwt5PHb0E6CIGS4tPW9ymULEuV2D4z+ncR9U5WCDUSrJe6eSfbqellYazYiRTPh31DkYDa2FRC1CoKUHSJnrjeNR2TMw0WUBFvNcqYe2qOJZg3iOhyUDhIChhQiWWC9VrzAvqSU6tuyKGMy5rAWbfTneEnL7NHsTgRRDC+0JAQJBAPlRGW6T4TnRBtbOpRcMU+jdCyJAK3zwuRO13alhexDLq105D1osg2uP1d3+XvTQudwCGo1qRfBSp/W72fynz5kCQQDzgmLyxGzO1rugtJNMLQTqsRGg8ZUoUPmsEVGbmnHwRzd2OGHWbT1JuIEEb+ivrZV3PfeEObv7fDAT6qIhyiarAkAcd4ka2iG+U0KfpkqtXgf6r7qEt6T/iBDp0js0CuBdY5P2efxpxGlhD7RQu6ml9Gs0Vr0nZnoD3bw1z7QtKBAJAkBiqBjesqZCxs0NtxtWaYbsbwDta/M6elQtWnbtzA0NhEz8IKvC7E9AZvgejBiB1JoRzZFSiPGYWiBAcXduqTAxAkEAqG24ePhjesKoF1Us2ViqgJC7zDd96v+LI5eausw3TfKjO4jj5oMoQiyc+hZFxHYlkyZRfA6XEraF1Rdgngf65w==",
//					"http://114.55.54.78:8081/zy_callback", "http://123.56.179.234:8085");
//			logger.info("ret={}", retMap);
//		} catch (UnsupportedEncodingException e) {
//			logger.error("", e);
//		}
		String atr;
		try {
			atr = URLEncoder.encode("YjZkYzlmNDNjM2MyM2I3ZjZjYTE3OGM3ZTk4OTA2YWQwZmNkZWRlMw==","UTF-8");
			System.out.println(atr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
