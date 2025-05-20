package game.module.mission.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MissionDailyTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * 任务：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class MissionDailyTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(MissionDailyTemplateCache.class);

	static class SingletonHolder {
		static MissionDailyTemplateCache instance = new MissionDailyTemplateCache();
	}

	public static MissionDailyTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile List<MissionDailyTemplate> templateMap;

	/**
	 * 所有的任务进程
	 * 
	 * @return
	 */
	public List<MissionDailyTemplate> getAllMissionDaily() {
		return templateMap;
	}

	@Override
	public void reload() {
		try {
			String fileName = MissionDailyTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			List<MissionDailyTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MissionDailyTemplate>>() {
			});
			logger.info("size={}", templateWrapperMap.size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public MissionDailyTemplate getMissionDaily(int mission_index) {
		return templateMap.get(mission_index);
	}

	public int getSize(){
		return templateMap.size();
	}

	public static void main(String[] args) {
		MissionDailyTemplateCache.getInstance().reload();
	}
}
