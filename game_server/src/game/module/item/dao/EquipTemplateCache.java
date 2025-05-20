package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.EquipTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EquipTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(EquipTemplateCache.class);

    static class SingletonHolder {
        static EquipTemplateCache instance = new EquipTemplateCache();
    }

    public static EquipTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, EquipTemplate> templateMap;

    public void reload() {
        try {
            String fileName = EquipTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, EquipTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, EquipTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,EquipTemplate> itemPair : templateWrapperMap.entrySet()){
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

    public EquipTemplate getEquipTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new EquipTemplateCache().reload();
    }

}