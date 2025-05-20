package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.RewardTemplateSimple;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GeneralLevelTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralLevelTemplateCache.class);

    static class SingletonHolder {

        static GeneralLevelTemplateCache instance = new GeneralLevelTemplateCache();
    }
    public static GeneralLevelTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<List<RewardTemplateSimple>> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = "dbGeneralLevel.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<List<RewardTemplateSimple>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<List<RewardTemplateSimple>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<RewardTemplateSimple> getConfigByLevel(int aLevel) {
        return templateMap.get(aLevel);
    }

    public List<List<RewardTemplateSimple>> getConfigAll() {
        return templateMap;
    }

    public int getMaxLevel() {
        return templateMap.size();
    }

    public static void main(String[] args) {
        GeneralLevelTemplateCache.getInstance().reload();
//		List<String> retlist = LevelsTemplateCache.getInstance()
//				.getDataListFromLine("30,600,\"[[2,1,5000],[2,2,5000],[2,3,5000],[4,5031,2],[4,20001,2]]\"");
//		logger.info("{}",retlist);
    }

}