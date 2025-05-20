package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionBossTemplate;
import game.module.template.LegionFDonationTemplate;
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
public class LegionFDonationTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(LegionFDonationTemplateCache.class);

	static class SingletonHolder {
		static LegionFDonationTemplateCache instance = new LegionFDonationTemplateCache();
	}

	public static LegionFDonationTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile Map<Integer, List<LegionFDonationTemplate>> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = LegionFDonationTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			Map<Integer, List<LegionFDonationTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
					new TypeReference<Map<Integer, List<LegionFDonationTemplate>>>() {
			});
			logger.info("size={}", templateWrapperMap.get(1).size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public List<LegionFDonationTemplate> getLegionFDonationTemplate(int donateType){
		return templateMap.get(donateType);
	}

	public static void main(String[] args) {
		LegionFDonationTemplateCache.getInstance().reload();
	}
}
