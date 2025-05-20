package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GeneralAwakeTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralAwakeTemplateCache.class);

    static class SingletonHolder {

        static GeneralAwakeTemplateCache instance = new GeneralAwakeTemplateCache();
    }

    public static GeneralAwakeTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Integer> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = "dbGeneralAwake.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, Integer> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, Integer>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Integer getAwakeGsid(int rawGsid) {
        return templateMap.get(rawGsid);
    }

    public static void main(String[] args) {
        GeneralAwakeTemplateCache.getInstance().reload();
    }

}