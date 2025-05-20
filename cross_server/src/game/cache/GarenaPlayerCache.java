package game.cache;

import game.bean.BusPlayerBean;
import game.module.player.dao.BusPlayerDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarenaPlayerCache {

	private static Logger logger = LoggerFactory.getLogger(GarenaPlayerCache.class);

	/**
	 * Key: garena open id
	 */
	private Map<String, Map<Integer, BusPlayerBean>> cache = new ConcurrentHashMap<>();

	static class SingletonHolder {
		static GarenaPlayerCache instance = new GarenaPlayerCache();
	}

	public static GarenaPlayerCache getInstance() {
		return SingletonHolder.instance;
	}

	public void loadFromDb() {
		List<BusPlayerBean> busPlayerBeans = BusPlayerDao.getInstance().busPlayerBeans();
		if (busPlayerBeans != null && !busPlayerBeans.isEmpty()) {
			for (BusPlayerBean busPlayerBean : busPlayerBeans) {
				Map<Integer, BusPlayerBean> busPlayerBeanMap = cache.get(busPlayerBean.getGarenaOpenId());
				if (busPlayerBeanMap == null) {
					busPlayerBeanMap = new HashMap<Integer, BusPlayerBean>();
					cache.put(busPlayerBean.getGarenaOpenId(), busPlayerBeanMap);
				}

				busPlayerBeanMap.put(busPlayerBean.getZoneId(), busPlayerBean);
			}
			logger.info("load all server player, size={}", busPlayerBeans.size());
		}
	}

	public void add(String pOpenId, Integer zoneId, String name, Integer icon, Integer level) {
		Map<Integer, BusPlayerBean> oneOpenIdMap = cache.get(pOpenId);
		if (oneOpenIdMap == null) {
			oneOpenIdMap = new HashMap<>();
			cache.put(pOpenId, oneOpenIdMap);
		}
		oneOpenIdMap.put(zoneId, new BusPlayerBean(zoneId, pOpenId, 0, name, icon, level));
	}

	public List<BusPlayerBean> getPlayerList(List<String> garenaOpenIds) {
		if (garenaOpenIds == null || garenaOpenIds.size() == 0) {
			return null;
		}
		List<BusPlayerBean> retList = new ArrayList<>();
		for (String garenaOpenId : garenaOpenIds) {
			Map<Integer, BusPlayerBean> playerMap = cache.get(garenaOpenId);
			int maxLevel = 0;
			int maxZoneId = 0;
			BusPlayerBean targetBean = null;
			for (BusPlayerBean aBean : playerMap.values()) {
				if (aBean.getLevel() > maxLevel) {
					targetBean = aBean;
					maxZoneId = aBean.getZoneId();
				} else if (aBean.getLevel() == maxLevel) {
					if (maxZoneId < aBean.getZoneId()) {
						targetBean = aBean;
						maxZoneId = aBean.getZoneId();
					}
				}
			}
			if (targetBean != null) {
				retList.add(targetBean);
			}
		}
		return retList;
	}

	public void updateLevel(String garenaOpenId, Integer zoneId, Integer playerId, Integer level) {
		Map<Integer, BusPlayerBean> oneOpenIdMap = cache.get(garenaOpenId);
		if (oneOpenIdMap == null) {
			return;
		}
		BusPlayerBean busPlayerBean = oneOpenIdMap.get(zoneId);
		if (busPlayerBean == null || !playerId.equals(busPlayerBean.getPlayerId())) {
			return;
		}
		busPlayerBean.setLevel(level);
	}

	public boolean containGarenaId(String garenaId) {
		return cache.containsKey(garenaId);
	}

	public Map<Integer, BusPlayerBean> getBusPlayers(String garenaId) {
		return cache.get(garenaId);
	}

	public BusPlayerBean getOneBusPlayerBean(String garenaOpenId) {
		Map<Integer, BusPlayerBean> busPlayerBeanMap = cache.get(garenaOpenId);
		if (busPlayerBeanMap != null) {
			List<BusPlayerBean> playerBeans = new ArrayList<BusPlayerBean>(busPlayerBeanMap.values());

			if (playerBeans.size() > 1) {
				return getLevelLargeOne(playerBeans);
			} else {
				return playerBeans.get(0);
			}
		}

		return null;
	}

	private BusPlayerBean getLevelLargeOne(List<BusPlayerBean> busPlayerBeans) {
		Collections.sort(busPlayerBeans, new Comparator<BusPlayerBean>() {

			@Override
			public int compare(BusPlayerBean o1, BusPlayerBean o2) {
				int compare = o2.getLevel() - o1.getLevel();
				if (compare == 0) {
					compare = o2.getZoneId() - o1.getZoneId();
				}
				return compare;
			}
		});

		return busPlayerBeans.get(0);
	}

}
