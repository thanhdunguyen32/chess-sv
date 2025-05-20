package game.module.online.dao;

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
import java.util.List;

/**
 * 任务：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class OnlineGiftTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(OnlineGiftTemplateCache.class);

    static class SingletonHolder {
		static OnlineGiftTemplateCache instance = new OnlineGiftTemplateCache();
	}

	public static OnlineGiftTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 所有任务<br/>
	 * Key：任务类型 1主线任务，2日常任务，3多天任务
	 */
	private volatile List<OnlineGiftTemplate.OnlineGiftTemplateReward> templateMap;

	private int giveSign;

	/**
	 * 所有的任务进程
	 * 
	 * @return
	 */
	public OnlineGiftTemplate.OnlineGiftTemplateReward getOnlineReward(int aIndex) {
		return templateMap.get(aIndex);
	}

	public int getRewardSize(){
		return templateMap.size();
	}

	@Override
	public void reload() {
		try {
			String fileName = OnlineGiftTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			OnlineGiftTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<OnlineGiftTemplate>() {
			});
			logger.info("size={}", templateWrapperMap.getREWARD().size());
			templateMap = templateWrapperMap.getREWARD();
			giveSign = templateWrapperMap.getGIVE_SIGN();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public int getGiveSign() {
		return giveSign;
	}

	public static void main(String[] args) {
		OnlineGiftTemplateCache.getInstance().reload();
	}
}
