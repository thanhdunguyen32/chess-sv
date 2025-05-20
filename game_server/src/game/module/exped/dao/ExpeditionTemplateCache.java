package game.module.exped.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ExpeditionTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExpeditionTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExpeditionTemplateCache.class);

    static class SingletonHolder {
        static ExpeditionTemplateCache instance = new ExpeditionTemplateCache();
    }

    public static ExpeditionTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, ExpeditionTemplate> templateMap;

    public void reload() {
        try {
            String fileName = ExpeditionTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, ExpeditionTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<Integer, ExpeditionTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            for (Map.Entry<Integer, ExpeditionTemplate> aEntry : templateWrapperMap.entrySet()) {
                aEntry.getValue().setId(aEntry.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.size();
    }

    public ExpeditionTemplate getExpeditionTemplate(int levelId) {
        return templateMap.get(levelId);
    }

    public static void main(String[] args) {
        new ExpeditionTemplateCache().reload();
    }

}