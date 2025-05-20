package game.module.activity.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.ExcelTemplateParse;
import game.module.template.ActivityTemplate;
import lion.common.BeanTool;
import lion.common.Reloadable;

public class ActivityTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(ActivityTemplateCache.class);

	static class SingletonHolder {
		static ActivityTemplateCache instance = new ActivityTemplateCache();
	}

	public static ActivityTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<Integer, ActivityTemplate> templateMap;

	@Override
	public void reload() {
		try {
			List<ActivityTemplate> tplList = ExcelTemplateParse.parse(ActivityTemplate.class);
			logger.info("activityTemplate,size={}", tplList.size());
			Map<Integer, ActivityTemplate> tmpTemplateMap = new HashMap<Integer, ActivityTemplate>();
			BeanTool.addOrUpdate(tmpTemplateMap, tplList, "id");
			templateMap = tmpTemplateMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public ActivityTemplate getActivityTemplateById(int templateId) {
		return templateMap.get(templateId);
	}

	public static void main(String[] args) {
		new ActivityTemplateCache().reload();

	}

}
