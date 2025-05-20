package game.module.user.logic;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfflineManager {

	private static Logger logger = LoggerFactory.getLogger(OfflineManager.class);

	static class SingletonHolder {
		static OfflineManager instance = new OfflineManager();
	}

	public static OfflineManager getInstance() {
		return SingletonHolder.instance;
	}

	public static void main(String[] args) {
		Map<Integer, Integer> rewardAll = new HashMap<>();
		rewardAll.put(1, 200);
		rewardAll.put(2, 400);
		float rewardRate = 1.2f;
		for (Map.Entry<Integer, Integer> aEntry : rewardAll.entrySet()) {
			rewardAll.put(aEntry.getKey(), (int) (aEntry.getValue() * rewardRate));
		}
		logger.info("x={}", rewardAll);
	}

}
