package game.module.user.dao;

import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CommonTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(CommonTemplateCache.class);

	static class SingletonHolder {
		static CommonTemplateCache instance = new CommonTemplateCache();
	}

	public static CommonTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<String, Object> templateMap;

	@SuppressWarnings("unchecked")
	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/Common.json"), StandardCharsets.UTF_8);
			templateMap = JacksonUtils.getInstance().readValue(jsonStr, Map.class);
			logger.info("size={}", templateMap.size());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Object getConfig(String aKey) {
		return templateMap.get(aKey);
	}

	public int getIntConfig(String aKey) {
		return (int) templateMap.get(aKey);
	}

	public static void main(String[] args) {
		new CommonTemplateCache().reload();
	}

}