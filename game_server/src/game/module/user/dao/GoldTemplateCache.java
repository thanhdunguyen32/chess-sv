package game.module.user.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.GoldTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GoldTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(GoldTemplateCache.class);

	static class SingletonHolder {

		static GoldTemplateCache instance = new GoldTemplateCache();
	}
	public static GoldTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile GoldTemplate templateList;

	@Override
	public void reload() {
		try {
			String fileName = GoldTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			GoldTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<GoldTemplate>() {
			});
			logger.info("size={}", templateWrapperMap);
			templateList = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public GoldTemplate getGoldTemplate() {
		return templateList;
	}

	public List<GoldTemplate.GoldTemplateInfo> getInfo() {
		return templateList.getINFO();
	}

	public static void main(String[] args) {
		GoldTemplateCache.getInstance().reload();
//		List<String> retlist = LevelsTemplateCache.getInstance()
//				.getDataListFromLine("30,600,\"[[2,1,5000],[2,2,5000],[2,3,5000],[4,5031,2],[4,20001,2]]\"");
//		logger.info("{}",retlist);
	}

}