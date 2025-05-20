package game.module.user.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlayerLevelTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(PlayerLevelTemplateCache.class);

	static class SingletonHolder {
		static PlayerLevelTemplateCache instance = new PlayerLevelTemplateCache();
	}

	public static PlayerLevelTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile List<Integer> templateList;

	@Override
	public void reload() {
		try {
			String fileName = "dbExp.json";
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			List<Integer> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<Integer>>() {
			});
			logger.info("size={}", templateWrapperMap.size());
			templateList = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public Integer getConfigByLevel(int aLevel) {
		if (aLevel >= templateList.size()) {
			return null;
		} else {
			return templateList.get(aLevel);
		}
	}

	public int getPlayerNextExpByLv(int playerLevel) {
		int ret = 0;
		for (int i = 0; i < playerLevel + 1; i++) {
			ret += templateList.get(i);
		}
		return ret;
	}

	public int getMaxLevel() {
		return templateList.size();
	}

	public static void main(String[] args) {
		PlayerLevelTemplateCache.getInstance().reload();
//		List<String> retlist = LevelsTemplateCache.getInstance()
//				.getDataListFromLine("30,600,\"[[2,1,5000],[2,2,5000],[2,3,5000],[4,5031,2],[4,20001,2]]\"");
//		logger.info("{}",retlist);
	}

}