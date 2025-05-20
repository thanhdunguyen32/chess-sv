package game.module.battle.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.RoleInfoTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RoleInfoTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(RoleInfoTemplateCache.class);

    static class SingletonHolder {
        static RoleInfoTemplateCache instance = new RoleInfoTemplateCache();
    }

    public static RoleInfoTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<String, RoleInfoTemplate> templateMap;

    public void reload() {
        try {
            String fileName = RoleInfoTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<String, RoleInfoTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<String, RoleInfoTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int templateId) {
        return templateMap.containsKey(templateId);
    }

    public RoleInfoTemplate getRoleInfoTemplateById(String modelId) {
        return templateMap.get(modelId);
    }

    public static void main(String[] args) {
        new RoleInfoTemplateCache().reload();
    }

}