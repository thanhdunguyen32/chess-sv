package game.module.hero_exclusive.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExclusiveSkillTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveSkillTemplateCache.class);

    static class SingletonHolder {
        static ExclusiveSkillTemplateCache instance = new ExclusiveSkillTemplateCache();
    }

    public static ExclusiveSkillTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer,List<Integer>> templateMap;

    public void reload() {
        try {
            String fileName = "dbExclusiveSkill.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<Integer>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, List<Integer>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<Integer> getSkillIds(int skillId){
        return templateMap.get(skillId);
    }

    public static void main(String[] args) {
        new ExclusiveSkillTemplateCache().reload();
    }

}