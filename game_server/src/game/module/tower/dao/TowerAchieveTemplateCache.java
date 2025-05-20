package game.module.tower.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.TowerAchieveTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class TowerAchieveTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TowerAchieveTemplateCache.class);

    static class SingletonHolder {
        static TowerAchieveTemplateCache instance = new TowerAchieveTemplateCache();
    }

    public static TowerAchieveTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile TowerAchieveTemplate templateMap;

    public void reload() {
        try {
            String fileName = TowerAchieveTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            TowerAchieveTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<TowerAchieveTemplate>() {
            });
            logger.info("size={}", templateWrapperMap.getACHIEVE().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getMark() {
        return templateMap.getGETMARK();
    }

    public TowerAchieveTemplate.TowerAchieve1 getTowerAchieveTemplate(int rewardIndex) {
        return templateMap.getACHIEVE().get(rewardIndex);
    }

    public static void main(String[] args) {
        new TowerAchieveTemplateCache().reload();
    }

}