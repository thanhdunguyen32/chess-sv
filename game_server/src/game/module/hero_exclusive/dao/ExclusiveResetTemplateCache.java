package game.module.hero_exclusive.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ExclusiveResetTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExclusiveResetTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveResetTemplateCache.class);

    static class SingletonHolder {
        static ExclusiveResetTemplateCache instance = new ExclusiveResetTemplateCache();
    }

    public static ExclusiveResetTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<ExclusiveResetTemplate> templateMap;

    public void reload() {
        try {
            String fileName = ExclusiveResetTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ExclusiveResetTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ExclusiveResetTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public ExclusiveResetTemplate getExclusiveResetTemplate(int level){
        return templateMap.get(level-1);
    }

    public static void main(String[] args) {
        new ExclusiveResetTemplateCache().reload();
    }

}