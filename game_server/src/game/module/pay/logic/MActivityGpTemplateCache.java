package game.module.pay.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MActivityGpTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MActivityGpTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(MActivityGpTemplateCache.class);

	static class SingletonHolder {
		static MActivityGpTemplateCache instance = new MActivityGpTemplateCache();
	}

	public static MActivityGpTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile List<MActivityGpTemplate> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = MActivityGpTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			List<MActivityGpTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
					new TypeReference<List<MActivityGpTemplate>>() {});
			logger.info("size={}", templateWrapperMap.size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public List<MActivityGpTemplate> getTemplateMap(){
		return templateMap;
	}

}