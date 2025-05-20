package com.swiftpass.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swiftpass.config.SwiftpassConfig;
import com.swiftpass.util.MD5;
import com.swiftpass.util.SignUtils;
import com.swiftpass.util.XmlUtils;

import sdk.common.MySSLSocketFactory;
import sdk.garena.SDKConstants;

public class SwiftpassOpenApi {

	private static Logger logger = LoggerFactory.getLogger(SwiftpassOpenApi.class);

	static class SingletonHolder {
		static SwiftpassOpenApi instance = new SwiftpassOpenApi();
	}

	public static SwiftpassOpenApi getInstance() {
		return SingletonHolder.instance;
	}

	public Map<String, String> prePay(String orderId, String clientIp, String productName, int totalFee, int playerId, int plat_id) {

		SortedMap<String, String> map = new TreeMap<>();

		String deviceInfo = "AND_SDK";
		if (plat_id == 3) {
			deviceInfo = "iOS_SDK";
		}

		map.put("mch_id", SwiftpassConfig.mch_id);
		map.put("attach", String.valueOf(playerId));

		map.put("callback_url", "wx1e4011757923db11://");
		map.put("service", "unified.trade.pay");
		map.put("mch_create_ip", clientIp);
		map.put("notify_url", SwiftpassConfig.notify_url);
		map.put("nonce_str", String.valueOf(new Date().getTime()));
		map.put("device_info", deviceInfo);// 应用类型
		map.put("mch_app_name", "闽闽棋牌");// 应用名 Spad
		map.put("mch_app_id", "com.guanqu.minmin");// 应用标识 com.pass.pay
		map.put("body", productName);
		map.put("total_fee", String.valueOf(totalFee));
		map.put("out_trade_no", orderId);
		Map<String, String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
		SignUtils.buildPayParams(buf, params, false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
		map.put("sign", sign);

		String reqUrl = SwiftpassConfig.req_url;
		logger.info("reqUrl：{}", reqUrl);

		String reqParams = XmlUtils.parseXML(map);
		Map<String, String> retMap = doHttpsPost(reqUrl, reqParams);
		return retMap;
	}

	private Map<String, String> doHttpsPost(String requestUrl, String requestParams) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			SSLSocketFactory ssf = MySSLSocketFactory.getInstance().getSSLSocketFactory();

			URL realUrl = new URL(requestUrl);
			logger.info("open connection!,url={}://{}{}?{}", realUrl.getProtocol(), realUrl.getHost(), realUrl.getPath(), realUrl.getQuery());
			// 打开和URL之间的连接
			HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
			connection.setSSLSocketFactory(ssf);
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
			logger.info("send request,url={}://{}:{}{},requestParams={}", realUrl.getProtocol(), realUrl.getHost(), realUrl.getPort(), realUrl.getPath(), requestParams);
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			Map<String, String> resultMap = XmlUtils.toMap(result, "utf-8");
			Map<String, String> retMap = new HashMap<>();
			logger.info("请求结果：{}", result);
			retMap.put("status", resultMap.get("status"));
			if (resultMap.containsKey("sign")) {
				if (SignUtils.checkParam(resultMap, SwiftpassConfig.key)) {
					if ("0".equals(resultMap.get("status"))) {
						retMap.put("token_id", resultMap.get("token_id"));
						retMap.put("services", resultMap.get("services"));
					}
				} else {
					logger.warn("md5 not match!");
				}
			}
			return retMap;
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

	public static void main(String[] args) {
		SwiftpassOpenApi swiftpassOpenApi = new SwiftpassOpenApi();
		SwiftpassConfig.key = "7daa4babae15ae17eee90c9e";
		SwiftpassConfig.mch_id = "755437000006";
		SwiftpassConfig.req_url = "https://pay.swiftpass.cn/pay/gateway";
		SwiftpassConfig.notify_url = "http://115.231.26.9:8086/swiftpass";
		String orderId = RandomStringUtils.randomAlphanumeric(32);
		String clientIp = "115.231.26.9";
		Map<String, String> retMap = swiftpassOpenApi.prePay(orderId, clientIp, "道具1", 1000, 10086, 1);
		logger.info("ret={}", retMap);
	}

}
