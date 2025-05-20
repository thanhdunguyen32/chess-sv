package game.module.award.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.AwardTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwardTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(AwardTemplateCache.class);

	static class SingletonHolder {
		static AwardTemplateCache instance = new AwardTemplateCache();
	}

	public static AwardTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, AwardTemplate> templateMap;

	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/Award.json"), StandardCharsets.UTF_8);
			List<AwardTemplate> templateList = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<AwardTemplate>>() {
			});
			logger.info("size={}", templateList.size());
			Map<Integer, AwardTemplate> tmpmap = new HashMap<>();
			for (AwardTemplate itemTemplate : templateList) {
				tmpmap.put(itemTemplate.getId(), itemTemplate);
			}
			templateMap = tmpmap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public AwardTemplate getAwardTemplateById(int awardId) {
		return templateMap.get(awardId);
	}

	public static void main(String[] args) {
		new AwardTemplateCache().reload();

	}

}
