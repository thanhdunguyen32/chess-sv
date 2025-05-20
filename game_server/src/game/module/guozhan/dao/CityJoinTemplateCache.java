package game.module.guozhan.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import game.module.item.bean.TemplateList;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 秘密基地：配置表缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年2月4日 下午8:35:44
 */
public class CityJoinTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(CityJoinTemplateCache.class);

	static class SingletonHolder {
		static CityJoinTemplateCache instance = new CityJoinTemplateCache();
	}

	public static CityJoinTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地配置<br/>
	 */
	private volatile TemplateList cityJoinList = new TemplateList();

	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/CityLianjie.json"), StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			TemplateList jsonObj = mapper.readValue(jsonStr, TemplateList.class);
			logger.info("size={}", jsonObj.size());
			cityJoinList = jsonObj;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 通过Id查找秘密基地
	 * 
	 * @param id
	 * @return
	 */
	public Map<String,Object> getSecretTemp(int cityId) {
		return cityJoinList.get(cityId-1);
	}

	public static void main(String[] args) {
		new CityJoinTemplateCache().reload();
	}

	public int getSize() {
		return cityJoinList.size();
	}

}
