package game.module.user.dao;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class OfflineCache {

	static class SingletonHolder {
		static OfflineCache instance = new OfflineCache();
	}

	public static OfflineCache getInstance() {
		return SingletonHolder.instance;
	}

	private Cache<Integer, OfflineAwardBean> cacheAll = CacheBuilder.newBuilder().concurrencyLevel(8)
			.expireAfterAccess(5, TimeUnit.MINUTES).build();

	public void putOfflineAward(Integer playerId, int offlineMinutes, Map<Integer, Integer> awards) {
		this.cacheAll.put(playerId, new OfflineAwardBean(offlineMinutes, awards));
	}

	public OfflineAwardBean getOfflineAward(Integer playerId) {
		return cacheAll.getIfPresent(playerId);
	}

	public void removeOfflineAward(Integer playerId) {
		cacheAll.invalidate(playerId);
	}

	public static final class OfflineAwardBean {
		public int offlineMinutes;
		public Map<Integer, Integer> awards;

		public OfflineAwardBean(int offlineMinutes, Map<Integer, Integer> awards) {
			super();
			this.offlineMinutes = offlineMinutes;
			this.awards = awards;
		}
	}

}
