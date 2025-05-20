package sdk.zy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import sdk.garena.SDKConstants;

/**
 * 上海典游IOS sdk
 *
 */
public class YunbeeApi {

	private static Logger logger = LoggerFactory.getLogger(YunbeeApi.class);

	static class SingletonHolder {
		static YunbeeApi instance = new YunbeeApi();
	}

	public static YunbeeApi getInstance() {
		return SingletonHolder.instance;
	}

	public Map<String, Object> verifylogin(String userId, String token, String requestUrl) {
		String requestParams = String.format("{\"user_id\":\"%1$s\",\"token\":\"%2$s\"}",userId, token);
		Map<String, Object> retMap = doHttpPost(requestUrl, requestParams);
		return retMap;
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
			connection.setRequestProperty("Content-Type", "application/json");
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
		
	}

}
