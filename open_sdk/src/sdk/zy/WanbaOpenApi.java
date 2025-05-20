package sdk.zy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import sdk.common.MySSLSocketFactory;
import sdk.garena.SDKConstants;

public class WanbaOpenApi {

	private static Logger logger = LoggerFactory.getLogger(WanbaOpenApi.class);

	static class SingletonHolder {
		static WanbaOpenApi instance = new WanbaOpenApi();
	}

	public static WanbaOpenApi getInstance() {
		return SingletonHolder.instance;
	}
	
	public Map<String, Object> checkLogin(String openid, String openkey, String appid, String pf,
			String queryUrlBase, String appKey) throws UnsupportedEncodingException {
		String queryPath = "/v3/user/is_login";
		SortedMap<String, String> map = new TreeMap<>();
		map.put("appid", appid);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("pf", pf);
		map.put("format", "json");
		map.put("userip", "");
		// 生成签名字符串
		StringBuilder sb = new StringBuilder(100);
		int i = 0;
		for (Entry<String, String> pair : map.entrySet()) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(pair.getKey() + "=" + pair.getValue());
			i++;
		}
		String queryStr = sb.toString();
		logger.info("to sign string={}", queryStr);
		String eocodedSignStr = "POST&"+URLEncoder.encode(queryPath, "UTF-8")+"&"+URLEncoder.encode(queryStr, "UTF-8");
		byte[] mysigeByte = HmacUtils.hmacSha1(appKey, eocodedSignStr);
		String mySign = sdk.common.Base64.encode(mysigeByte);
		queryStr += "&sig=" + URLEncoder.encode(mySign, "UTF-8");
		
