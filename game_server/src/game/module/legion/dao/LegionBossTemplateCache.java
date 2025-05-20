package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionBossTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 任务：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class LegionBossTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(LegionBossTemplateCache.class);

	public List<LegionBossTemplate.LegionBossRank> getRankTemplate() {
		return templateMap.getRANK();
	}

    static class SingletonHolder {
		static LegionBossTemplateCache instance = new LegionBossTemplateCache();
	}

	public static LegionBossTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile LegionBossTemplate templateMap;

	/**
	 * 所有的任务进程
	 * 
	 * @return
	 */
	public List<LegionBossTemplate.LegionBoss1> getBossTemplates() {
		return templateMap.getBOSS();
	}

	public List<LegionBossTemplate.LegionBossCost> getCosts(){
		return templateMap.getCOST();
	}

	@Override
	public void reload() {
		try {
			String fileName = LegionBossTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			LegionBossTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<LegionBossTemplate>() {
			});
			logger.info("size={}", templateWrapperMap.getBOSS().size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public LegionBossTemplate.LegionBoss1 getBossTemplate(int chapter_index) {
		return templateMap.getBOSS().get(chapter_index);
	}

	public int getSize(){
		return templateMap.getBOSS().size();
	}

	public static void main(String[] args) {
		LegionBossTemplateCache.getInstance().reload();
	}
}
