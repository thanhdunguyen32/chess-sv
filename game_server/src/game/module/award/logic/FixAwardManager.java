package game.module.award.logic;

import game.module.award.dao.FixAwardTemplateCache;
import game.module.template.FixAwardTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FixAwardManager {

	private static final Logger logger = LoggerFactory.getLogger(FixAwardManager.class);

	static class SingletonHolder {
		static FixAwardManager instance = new FixAwardManager();
	}

	public static FixAwardManager getInstance() {
		return SingletonHolder.instance;
	}

	public Map<Integer, Integer> getAward(int awardId) {
		FixAwardTemplate fixAwardTemplate = FixAwardTemplateCache.getInstance().getFixAwardTemplateById(awardId);
		if (fixAwardTemplate == null) {
			return null;
		}
		return fixAwardTemplate.getAward();
	}

}
