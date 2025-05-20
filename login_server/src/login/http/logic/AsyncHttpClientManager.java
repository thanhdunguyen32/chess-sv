//package login.http.logic;
//
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
//import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
//import org.apache.http.nio.conn.NHttpClientConnectionManager;
//import org.apache.http.nio.reactor.ConnectingIOReactor;
//import org.apache.http.nio.reactor.IOReactorException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class AsyncHttpClientManager {
//
//	private static Logger logger = LoggerFactory.getLogger(AsyncHttpClientManager.class);
//
//	private PoolingNHttpClientConnectionManager cm;
//
//	static class SingletonHolder {
//		static AsyncHttpClientManager instance = new AsyncHttpClientManager();
//	}
//
//	public static AsyncHttpClientManager getInstance() {
//		return SingletonHolder.instance;
//	}
//
//	public CloseableHttpAsyncClient getAsyncHttpClient() {
//		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
//		CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig)
//				.setConnectionManager(getPool()).build();
//		return httpclient;
//	}
//
//	private NHttpClientConnectionManager getPool() {
//		if (cm == null) {
//			ConnectingIOReactor ioReactor;
//			try {
//				ioReactor = new DefaultConnectingIOReactor();
//				cm = new PoolingNHttpClientConnectionManager(ioReactor);
//				cm.setDefaultMaxPerRoute(50);
//				cm.setMaxTotal(100);
//			} catch (IOReactorException e) {
//				logger.error("", e);
//			}
//
//		}
//		return cm;
//	}
//
//}
