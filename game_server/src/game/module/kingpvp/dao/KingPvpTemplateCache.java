package game.module.kingpvp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.KingLineupNumTemplate;
import game.module.template.KingMissionDailyTemplate;
import game.module.template.KingStageTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingPvpTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(KingPvpTemplateCache.class);

    static class SingletonHolder {
        static KingPvpTemplateCache instance = new KingPvpTemplateCache();
    }

    public static KingPvpTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<KingLineupNumTemplate> lineupNumTemplates;

    private volatile List<KingMissionDailyTemplate> missionDailyTemplates;

    private volatile Map<Integer,KingStageTemplate> kingStageTemplates;

    public void reload() {
        try {
            String fileName = KingLineupNumTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<KingLineupNumTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<KingLineupNumTemplate>>() {
            });
            logger.info("KingLineupNumTemplate,size={}", templateWrapperMap.size());
            lineupNumTemplates = templateWrapperMap;
            fileName = KingMissionDailyTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<KingMissionDailyTemplate> kingMissionDailyTemplateList = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<KingMissionDailyTemplate>>() {
            });
            logger.info("KingMissionDailyTemplate,size={}", kingMissionDailyTemplateList.size());
            missionDailyTemplates = kingMissionDailyTemplateList;
            fileName = KingStageTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<KingStageTemplate> kingStageTemplateList = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<KingStageTemplate>>() {
            });
            logger.info("KingStageTemplate,size={}", kingStageTemplateList.size());
            kingStageTemplates = new HashMap<>();
            for (KingStageTemplate kingStageTemplate : kingStageTemplateList){
                kingStageTemplates.put(kingStageTemplate.getID(),kingStageTemplate);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<KingLineupNumTemplate> getLineupNumTemplates() {
        return lineupNumTemplates;
    }

    public KingMissionDailyTemplate getMissionDailyTemplate(int missionIndex) {
        return missionDailyTemplates.get(missionIndex);
    }

    public KingStageTemplate getKingStageTemplate(int stage) {
        return kingStageTemplates.get(stage);
    }

    public List<KingMissionDailyTemplate> getMissionDailyTemplates() {
        return missionDailyTemplates;
    }

    public static void main(String[] args) {
        new KingPvpTemplateCache().reload();
    }

}