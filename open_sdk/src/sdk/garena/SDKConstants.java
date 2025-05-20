package sdk.garena;

public class SDKConstants {

	public static final int CONNECTION_TIMEOUT = 5000;

	public static final String LIVE_URL_BASE = "https://connect.garena.com";

	public static final String SANDBOX_URL_BASE = "https://testconnect.garena.com";
	
	public static final String LIVE_BEETALK_BASE = "http://beepost.beetalkmobile.com";
	
	public static final String SANDBOX_BEETALK_BASE = "http://testbeepost.beetalkmobile.com";

	public static final int DEFAULT_APP_SERVER_ID = 0;

	public static final int DEFAULT_APP_ROLE_ID = 0;
	
	public static final int GARENA_APP_ID = 100024;
	
	public static final String GARENA_PAYMENT_KEY = "faefd6455e930e76b15ed17e709c6a694628234a84868dc9351fd74ecdd34191";//sandbox
	
	//public static final String GARENA_PAYMENT_KEY = "2eb5d33ba0cbd822bcd7f5ba55c54ce61f25805003a2103097ac941be8e9b1fa";//live
	
	public static final String GARENA_POST_KEY = "6302197ef28376496dc35648d2a9e993adddca995bbcab47d266f732c13fe0a2";//sandbox
	
	public static final String SIFUBA_APP_KEY = "710d8edf207a2b66e86b46395113c71e";//用于登录、角色查询接口等
	
	public static final String SIFUBA_PAY_KEY = "9ade1d4a413bb861cd4d8f4bf5e76dea";//支付key

	public static String getGarenaUrlBase() {
		return SANDBOX_URL_BASE;
	}
	
	public static String getBeetalkUrlBase() {
		return SANDBOX_BEETALK_BASE;
	}

	public enum ClientType {

		PC(0), iOS(1), Android(2), WindowsPhone(3), Unknown(255);

		private final int val;

		private ClientType(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}

	public enum ClientPlatform {
		Garena(1), BeeTalk(2), Facebook(3), Guest(4);

		private final int val;

		private ClientPlatform(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}

}