		Map<String, Object> retMap = doHttpsPost(queryUrlBase + queryPath, queryStr);
		return retMap;
	}

	/**
	 * 
	 * 获取玩家基本信息
	 */
	public Map<String, Object> getLoginInfo(String openid, String openkey, String appid, String pf,
			String queryUrlBase, String appKey) throws UnsupportedEncodingException {
		String queryPath = "/v3/user/get_info";
		SortedMap<String, String> map = new TreeMap<>();
		map.put("appid", appid);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("pf", pf);
		map.put("format", "json");
		map.put("userip", "");
		// 生成签名字符串
		StringBuilder sb = new StringBuilder(100);
		int i = 0;
		for (Entry<String, String> pair : map.entrySet()) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(pair.getKey() + "=" + pair.getValue());
			i++;
		}
		String queryStr = sb.toString();
		logger.info("to sign string={}", queryStr);
		String eocodedSignStr = "POST&"+URLEncoder.encode(queryPath, "UTF-8")+"&"+URLEncoder.encode(queryStr, "UTF-8");
		byte[] mysigeByte = HmacUtils.hmacSha1(appKey, eocodedSignStr);
		String mySign = sdk.common.Base64.encode(mysigeByte);
		queryStr += "&sig=" + URLEncoder.encode(mySign, "UTF-8");
		
		Map<String, Object> retMap = doHttpsPost(queryUrlBase + queryPath, queryStr);
		return retMap;
	}
	
	public Map<String, Object> getUserInfo(String openid, String openkey, String appid, String pf,int zoneid,
			String queryUrlBase, String appKey) throws UnsupportedEncodingException {
		String queryPath = "/v3/user/get_playzone_userinfo";
		SortedMap<String, String> map = new TreeMap<>();
		map.put("appid", appid);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("pf", pf);
		map.put("zoneid", String.valueOf(zoneid));
		map.put("format", "json");
		map.put("userip", "");
		// 生成签名字符串
		StringBuilder sb = new StringBuilder(100);
		int i = 0;
		for (Entry<String, String> pair : map.entrySet()) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(pair.getKey() + "=" + pair.getValue());
			i++;
		}
		String queryStr = sb.toString();
		logger.info("to sign string={}", queryStr);
		String eocodedSignStr = "POST&"+URLEncoder.encode(queryPath, "UTF-8")+"&"+URLEncoder.encode(queryStr, "UTF-8");
		byte[] mysigeByte = HmacUtils.hmacSha1(appKey, eocodedSignStr);
		String mySign = sdk.common.Base64.encode(mysigeByte);
		queryStr += "&sig=" + URLEncoder.encode(mySign, "UTF-8");
		
		Map<String, Object> retMap = doHttpsPost(queryUrlBase + queryPath, queryStr);
		return retMap;
	}
	
	public Map<String, Object> buyItem(String openid, String openkey, String appid, String pf,String zoneid,String billno,String itemid,
			String queryUrlBase, String appKey) throws UnsupportedEncodingException {
		String queryPath = "/v3/user/buy_playzone_item";
		SortedMap<String, String> map = new TreeMap<>();
		map.put("appid", appid);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("pf", pf);
		map.put("zoneid", zoneid);
		map.put("billno", billno);
		map.put("itemid", itemid);
		map.put("format", "json");
		map.put("userip", "");
		// 生成签名字符串
		StringBuilder sb = new StringBuilder(100);
		int i = 0;
		for (Entry<String, String> pair : map.entrySet()) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(pair.getKey() + "=" + pair.getValue());
			i++;
		}
		String queryStr = sb.toString();
		logger.info("to sign string={}", queryStr);
		String eocodedSignStr = "POST&"+URLEncoder.encode(queryPath, "UTF-8")+"&"+URLEncoder.encode(queryStr, "UTF-8");
		byte[] mysigeByte = HmacUtils.hmacSha1(appKey, eocodedSignStr);
		String mySign = sdk.common.Base64.encode(mysigeByte);
		queryStr += "&sig=" + URLEncoder.encode(mySign, "UTF-8");

		Map<String, Object> retMap = doHttpsPost(queryUrlBase + queryPath, queryStr);
		return retMap;
	}
	
	public Map<String, Object> giftExchange(String openid, String openkey, String appid, String pf,
			String queryUrlBase, String appKey, String gift_id) throws UnsupportedEncodingException {
		String queryPath = "/v3/user/gift_exchange";
		SortedMap<String, String> map = new TreeMap<>();
		map.put("appid", appid);
		map.put("openid", openid);
		map.put("openkey", openkey);
		map.put("pf", pf);
		map.put("gift_id", gift_id);
		map.put("format", "json");
		map.put("userip", "");
		// 生成签名字符串
		StringBuilder sb = new StringBuilder(100);
		int i = 0;
		for (Entry<String, String> pair : map.entrySet()) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(pair.getKey() + "=" + pair.getValue());
			i++;
		}
		String queryStr = sb.toString();
		logger.info("to sign string={}", queryStr);
		String eocodedSignStr = "POST&"+URLEncoder.encode(queryPath, "UTF-8")+"&"+URLEncoder.encode(queryStr, "UTF-8");
		byte[] mysigeByte = HmacUtils.hmacSha1(appKey, eocodedSignStr);
		String mySign = sdk.common.Base64.encode(mysigeByte);
		queryStr += "&sig=" + URLEncoder.encode(mySign, "UTF-8");
		
		Map<String, Object> retMap = doHttpsPost(queryUrlBase + queryPath, queryStr);
		return retMap;
	}

	private Map<String, Object> doHttpsPost(String requestUrl, String requestParams) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			SSLSocketFactory ssf = MySSLSocketFactory.getInstance().getSSLSocketFactory();

			URL realUrl = new URL(requestUrl);
			logger.info("open connection!,url={}://{}{}?{}", realUrl.getProtocol(), realUrl.getHost(),
					realUrl.getPath(), realUrl.getQuery());
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
		WanbaOpenApi wanbaOpenApi = WanbaOpenApi.getInstance();
		try {
			Map<String, Object> retMap = wanbaOpenApi.checkLogin("EEAAE1EA94A6DAD6B40472AA436578E6",
					"612A1F10DC1BD5ACB81347532E58EDD0", "1106644446", "wanba_ts","https://api.urlshare.cn",
					"rAXRn6TTAcGVId2j&");
			logger.info("ret={}", retMap);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
