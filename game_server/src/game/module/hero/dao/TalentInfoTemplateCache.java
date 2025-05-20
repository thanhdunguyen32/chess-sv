package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.TalentInfoTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TalentInfoTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TalentInfoTemplateCache.class);

    static class SingletonHolder {
        static TalentInfoTemplateCache instance = new TalentInfoTemplateCache();
    }

    public static TalentInfoTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, TalentInfoTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = TalentInfoTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer,TalentInfoTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,TalentInfoTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,TalentInfoTemplate> templateEntry : templateWrapperMap.entrySet()){
                templateEntry.getValue().setId(templateEntry.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int id) {
        return templateMap.containsKey(id);
    }

    public int getTalentInfoLock(int id) {
        return templateMap.get(id).getLOCK();
    }

    public static void main(String[] args) {
        TalentInfoTemplateCache.getInstance().reload();
        int ret1 = TalentInfoTemplateCache.getInstance().getTalentInfoLock(36083);
        System.out.println(ret1);
    }

}