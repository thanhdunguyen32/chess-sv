package cross.boss.logic;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import game.module.cross.bean.CrossBossBegin;

public class BossRequestManager {

	private static Logger logger = LoggerFactory.getLogger(BossRequestManager.class);

	static class SingletonHolder {
		static BossRequestManager instance = new BossRequestManager();
	}

	public static BossRequestManager getInstance() {
		return SingletonHolder.instance;
	}

	private Cache<Long, CrossBossBegin> craftRequestMap = CacheBuilder.newBuilder().concurrencyLevel(8)
			.expireAfterWrite(60, TimeUnit.SECONDS).build();

	public void addRequest(long sessionId, CrossBossBegin crossCraftBegin) {
		craftRequestMap.put(sessionId, crossCraftBegin);
	}

	public CrossBossBegin checkExist(long sessionId) {
		return craftRequestMap.getIfPresent(sessionId);
	}

	public void remove(long sessionId) {
		craftRequestMap.invalidate(sessionId);
	}

}
