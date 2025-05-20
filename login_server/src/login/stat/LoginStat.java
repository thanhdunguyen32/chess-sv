package login.stat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginStat {

	private static Logger logger = LoggerFactory.getLogger(LoginStat.class);

	private Map<String, Long> loginProcessorMap = new ConcurrentHashMap<String, Long>();

	private Map<Long, Long> selectServerProcessorMap = new ConcurrentHashMap<Long, Long>();

	static class SingletonHolder {
		static LoginStat instance = new LoginStat();
	}

	public static LoginStat getInstance() {
		return SingletonHolder.instance;
	}

	public void saveLoginTime(String uname) {
		loginProcessorMap.put(uname, System.currentTimeMillis());
	}

	public void statLogin(String uname) {
		Long startLoginTime = loginProcessorMap.get(uname);
		if (startLoginTime != null) {
			logger.info("login processor cost={}ms,uname={}", System.currentTimeMillis() - startLoginTime, uname);
		}
	}

	public void saveSelectServerTime(Long sessionId) {
		selectServerProcessorMap.put(sessionId, System.currentTimeMillis());
	}

	public void statSelectServer(Long sessionId) {
		Long startSelectServerTime = selectServerProcessorMap.get(sessionId);
		if (startSelectServerTime != null) {
			logger.info("select server processor cost={}ms,sessionId={}", System.currentTimeMillis()
					- startSelectServerTime, sessionId);
		}
	}

}
