package game.module.hero_exclusive.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.KVTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExclusiveWordTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveWordTemplateCache.class);

    static class SingletonHolder {
        static ExclusiveWordTemplateCache instance = new ExclusiveWordTemplateCache();
    }

    public static ExclusiveWordTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, List<KVTemplate>> templateMap;

    public void reload() {
        try {
            String fileName = "dbExclusiveWord.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<KVTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    List<KVTemplate>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<KVTemplate> getWordTemplates(int attrId){
        return templateMap.get(attrId);
    }

    public static void main(String[] args) {
        new ExclusiveWordTemplateCache().reload();
    }

}