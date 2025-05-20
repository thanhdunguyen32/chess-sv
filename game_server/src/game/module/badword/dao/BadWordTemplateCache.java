package game.module.badword.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.ExcelTemplateParse;
import game.module.template.BadWordTemplate;

public class BadWordTemplateCache {

	private static Logger logger = LoggerFactory.getLogger(BadWordTemplateCache.class);

	static class SingletonHolder {
		static BadWordTemplateCache instance = new BadWordTemplateCache();
	}

	public static BadWordTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	public List<BadWordTemplate> loadFromDb() {
		List<BadWordTemplate> tplList = ExcelTemplateParse.parse(BadWordTemplate.class);
		if (tplList != null) {
			logger.info("BadWords template,size={}", tplList.size());
			return tplList;
		} else {
			logger.error("Không thể parse dữ liệu template");
			return null;
		}
	}

	public static void main(String[] args) {
		BadWordTemplateCache.getInstance().loadFromDb();
	}

}
