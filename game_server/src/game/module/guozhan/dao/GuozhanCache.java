package game.module.guozhan.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.module.offline.logic.PlayerOfflineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanNation;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOfficePoint;
import game.module.guozhan.bean.GuozhanOfficeTemplate;
import game.module.guozhan.bean.GuozhanPlayer;

/**
 * 秘密基地缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年1月28日 上午10:15:39
 */
public class GuozhanCache {

	private static Logger logger = LoggerFactory.getLogger(GuozhanCache.class);

	static class SingletonHolder {
		static GuozhanCache instance = new GuozhanCache();
	}

	public static GuozhanCache getInstance() {
		return SingletonHolder.instance;
	}
	
	private DBGuoZhanOffice guoZhanOffice = null;
	
	private DBGuoZhanFight guozhanFight = null;

	private Date lastUnionRewardTime = null;

	/**
	 * 秘密基地缓存<br/>
	 * Key：玩家唯一ID
	 */
	private Map<Integer, GuozhanPlayer> secretCacheAll = new ConcurrentHashMap<Integer, GuozhanPlayer>();

	public void loadFromDb() {
		guoZhanOffice = GuozhanDao.getInstance().getGuozhan();
		//正在战斗数据重置
		if(guoZhanOffice != null) {
			DBGuoZhanOffice.Builder rootBuilder = guoZhanOffice.toBuilder();
			List<DBGuoZhanNation.Builder> nationList = rootBuilder.getNationsBuilderList();
			List<GuozhanOfficeTemplate> officeTemplates = GuozhanOfficeTemplateCache.getInstance().getTemplates();
			for (DBGuoZhanNation.Builder nationBuilder : nationList) {
				if (nationBuilder.getPlayerOfficesCount() == 0) {
					continue;
				}
				int levelIndex = 0;
				for (GuozhanOfficeTemplate guozhanOfficeTemplate : officeTemplates) {
					int levelSize = guozhanOfficeTemplate.getCount();
					for (int i = 0; i < levelSize; i++) {
						int officeIndex = levelIndex * 10 + i;
						DBGuoZhanOfficePoint.Builder guoZhanOfficePointBuilder = nationBuilder.getPlayerOfficesBuilder(officeIndex);
						if (guoZhanOfficePointBuilder.getIsFighting()) {
							guoZhanOfficePointBuilder.setIsFighting(false);
						}
					}
					levelIndex++;
				}
			}
			guoZhanOffice = rootBuilder.build();
		}
		guozhanFight = GuozhanDao.getInstance().getGuozhanFight();
		lastUnionRewardTime = GuozhanDao.getInstance().getGuozhanLastUnionRewardTime();
	}
	
	public DBGuoZhanOffice getDBGuoZhanOffice() {
		return guoZhanOffice;
	}

	public DBGuoZhanFight getDBGuoZhanFight() {
		return guozhanFight;
	}
	
	public void setGuozhanOffice(DBGuoZhanOffice pbb) {
		guoZhanOffice = pbb;
	}

	public void setGuozhanFight(DBGuoZhanFight pbb) {
		guozhanFight = pbb;
	}

	public void remove() {
		guoZhanOffice = null;
	}

	public void save2Db() {
		if(guoZhanOffice != null) {
			GuozhanDao.getInstance().updateGuozhan(guoZhanOffice);
		}
		if (guozhanFight != null) {
			GuozhanDao.getInstance().updateGuozhanFight(guozhanFight, lastUnionRewardTime);
		}
	}
	
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
		GuozhanPlayer secret = GuozhanPlayerDao.getInstance().getPlayerGuozhanPlayer(playerId);
		if (secret != null) {
			secretCacheAll.put(playerId, secret);
		}
		//更新缓存
		PlayerOfflineManager.getInstance().updateNationId(playerId, secret);
	}

	/**
	 * 下线删除缓存
	 * 
	 * @param playerId
	 */
	public void remove(int playerId) {
		secretCacheAll.remove(playerId);
	}

	public GuozhanPlayer getGuozhanPlayer(int playerId) {
		return secretCacheAll.get(playerId);
	}

	/**
	 * 添加秘密基地
	 * 
	 * @param playerId
	 * @param mail
	 */
	public void addGuozhanPlayer(GuozhanPlayer secret) {
		if (secret != null) {
			secretCacheAll.put(secret.getPlayerId(), secret);
		}
	}

	public Collection<GuozhanPlayer> getGuozhanPlayerAll() {
		return secretCacheAll.values();
	}

	public Date getLastUnionRewardTime() {
		return lastUnionRewardTime;
	}

	public void setLastUnionRewardTime(Date newTime) {
		lastUnionRewardTime = newTime;
	}

}
