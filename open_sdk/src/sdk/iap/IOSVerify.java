package sdk.iap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;

public class IOSVerify {
	
	private static Logger logger = LoggerFactory.getLogger(IOSVerify.class);
	
	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static final String url_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt";
	private static final String url_verify = "https://buy.itunes.apple.com/verifyReceipt";

	/**
	 * 苹果服务器验证
	 * 
	 * @param receipt
	 *            账单
	 * @url 要验证的地址
	 * @return null 或返回结果 沙盒 https://sandbox.itunes.apple.com/verifyReceipt
	 * 
	 */
	public static String buyAppVerify(String receipt, Boolean isSanbox) {
		String url = url_verify;
		if (isSanbox) {
			url = url_sandbox;
		}
		//String buyCode = getBASE64(receipt);
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "text/json");
			conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

			//TODO change locale
			String str = String.format(Locale.ENGLISH, "{\"receipt-data\":\"" + receipt + "\"}");
			hurlBufOus.write(str.getBytes());
			hurlBufOus.flush();

			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return null;
	}

	/**
	 * 根据原始收据返回苹果的验证地址: * 沙箱 https://sandbox.itunes.apple.com/verifyReceipt 真正的地址
	 * https://buy.itunes.apple.com/verifyReceipt
	 * 
	 * @param receipt
	 * @return Sandbox 测试单 Real 正式单
	 */
	public static String getEnvironment(String receipt) {
		// try {
		// JSONObject job = JSONObject.fromObject(receipt);
		// if (job.containsKey("environment")) {
		// String evvironment = job.getString("environment");
		// return evvironment;
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		return "Real";
	}

	/**
	 * 用BASE64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getBASE64(String str) {
		byte[] b = str.getBytes();
		String s = null;
		if (b != null) {
//			s = new sun.misc.BASE64Encoder().encode(b);
		}
		return s;
	}

	/**
	 * 解密BASE64字窜
	 * 
	 * @param s
	 * @return
	 */
	public static String getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
