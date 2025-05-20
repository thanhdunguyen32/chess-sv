package game.module.bigbattle.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.BigChapterTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BigChapterTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(BigChapterTemplateCache.class);

    static class SingletonHolder {
        static BigChapterTemplateCache instance = new BigChapterTemplateCache();
    }

    public static BigChapterTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, BigChapterTemplate> templateMap;

    public void reload() {
        try {
            String fileName = BigChapterTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, BigChapterTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, BigChapterTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,BigChapterTemplate> itemPair : templateWrapperMap.entrySet()){
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

    public BigChapterTemplate getBigChapterTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public static void main(String[] args) {
        new BigChapterTemplateCache().reload();
    }

}