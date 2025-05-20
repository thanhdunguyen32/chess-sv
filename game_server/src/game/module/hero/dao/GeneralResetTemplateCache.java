package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.GeneralCompTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneralResetTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralResetTemplateCache.class);

    static class SingletonHolder {
        static GeneralResetTemplateCache instance = new GeneralResetTemplateCache();
    }

    public static GeneralResetTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, List<Integer>> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = "dbGeneralReset.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<Integer>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, List<Integer>>>() {
            });
            logger.info("GeneralResetTemplate,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            //
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<Integer> getGeneralResetConfig(int generalStar) {
        return templateMap.get(generalStar);
    }

}