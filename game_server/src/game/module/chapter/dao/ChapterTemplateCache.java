package game.module.chapter.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ChapterTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ChapterTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ChapterTemplateCache.class);

    static class SingletonHolder {
        static ChapterTemplateCache instance = new ChapterTemplateCache();
    }

    public static ChapterTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, ChapterTemplate> templateMap;

    public void reload() {
        try {
            String fileName = ChapterTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, ChapterTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, ChapterTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,ChapterTemplate> itemPair : templateWrapperMap.entrySet()){
                itemPair.getValue().setId(itemPair.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int templateId) {
        return templateMap.containsKey(templateId);
    }

    public ChapterTemplate getChapterTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new ChapterTemplateCache().reload();
    }

}