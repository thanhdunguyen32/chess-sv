package game.module.exped.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ExpedStatueTemplate;
import game.module.template.ExpedStatueTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExpedStatueTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExpedStatueTemplateCache.class);

    static class SingletonHolder {
        static ExpedStatueTemplateCache instance = new ExpedStatueTemplateCache();
    }

    public static ExpedStatueTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, ExpedStatueTemplate> templateMap;

    public void reload() {
        try {
            String fileName = ExpedStatueTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, ExpedStatueTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, ExpedStatueTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public ExpedStatueTemplate getExpedStatueTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new ExpedStatueTemplateCache().reload();
    }

}