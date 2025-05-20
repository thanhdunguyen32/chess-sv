package game.module.guozhan.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import game.module.guozhan.bean.CityListTemplate;
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
public class CityListTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(CityListTemplateCache.class);

	static class SingletonHolder {
		static CityListTemplateCache instance = new CityListTemplateCache();
	}

	public static CityListTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地配置<br/>
	 */
	private volatile List<CityListTemplate> cityListTemplate = new ArrayList<>();

	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/CityList.json"), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        // Thêm cấu hình này
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        
        List<CityListTemplate> jsonObj = mapper.readValue(jsonStr, 
            new TypeReference<List<CityListTemplate>>() {});
        cityListTemplate = jsonObj;
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
	public CityListTemplate getSecretTemp(int cityId) {
		return cityListTemplate.get(cityId);
	}

	public static void main(String[] args) {
		new CityListTemplateCache().reload();
	}

	public int getSize() {
		return cityListTemplate.size();
	}

	public List<CityListTemplate> getCityListTemplates() {
		return cityListTemplate;
	}

}
