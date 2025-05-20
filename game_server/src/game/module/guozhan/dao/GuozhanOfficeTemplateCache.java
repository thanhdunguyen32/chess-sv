package game.module.guozhan.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import game.module.guozhan.bean.GuozhanOfficeTemplate;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 秘密基地：配置表缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年2月4日 下午8:35:44
 */
public class GuozhanOfficeTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(GuozhanOfficeTemplateCache.class);

	static class SingletonHolder {
		static GuozhanOfficeTemplateCache instance = new GuozhanOfficeTemplateCache();
	}

	public static GuozhanOfficeTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地配置<br/>
	 */
	private volatile List<GuozhanOfficeTemplate> guozhanOfficeTemplates = new ArrayList<GuozhanOfficeTemplate>();

	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/GuoZhanOffice.json"), StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			List<GuozhanOfficeTemplate> jsonObj = mapper.readValue(jsonStr,
					new TypeReference<List<GuozhanOfficeTemplate>>() {
					});
			logger.info("size={}", jsonObj.size());
			guozhanOfficeTemplates = jsonObj;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static void main(String[] args) {
		new GuozhanOfficeTemplateCache().reload();
	}

	public List<GuozhanOfficeTemplate> getTemplates() {
		return guozhanOfficeTemplates;
	}

}
