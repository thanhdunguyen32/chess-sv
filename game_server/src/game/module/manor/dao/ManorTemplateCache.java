package game.module.manor.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ManorTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ManorTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ManorTemplateCache.class);

    static class SingletonHolder {
        static ManorTemplateCache instance = new ManorTemplateCache();
    }

    public static ManorTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile ManorTemplate templateMap;

    public void reload() {
        try {
            String fileName = ManorTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ManorTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<ManorTemplate>() {
            });
            logger.info("size={}", templateWrapperMap.getHOME().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public ManorTemplate.ManorHomeTemplate getManorHomeTemplate(int manorLevel) {
        return templateMap.getHOME().get(manorLevel - 1);
    }

    public ManorTemplate.ManorUpTemplate getUpTemplate(int buildingId) {
        return templateMap.getUP().get(buildingId);
    }

    public ManorTemplate.ManorMaxTemplate getMaxTemplate(int buildingId) {
        return templateMap.getMAX().get(buildingId);
    }

    public ManorTemplate.ManorEnemyTemplate getEnemyTemplate(int levelIndex) {
        return templateMap.getENEMY().get(levelIndex);
    }

    public ManorTemplate.ManorEnemyTemplate getBossTemplate(int levelIndex) {
        return templateMap.getBOSS().get(levelIndex);
    }

    public Map<Integer, Integer> getFight() {
        return templateMap.getFIGHT();
    }

    public static void main(String[] args) {
        new ManorTemplateCache().reload();
    }

}