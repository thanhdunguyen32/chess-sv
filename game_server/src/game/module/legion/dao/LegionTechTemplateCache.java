package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionMissionTemplate;
import game.module.template.LegionTechTemplate;
import game.module.template.OnlineGiftTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 任务：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class LegionTechTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(LegionTechTemplateCache.class);

	static class SingletonHolder {
		static LegionTechTemplateCache instance = new LegionTechTemplateCache();
	}

	public static LegionTechTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile Map<Integer,LegionTechTemplate> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = LegionTechTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			Map<Integer, LegionTechTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
					new TypeReference<Map<Integer,LegionTechTemplate>>() {
			});
			logger.info("size={}", templateWrapperMap.size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public boolean containsTechId(int techId){
		return templateMap.containsKey(techId);
	}

	public LegionTechTemplate getLegionTechTemplate(int techId){
		return templateMap.get(techId);
	}

	public static void main(String[] args) {
		LegionTechTemplateCache.getInstance().reload();
	}
}
