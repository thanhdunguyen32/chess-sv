package game.module.chapter.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ChapterBattleTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ChapterBattleTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ChapterBattleTemplateCache.class);

    static class SingletonHolder {
        static ChapterBattleTemplateCache instance = new ChapterBattleTemplateCache();
    }

    public static ChapterBattleTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Map<Integer, ChapterBattleTemplate>> templateMap;

    public void reload() {
        try {
            String fileName = ChapterBattleTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
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

    public Map<Integer, ChapterBattleTemplate> getChapterBattleById(int towerId) {
        return templateMap.get(towerId);
    }

    public static void main(String[] args) {
        new ChapterBattleTemplateCache().reload();
    }

}