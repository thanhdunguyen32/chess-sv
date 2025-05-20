package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.PropHideTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PropHideTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(PropHideTemplateCache.class);

    static class SingletonHolder {
        static PropHideTemplateCache instance = new PropHideTemplateCache();
    }

    public static PropHideTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, PropHideTemplate> templateMap;

    public void reload() {
        try {
            String fileName = PropHideTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, PropHideTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, PropHideTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,PropHideTemplate> itemPair : templateWrapperMap.entrySet()){
                itemPair.getValue().setId(itemPair.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int templateId) {
        return templateMap.containsKey(templateId);
    }

    public PropHideTemplate getPropHideTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new PropHideTemplateCache().reload();
    }

}