package game.module.award.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.award.dao.AwardTemplateCache;
import game.module.box.bean.ItemPair;
import game.module.template.AwardTemplate;
import lion.common.StringConstants;
import lion.math.RandomDispatcher;

public class AwardManager {

	private static Logger logger = LoggerFactory.getLogger(AwardManager.class);

	public static final int AWARD_RATE_SINGLE = 1;

	public static final int AWARD_RATE_RELATIVE = 2;

	private AwardTemplateCache awardTemplateCache = AwardTemplateCache.getInstance();

	static class SingletonHolder {
		static AwardManager instance = new AwardManager();
	}

	public static AwardManager getInstance() {
		return SingletonHolder.instance;
	}

	public List<ItemPair> getAward(int awardId) {
		AwardTemplate awardTemplate = awardTemplateCache.getAwardTemplateById(awardId);
		if (awardTemplate == null) {
			return null;
		}
		int awardType = awardTemplate.getType();
		String awardStr = awardTemplate.getAward();
		if (awardType == AWARD_RATE_SINGLE) {
			return independentRate(awardStr);
		} else if (awardType == AWARD_RATE_RELATIVE) {
			return relativeRate(awardStr);
		}
		return null;
	}

	private List<ItemPair> relativeRate(String awardStr) {
		String[] awardOneList = StringUtils.split(awardStr, StringConstants.SEPARATOR_HENG);
		int count = Integer.valueOf(awardOneList[0]);
		RandomDispatcher<Integer> randomDispatcher = new RandomDispatcher<Integer>();
		for (int i = 1; i < awardOneList.length; i++) {
			String awardOnePair = awardOneList[i];
			String[] awardPairList = StringUtils.split(awardOnePair, StringConstants.SEPARATOR_SHU);
			int awardId = Integer.valueOf(awardPairList[0]);
			int awardRate = Integer.valueOf(awardPairList[1]);
			randomDispatcher.put(awardRate, awardId);
		}
		// result
		List<ItemPair> retList = new ArrayList<ItemPair>();
		for (int i = count; i > 0; i--) {
			int randAwardId = randomDispatcher.randomRemove();
			AwardTemplate awardTemplate = awardTemplateCache.getAwardTemplateById(randAwardId);
			String independentAwardStr = awardTemplate.getAward();
			retList.addAll(independentRate(independentAwardStr));
		}
		return retList;
	}

	private List<ItemPair> independentRate(String awardStr) {
		List<ItemPair> retList = new ArrayList<ItemPair>();
		String awardList = awardStr;
		String[] awardOneList = StringUtils.split(awardList, StringConstants.SEPARATOR_HENG);
		for (String awardOne : awardOneList) {
			String[] itemList = StringUtils.split(awardOne, StringConstants.SEPARATOR_SHU);
			int tplId = Integer.valueOf(itemList[0]);
			int rateNum = Integer.valueOf(itemList[1]);
			int randRate = RandomUtils.nextInt(1, StringConstants.RATE_MAX);
			if (randRate > rateNum) {
				continue;
			}
			// 生成物品
			RandomDispatcher<Integer> randomDispatcher = new RandomDispatcher<Integer>();
			for (int i = 2; i < itemList.length; i += 2) {
				randomDispatcher.put(Integer.valueOf(itemList[i + 1]), Integer.valueOf(itemList[i]));
			}
			int randCount = randomDispatcher.random();
			retList.add(new ItemPair(tplId, randCount));
		}
		return retList;
	}

	public static void main(String[] args) {
		AwardTemplateCache.getInstance().reload();
		List<ItemPair> itemList = AwardManager.getInstance().getAward(2025);
		logger.info("itemList={}", itemList);
	}

}
