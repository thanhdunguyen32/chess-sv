package game.module.pvp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.PvpDayRewardTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PvpRewardTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(PvpRewardTemplateCache.class);

    static class SingletonHolder {
        static PvpRewardTemplateCache instance = new PvpRewardTemplateCache();
    }

    public static PvpRewardTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<PvpDayRewardTemplate> templateMap;

    private volatile List<PvpDayRewardTemplate> weekRewardTemplates;

    public void reload() {
        try {
            String fileName = PvpDayRewardTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<PvpDayRewardTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<PvpDayRewardTemplate>>() {
            });
            logger.info("PvpDayRewards,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            //week
            fileName = "dbPvpWeekReward.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<PvpDayRewardTemplate>>() {
            });
            logger.info("PvpWeekRewards,size={}", templateWrapperMap.size());
            weekRewardTemplates = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<PvpDayRewardTemplate> getTemplateMap(){
        return templateMap;
    }

    public List<PvpDayRewardTemplate> getWeekRewardTemplates(){
        return weekRewardTemplates;
    }

    public static void main(String[] args) {
        new PvpRewardTemplateCache().reload();
    }

}