//			BASE64Decoder decoder = new BASE64Decoder();
//			try {
//				b = decoder.decodeBuffer(s);
//				return new String(b);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return new String(b);
	}

	/**
	 * md5加密方法
	 * 
	 * @author: zhengsunlei Jul 30, 2010 4:38:28 PM
	 * @param plainText
	 *            加密字符串
	 * @return String 返回32位md5加密字符串(16位加密取substring(8,24)) 每位工程师都有保持代码优雅的义务 each engineer has a duty to keep the code
	 *         elegant
	 */
	public final static String md5(String plainText) {
		// 返回字符串
		String md5Str = null;
		try {
			// 操作字符串
			StringBuffer buf = new StringBuffer();
			/**
			 * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			 * 
			 * MessageDigest 对象开始被初始化。 该对象通过使用 update()方法处理数据。 任何时候都可以调用 reset()方法重置摘要。
			 * 一旦所有需要更新的数据都已经被更新了，应该调用digest()方法之一完成哈希计算。
			 * 
			 * 对于给定数量的更新数据，digest 方法只能被调用一次。 在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
			 */
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
			md.update(plainText.getBytes(StandardCharsets.UTF_8));
			// 计算出摘要,完成哈希计算。
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				// 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
				buf.append(Integer.toHexString(i));
			}
			// 32位的加密
			md5Str = buf.toString();
			// 16位的加密
			// md5Str = buf.toString().md5Strstring(8,24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String receiptStr = "ewoJInNpZ25hdHVyZSIgPSAiQWdHUzhxMWZXeVhDV0cyY2h6dlY4MG9pUnFRcjA2Nk10ZVpvZ2dRaEdtbFhQTDBmS2k0QzVLb1p5L3AxaFFqSWRQTmduNURPWFRRaktxaW1CQ2FwNFpTMHVHNEcva20yeGZaT1VLMENSMUpWRDNveUNtODlYRGlSRmt3dTRlN1Q4NzRmZ1hZKzdKRzN2YUFIRzBrZmE3L1BOR1RLWlVnTUFJZnA0V3A0U1QySkFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NCdXA0K1BBaG0vTE1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEUwTURZd056QXdNREl5TVZvWERURTJNRFV4T0RFNE16RXpNRm93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNbVRFdUxnamltTHdSSnh5MW9FZjBlc1VORFZFSWU2d0Rzbm5hbDE0aE5CdDF2MTk1WDZuOTNZTzdnaTNvclBTdXg5RDU1NFNrTXArU2F5Zzg0bFRjMzYyVXRtWUxwV25iMzRucXlHeDlLQlZUeTVPR1Y0bGpFMU93QytvVG5STStRTFJDbWVOeE1iUFpoUzQ3VCtlWnRERWhWQjl1c2szK0pNMkNvZ2Z3bzdBZ01CQUFHamNqQndNQjBHQTFVZERnUVdCQlNKYUVlTnVxOURmNlpmTjY4RmUrSTJ1MjJzc0RBTUJnTlZIUk1CQWY4RUFqQUFNQjhHQTFVZEl3UVlNQmFBRkRZZDZPS2RndElCR0xVeWF3N1hRd3VSV0VNNk1BNEdBMVVkRHdFQi93UUVBd0lIZ0RBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQWVhSlYyVTUxcnhmY3FBQWU1QzIvZkVXOEtVbDRpTzRsTXV0YTdONlh6UDFwWkl6MU5ra0N0SUl3ZXlOajVVUllISytIalJLU1U5UkxndU5sMG5rZnhxT2JpTWNrd1J1ZEtTcTY5Tkluclp5Q0Q2NlI0Szc3bmI5bE1UQUJTU1lsc0t0OG9OdGxoZ1IvMWtqU1NSUWNIa3RzRGNTaVFHS01ka1NscDRBeVhmN3ZuSFBCZTR5Q3dZVjJQcFNOMDRrYm9pSjNwQmx4c0d3Vi9abEwyNk0ydWVZSEtZQ3VYaGRxRnd4VmdtNTJoM29lSk9PdC92WTRFY1FxN2VxSG02bTAzWjliN1BSellNMktHWEhEbU9Nazd2RHBlTVZsTERQU0dZejErVTNzRHhKemViU3BiYUptVDdpbXpVS2ZnZ0VZN3h4ZjRjemZIMHlqNXdOelNHVE92UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUyTFRBekxUTXdJREU0T2pVNU9qQXpJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0k1TWpkbE5HVXlObUZsWWpJM1pETmlaV1UzTTJVek1tRTFPVEl6T0RNNFlqUXpNbVZoTnpnMElqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREl3TWpVek5qRTNOU0k3Q2draVluWnljeUlnUFNBaU1TNHdJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURJd01qVXpOakUzTlNJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFME5Ua3pPRGsxTkRNeE5EWWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaU9ERXdORUkxTmpZdE16QkdNQzAwUkVNeExUaENORFV0UkVOQk1EY3dNMEkwTkRRM0lqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSmpiaTU0ZUdOaGFXWjFMbVJwWVcxdmJtUXdNREVpT3dvSkltbDBaVzB0YVdRaUlEMGdJakV3T1Rnd056Z3lPVEFpT3dvSkltSnBaQ0lnUFNBaVkyOXRMbmg0WTJGcFpuVXVaMkZ0WlNJN0Nna2ljSFZ5WTJoaGMyVXRaR0YwWlMxdGN5SWdQU0FpTVRRMU9UTTRPVFUwTXpFME5pSTdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTSWdQU0FpTWpBeE5pMHdNeTB6TVNBd01UbzFPVG93TXlCRmRHTXZSMDFVSWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFhCemRDSWdQU0FpTWpBeE5pMHdNeTB6TUNBeE9EbzFPVG93TXlCQmJXVnlhV05oTDB4dmMxOUJibWRsYkdWeklqc0tDU0p2Y21sbmFXNWhiQzF3ZFhKamFHRnpaUzFrWVhSbElpQTlJQ0l5TURFMkxUQXpMVE14SURBeE9qVTVPakF6SUVWMFl5OUhUVlFpT3dwOSI7CgkiZW52aXJvbm1lbnQiID0gIlNhbmRib3giOwoJInBvZCIgPSAiMTAwIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=";
		String retStr = buyAppVerify(receiptStr, true);
		System.out.println(retStr);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> job;
		job = mapper.readValue(retStr, Map.class);
		Integer states = (Integer) job.get("status");
		if (states== 0)// 验证成功
		{
			Map<String, Object> returnJson = (Map<String, Object>) job.get("receipt");
			// 产品ID TODO 判断是否是我们产品
			String product_id = (String) returnJson.get("product_id");
			// 数量
			String quantity = (String) returnJson.get("quantity");
			// TODO 判断Transaction Identifier是否已经存在
			String transaction_id = (String) returnJson.get("transaction_id");
			// 交易日期
			String purchase_date = (String) returnJson.get("purchase_date");
			
			logger.info("retval,product_id={},quantity={},transaction_id={},purchase_date={}",product_id,quantity,transaction_id,purchase_date);
		}
	}
}
