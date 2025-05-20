package game.module.pay.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.RightTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RightTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(RightTemplateCache.class);

	static class SingletonHolder {
		static RightTemplateCache instance = new RightTemplateCache();
	}

	public static RightTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<String,RightTemplate> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = RightTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			Map<String,RightTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
					new TypeReference<Map<String,RightTemplate>>() {});
			logger.info("size={}", templateWrapperMap.size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public RightTemplate getRightTemplate(String productId){
		return templateMap.get(productId);
	}

}