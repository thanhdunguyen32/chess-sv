package game.module.cdkey.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.cdkey.bean.Cdk;
import game.module.cdkey.bean.Cdkey;

/**
 * 激活码缓存数据
 * 
 * @author zhangning
 * 
 * @Date 2015年2月13日 上午7:25:06
 */
public class CdkeyCache {

	private static Logger logger = LoggerFactory.getLogger(CdkeyCache.class);

	static class SingletonHolder {
		static CdkeyCache instance = new CdkeyCache();
	}

	public static CdkeyCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 该平台的所有激活码<br/>
	 * Key1:区, Key2:激活码
	 */
	private Map<Integer, Map<String, Cdk>> cdkOfPlatMap = new ConcurrentHashMap<Integer, Map<String, Cdk>>();

	/**
	 * 该平台兑换过的激活码<br/>
	 * Key1:区, Key2:激活码
	 */
	private Map<String, Cdkey> cdkeyCacheAll = new ConcurrentHashMap<String, Cdkey>();

	private Map<Integer, Set<Integer>> playerUsedAreas = new ConcurrentHashMap<Integer, Set<Integer>>();

	/**
	 * 激活码初始化
	 * 
	 * @param platform
	 *            : 平台ID
	 */
	public void loadFromDb(int platform) {
		try {
			// 自动生成的激活码集合
			// List<Cdk> cdks = CdkDao.getInstance().getCdksOfPlat(platform);
			// if (cdks != null && !cdks.isEmpty()) {
			// for (Cdk cdk : cdks) {
			// Map<String, Cdk> cdkMap = cdkOfPlatMap.get(cdk.getArea());
			// if (cdkMap == null) {
			// cdkMap = new HashMap<String, Cdk>();
			// cdkOfPlatMap.put(cdk.getArea(), cdkMap);
			// }
			// cdkMap.put(cdk.getCdk(), cdk);
			// }
			// }

			// 使用过的激活码初始化
			List<Cdkey> cdkeyList = CdkeyDao.getInstance().getUsedCdkeys(platform);
			if (cdkeyList != null && !cdkeyList.isEmpty()) {
				for (Cdkey cdkey : cdkeyList) {
					cdkeyCacheAll.put(cdkey.getCdkey(), cdkey);
					Set<Integer> areaSet = playerUsedAreas.get(cdkey.getPlayerId());
					if (areaSet == null) {
						areaSet = new HashSet<>();
						playerUsedAreas.put(cdkey.getPlayerId(), areaSet);
					}
					areaSet.add(cdkey.getArea());
				}
			}
			logger.info("CdkeyCache loadFromDb success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 某区下的所有激活码
	 * 
	 * @param area
	 * @return
	 */
	public Map<String, Cdk> getCdkMap(int area) {
		return cdkOfPlatMap.get(area);
	}

	/**
	 * 某区下的所有激活码
	 * 
	 * @param area
	 * @return
	 */
	public int getCdkId(int area, String cdkStr) {
		Map<String, Cdk> cdkMap = cdkOfPlatMap.get(area);
		if (cdkMap != null && !cdkMap.isEmpty()) {
			Cdk cdk = cdkMap.get(cdkStr);
			return cdk != null ? cdk.getId() : 0;
		}
		return 0;
	}

	/**
	 * 奖励ID
	 * 
	 * @param cdkStr
	 * @return
	 */
	public int getAwardId(int area, String cdkStr) {
		Map<String, Cdk> cdkMap = getCdkMap(area);
		if (cdkMap != null && !cdkMap.isEmpty()) {
			Cdk cdk = cdkMap.get(cdkStr);
			if (cdk != null) {
				return cdk.getAwardId();
			}
		}

		return 0;
	}

	/**
	 * 激活码是否存在
	 * 
	 * @param area
	 * @param cdk
	 */
	public boolean isExistCdk(int area, String cdk) {
		Map<String, Cdk> cdkMap = getCdkMap(area);
		if (cdkMap != null && !cdkMap.isEmpty()) {
			return cdkMap.containsKey(cdk);
		}

		return false;
	}

	/**
	 * 激活码是否过期
	 * 
	 * @param area
	 * @param cdkStr
	 * @return
	 */
	public boolean isPastDue(int area, String cdkStr) {
		Map<String, Cdk> cdkMap = getCdkMap(area);
		if (cdkMap != null && !cdkMap.isEmpty()) {
			Cdk cdk = cdkMap.get(cdkStr);
			if (cdk != null) {
				return cdk.getEndTime() == null ? false : System.currentTimeMillis() > cdk.getEndTime().getTime();
			}
		}

		return false;
	}

	/**
	 * 删除过期激活码
	 * 
	 * @param area
	 * @param cdkStr
	 * @return
	 */
	public Cdk removePastDueCdk(int area, String cdkStr) {
		Map<String, Cdk> cdkMap = getCdkMap(area);
		if (cdkMap != null && !cdkMap.isEmpty()) {
			return cdkMap.remove(cdkStr);
		}
		return null;
	}

	// ---------------------------------使用过的激活码-------------------------------------//

	/**
	 * 获取某区, 使用过的激活码集合
	 * 
	 * @param area
	 * @return
	 */
	public Cdkey getCdkeyCache(String cdKey) {
		return cdkeyCacheAll.get(cdKey);
	}

	/**
	 * 激活码是否已使用
	 * 
	 * @param area
	 * @param cdkey
	 * @return
	 */
	public boolean isUsedCdkey(int area, String cdkey) {
		return cdkeyCacheAll.containsKey(cdkey);
	}

	/**
	 * 添加一条激活码记录
	 * 
	 * @param area
	 * @param mail
	 */
	public void addOneCdkey(int area, Cdkey cdkey) {
		if (cdkey != null) {
			cdkeyCacheAll.put(cdkey.getCdkey(), cdkey);
			Set<Integer> areaSet = playerUsedAreas.get(cdkey.getPlayerId());
			if (areaSet == null) {
				areaSet = new HashSet<>();
				playerUsedAreas.put(cdkey.getPlayerId(), areaSet);
			}
			areaSet.add(area);
		}
	}

	public boolean isPlayerGetArea(int playerId, int area) {
		boolean ret = false;
		Set<Integer> retSet = playerUsedAreas.get(playerId);
		if (retSet != null && retSet.contains(area)) {
			ret = true;
		}
		return ret;
	}
	
}
