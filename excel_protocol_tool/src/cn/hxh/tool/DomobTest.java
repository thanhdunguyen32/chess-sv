package cn.hxh.tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomobTest {

	private static Logger logger = LoggerFactory.getLogger(DomobTest.class);

	public static void main(String[] args) {
		String appKey = "1120075190";
		String idfaStr = "506BD7AB-7481-4338-88C4-A99C35B55F5B";
		String ipAddr = "116.226.123.131";
		int acttime = (int) (System.currentTimeMillis() / 1000);
		int acttype = 2;
		int returnFormat = 1;
		String macStr = "";
		String macmd5 = "";
		String idfaMd5 = "";
		String sign_key = "1ae2fb13f75310baeae8b9a720238ae4";
		logger.info("domob sign key={}", sign_key);
		String s = appKey + "," + macStr + "," + macmd5 + "," + idfaStr + "," + idfaMd5 + "," + sign_key;
		String signStr = DigestUtils.md5Hex(s);
		// 发送get请求
		String requestUrl = "http://e.domob.cn/track/ow/api/callback";
		String requestParams = String.format(
				"appkey=%1$s&ifa=%2$s&acttime=%3$d&acttype=%4$d&returnFormat=%5$d&sign=%6$s&actip=%7$s", appKey,
				idfaStr, acttime, acttype, returnFormat, signStr, ipAddr);
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			URL realUrl = new URL(requestUrl + "?" + requestParams);

			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
			logger.info("domob active idfa result={}", result);
		} catch (Exception e) {
			logger.error("发送GET请求出现异常！", e);
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
	}

}
