package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.RewardTemplateSimple;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class EquipCompTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(EquipCompTemplateCache.class);

    static class SingletonHolder {
        static EquipCompTemplateCache instance = new EquipCompTemplateCache();
    }

    public static EquipCompTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, List<RewardTemplateSimple>> templateMap;

    public void reload() {
        try {
            String fileName = "dbEquipComp.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<RewardTemplateSimple>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    List<RewardTemplateSimple>>>() {
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

    public List<RewardTemplateSimple> getEquipCompTemplate(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new EquipCompTemplateCache().reload();
    }

}