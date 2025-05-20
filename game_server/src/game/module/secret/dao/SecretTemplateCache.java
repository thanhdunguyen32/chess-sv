package game.module.secret.dao;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import game.module.secret.bean.SecretTemplate;
import lion.common.Reloadable;

/**
 * 秘密基地：配置表缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年2月4日 下午8:35:44
 */
public class SecretTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(SecretTemplateCache.class);

	static class SingletonHolder {
		static SecretTemplateCache instance = new SecretTemplateCache();
	}

	public static SecretTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 秘密基地配置<br/>
	 */
	private volatile Map<Integer, SecretTemplate> secretTemplMap = new HashMap<Integer, SecretTemplate>();

	@Override
	public void reload() {
		try {
			String jsonStr = Files.toString(new File("data/Secret.json"), StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			List<SecretTemplate> jsonObj = mapper.readValue(jsonStr, new TypeReference<List<SecretTemplate>>() {
			});
			logger.info("size={}", jsonObj.size());
			// 创建map
			Map<Integer, SecretTemplate> tmpMap = new HashMap<>();
			for (SecretTemplate levelsTemplate : jsonObj) {
				tmpMap.put(levelsTemplate.getId(), levelsTemplate);
			}
			secretTemplMap = tmpMap;
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
	public SecretTemplate getSecretTemp(int id) {
		return secretTemplMap.get(id);
	}

	public static void main(String[] args) {
		new SecretTemplateCache().reload();
	}

}
