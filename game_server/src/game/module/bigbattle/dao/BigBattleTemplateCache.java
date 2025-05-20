package game.module.bigbattle.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ChapterBattleTemplate;
import game.module.template.ChapterTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BigBattleTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(BigBattleTemplateCache.class);

    static class SingletonHolder {
        static BigBattleTemplateCache instance = new BigBattleTemplateCache();
    }

    public static BigBattleTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Map<Integer, ChapterBattleTemplate>> templateMap;

    public void reload() {
        try {
            String fileName = "dbBigBattle.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, Map<Integer, ChapterBattleTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<Integer, Map<Integer, ChapterBattleTemplate>>>() {
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

    public Map<Integer, ChapterBattleTemplate> getBigBattleByMapId(int mapId) {
        return templateMap.get(mapId);
    }

    public static void main(String[] args) {
        new BigBattleTemplateCache().reload();
    }

}