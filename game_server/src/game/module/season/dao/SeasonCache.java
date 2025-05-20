package game.module.season.dao;

import game.module.activity_month.logic.ActivityMonthManager;
import game.module.season.bean.BattleSeason;
import game.module.season.logic.SeasonManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 * 
 * @author zhangning
 *
 */
public class SeasonCache {

	private static Logger logger = LoggerFactory.getLogger(SeasonCache.class);

    static class SingletonHolder {
		static SeasonCache instance = new SeasonCache();
	}

	public static SeasonCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 任务进度缓存<br/>
	 * Key：玩家唯一ID<br/>
	 * Value：每个玩家的任务缓存
	 * 
	 */
	private BattleSeason battleSeason;

	/**
	 * 初始化数据到缓存中
	 * 
	 */
	public void loadFromDb() {
		battleSeason = SeasonDao.getInstance().getBattleSeason();
		if(battleSeason == null){
			battleSeason = SeasonManager.getInstance().createBattleSeason();
			SeasonDao.getInstance().addBattleSeason(battleSeason);
		}
		logger.info("battle season loadFromDb success!");
	}

	public void setBattleSeason(BattleSeason abattleSeason) {
		battleSeason = abattleSeason;
	}

	public BattleSeason getBattleSeason(){
		return battleSeason;
	}

}
