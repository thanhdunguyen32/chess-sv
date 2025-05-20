package login.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class LoginSessionCache {

	static class SingletonHolder {
		static LoginSessionCache instance = new LoginSessionCache();
	}

	public static LoginSessionCache getInstance() {
		return SingletonHolder.instance;
	}

	private Cache<Long, String> cacheAll = CacheBuilder.newBuilder().concurrencyLevel(8)
			.expireAfterAccess(12, TimeUnit.HOURS).build();

	public void addSession(long sessionId, String openId) {
		this.cacheAll.put(sessionId, openId);
	}

	public String getOpenId(long sessionId) {
		return cacheAll.getIfPresent(sessionId);
	}

	public void removeSession(long sessionId) {
		cacheAll.invalidate(sessionId);
	}

	public void visit(Long accountKey) {
		cacheAll.getIfPresent(accountKey);
	}

}
