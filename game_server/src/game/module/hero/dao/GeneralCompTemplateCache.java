package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.GeneralCompTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class GeneralCompTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralCompTemplateCache.class);

    static class SingletonHolder {
        static GeneralCompTemplateCache instance = new GeneralCompTemplateCache();
    }

    public static GeneralCompTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, GeneralCompTemplate> templateMap;

    private volatile Set<Integer> gComList;

    @Override
    public void reload() {
        try {
            String fileName = GeneralCompTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, GeneralCompTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, GeneralCompTemplate>>() {
            });
            logger.info("GeneralCompTemplate,size={}", templateWrapperMap.size());
            for (Map.Entry<Integer, GeneralCompTemplate> aPair : templateWrapperMap.entrySet()) {
                aPair.getValue().setId(aPair.getKey());
            }
            templateMap = templateWrapperMap;
            //
            fileName = "dbGCompList.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Set<Integer> dbGCompList = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Set<Integer>>() {
            });
            logger.info("dbGCompList,size={}", dbGCompList.size());
            this.gComList = dbGCompList;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(Integer id) {
        return gComList.contains(id);
    }

    public GeneralCompTemplate getGeneralCompTemplate(int id) {
        return templateMap.get(id);
    }

    public static void main(String[] args) {
        GeneralCompTemplateCache.getInstance().reload();
        boolean ret1 = GeneralCompTemplateCache.getInstance().containsId(192);
        System.out.println(ret1);
    }

}