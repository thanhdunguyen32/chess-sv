package game.module.secret.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import game.module.secret.bean.Secret;

/**
 * 秘密基地缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年1月28日 上午10:15:39
 */
public class SecretCache {

	private static Logger logger = LoggerFactory.getLogger(SecretCache.class);

	static class SingletonHolder {
		static SecretCache instance = new SecretCache();
	}

	public static SecretCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地缓存<br/>
	 * Key：玩家唯一ID
	 */
	private Map<Integer, Secret> secretCacheAll = new ConcurrentHashMap<Integer, Secret>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		if (secretCacheAll.containsKey(playerId)) {
			return;
		}
		// Load玩家秘密基地
		Secret secret = SecretDao.getInstance().getPlayerSecret(playerId);
		if (secret != null) {
			secretCacheAll.put(playerId, secret);
		}
		logger.info("Player ID: {}'s secret base cache data is loaded successfully", playerId);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		secretCacheAll.remove(playerId);
	}

	public Secret getSecret(int playerId) {
		return secretCacheAll.get(playerId);
	}

	/**
	 * 添加秘密基地
	 * 
	 * @param playerId
	 * @param mail
	 */
	public void addSecret(Secret secret) {
		if (secret != null) {
			secretCacheAll.put(secret.getPlayerId(), secret);
		}
	}

	/**
	 * 随机副本奖励
	 * 
	 * @param playerId
	 * @param copyId
	 * @return
	 */
	public DBSecretBoxAward getRandomAward(int playerId, int copyId) {
//		Secret secret = getSecret(playerId);
//		if (secret != null) {
//			List<RandomAward> randomAwards = secret.getRandomAward().getRandomAwardList();
//			for (RandomAward randomAward : randomAwards) {
//				if (copyId == randomAward.getCopyId() && randomAward.getIsGet() == SecretConstants.NO) {
//					return randomAward;
//				}
//			}
//		}
		return null;
	}

}
