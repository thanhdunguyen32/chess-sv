package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpHtmlHandler;
import login.LoginServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sdk.garena.SDKConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpForwardHandler extends HttpHtmlHandler {

	private static Logger logger = LoggerFactory.getLogger(HttpForwardHandler.class);
	
	private String fowardUrl;
	
	public HttpForwardHandler(String name, String forwardUrl) {
		super(name);
		this.fowardUrl = forwardUrl;
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception {
		logger.info("forward,url={},content={}",fowardUrl,content);
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				doHttpPost(fowardUrl, content);
			}
		});
		return "result=OK&resultMsg=成功";
	}

	private void doHttpPost(String requestUrl, String requestParams) {
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
	}

}
