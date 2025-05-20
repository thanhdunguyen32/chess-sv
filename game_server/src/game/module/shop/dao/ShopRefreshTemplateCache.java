package game.module.shop.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.OnlineGiftTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 任务：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class ShopRefreshTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(ShopRefreshTemplateCache.class);

	static class SingletonHolder {
		static ShopRefreshTemplateCache instance = new ShopRefreshTemplateCache();
	}

	public static ShopRefreshTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile Map<String,Object> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = "dbShopRefresh.json";
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			Map<String,Object> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<String,Object>>() {
			});
			logger.info("size={}", templateWrapperMap.size());
			templateWrapperMap.entrySet().removeIf(shopConfig1 -> shopConfig1.getValue() instanceof Map && ((Map) shopConfig1.getValue()).size() == 0);
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public boolean containsShop(String shopType){
		return templateMap.containsKey(shopType);
	}

	public int getDayShopInterval(){
		return ((Map<String,Integer>)templateMap.get("day")).get("INTERAL");
	}

	public List<Map<String,Integer>> getShopRefreshCosts(String shopType){
		Object shopConfig = templateMap.get(shopType);
		if(shopConfig != null){
			Map<String, Object> manual = (Map<String, Object>) (((Map<String, Object>) shopConfig).get("MANUAL"));
			return (List<Map<String,Integer>>)(manual.get("CAST_ITEMS"));
		}
		return null;
	}

	public static void main(String[] args) {
		ShopRefreshTemplateCache.getInstance().reload();
	}
}
