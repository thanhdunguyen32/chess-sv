package cross.logic;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import game.module.cross.bean.CrossCraftBegin;

public class CraftRequestManager {

	private static Logger logger = LoggerFactory.getLogger(CraftRequestManager.class);

	static class SingletonHolder {
		static CraftRequestManager instance = new CraftRequestManager();
	}

	public static CraftRequestManager getInstance() {
		return SingletonHolder.instance;
	}

	private Cache<Long, CrossCraftBegin> craftRequestMap = CacheBuilder.newBuilder().concurrencyLevel(8)
			.expireAfterWrite(60, TimeUnit.SECONDS).build();

	public void addRequest(long sessionId, CrossCraftBegin crossCraftBegin) {
		craftRequestMap.put(sessionId, crossCraftBegin);
	}

	public CrossCraftBegin checkExist(long sessionId) {
		return craftRequestMap.getIfPresent(sessionId);
	}
	
	public void remove(long sessionId){
		craftRequestMap.invalidate(sessionId);
	}

}
