package cc.mrbird.febs.template;

import cc.mrbird.febs.common.runner.FebsStartedUpRunner;
import com.google.common.io.Files;
import game.module.template.ActivityTemplate;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lion.common.BeanTool;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(ActivityTemplateCache.class);

	static class SingletonHolder {
		static ActivityTemplateCache instance = new ActivityTemplateCache();
	}

	public static ActivityTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有配置
	 */
	private transient List<ActivityTemplate> templateList;

	/**
	 * 所有配置<br/>
	 */
	private volatile Map<Integer, ActivityTemplate> templateMap;

	@Override
	public void reload() {
		try {
			Schema<TemplateCollection> schema = RuntimeSchema.getSchema(TemplateCollection.class);
			TemplateCollection tc = new TemplateCollection();
			String fileName = ActivityTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			ClassPathResource cpr = new ClassPathResource("templates/"+fileName+".pro");
			byte[] pomByte =  FileCopyUtils.copyToByteArray(cpr.getInputStream());
			ProtostuffIOUtil.mergeFrom(pomByte, tc, schema);
			List<ActivityTemplate> tplList = tc.getObjList(ActivityTemplate.class);
			templateList = tplList;
			logger.info("ActivityTemplate,size={}", tplList.size());
			Map<Integer, ActivityTemplate> tmpTemplateMap = new HashMap<Integer, ActivityTemplate>();
			BeanTool.addOrUpdate(tmpTemplateMap, templateList, "id");
			templateMap = tmpTemplateMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 获得某个激活码配置
	 * 
	 * @param id
	 * @return
	 */
	public ActivityTemplate getActivityTemplate(int id) {
		return templateMap != null ? templateMap.get(id) : null;
	}

	public List<ActivityTemplate> getActivityTemplates() {
		return templateList;
	}

}
