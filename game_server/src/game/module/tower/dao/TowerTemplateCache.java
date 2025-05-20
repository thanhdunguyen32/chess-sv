package game.module.tower.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.TowerTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TowerTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TowerTemplateCache.class);

    static class SingletonHolder {
        static TowerTemplateCache instance = new TowerTemplateCache();
    }

    public static TowerTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<TowerTemplate> templateMap;

    public void reload() {
        try {
            String fileName = TowerTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<TowerTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<TowerTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public TowerTemplate getTowerTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new TowerTemplateCache().reload();
    }

}