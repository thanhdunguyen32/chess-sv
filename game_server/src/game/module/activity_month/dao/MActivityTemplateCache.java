package game.module.activity_month.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.MActivityTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MActivityTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MActivityTemplateCache.class);

    static class SingletonHolder {
        static MActivityTemplateCache instance = new MActivityTemplateCache();
    }

    public static MActivityTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<MActivityTemplate> affairsTemplateMap;
    private volatile List<MActivityTemplate> expedTemplateMap;
    private volatile List<MActivityTemplate> gStarTemplateMap;
    private volatile List<MActivityTemplate> pvpTemplateMap;

    public void reload() {
        try {
            String fileName = "dbMActivityAffairs.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MActivityTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MActivityTemplate>>() {
                    });
            logger.info("dbMActivityAffairs,size={}", templateWrapperMap.size());
            affairsTemplateMap = templateWrapperMap;

            fileName = "dbMActivityExped.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MActivityTemplate> dbMActivityExpedMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MActivityTemplate>>() {
                    });
            logger.info("dbMActivityExped,size={}", dbMActivityExpedMap.size());
            expedTemplateMap = dbMActivityExpedMap;

            fileName = "dbMActivityGstar.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MActivityTemplate> dbMActivityGstarMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MActivityTemplate>>() {
                    });
            logger.info("dbMActivityGstar,size={}", dbMActivityGstarMap.size());
            gStarTemplateMap = dbMActivityGstarMap;

            fileName = "dbMActivityPvp.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MActivityTemplate> dbMActivityPvpMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MActivityTemplate>>() {
                    });
            logger.info("dbMActivityPvp,size={}", dbMActivityPvpMap.size());
            pvpTemplateMap = dbMActivityPvpMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<MActivityTemplate> getAffairsTemplateMap() {
        return affairsTemplateMap;
    }

    public List<MActivityTemplate> getExpedTemplateMap() {
        return expedTemplateMap;
    }

    public List<MActivityTemplate> getgStarTemplateMap() {
        return gStarTemplateMap;
    }

    public List<MActivityTemplate> getPvpTemplateMap() {
        return pvpTemplateMap;
    }

    public static void main(String[] args) {
        new MActivityTemplateCache().reload();
    }

}