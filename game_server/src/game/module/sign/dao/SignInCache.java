package game.module.sign.dao;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.module.sign.bean.SignIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.DateCommonUtils;

/**
 * 签到缓存数据
 * 
 * @author zhangning
 * 
 * @Date 2015年1月12日 下午6:02:56
 */
public class SignInCache {

	private static Logger logger = LoggerFactory.getLogger(SignInCache.class);

	static class SingletonHolder {
		static SignInCache instance = new SignInCache();
	}

	public static SignInCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 签到缓存<br/>
	 * Key：玩家唯一ID
	 */
	private Map<Integer, SignIn> signCacheAll = new ConcurrentHashMap<Integer, SignIn>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		if(signCacheAll.containsKey(playerId)){
			return;
		}
		// Load玩家邮件
		SignIn signIn = SignDao.getInstance().getPlayerSign(playerId);
		if (signIn != null) {
			signCacheAll.put(signIn.getPlayerId(), signIn);
		}
		logger.info("Player ID: {}'s check-in cache data is loaded successfully", playerId);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		signCacheAll.remove(playerId);
	}

	/**
	 * 获取玩家在线签到信息
	 * 
	 * @param playerId
	 * @return
	 */
	public SignIn getSign(int playerId) {
		return signCacheAll.get(playerId);
	}

	/**
	 * 添加一条签到记录
	 * 
	 * @param signIn
	 */
	public void addOneSign(SignIn signIn) {
		if (signIn != null) {
			signCacheAll.put(signIn.getPlayerId(), signIn);
		}
	}

}
