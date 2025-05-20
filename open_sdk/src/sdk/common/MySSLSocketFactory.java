package sdk.common;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import sdk.https.MyX509TrustManager;

public class MySSLSocketFactory {

	private SSLContext sslContext = null;
	
	static class SingletonHolder {
		static MySSLSocketFactory instance = new MySSLSocketFactory();
	}

	public static MySSLSocketFactory getInstance() {
		return SingletonHolder.instance;
	}

	private static SSLContext createEasySSLContext() {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private SSLContext getSSLContext() {
		if (this.sslContext == null) {
			this.sslContext = createEasySSLContext();
		}
		return this.sslContext;
	}

	public SSLSocketFactory getSSLSocketFactory() {
		return getSSLContext().getSocketFactory();
	}

}
