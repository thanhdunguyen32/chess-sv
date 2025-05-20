package game.module.manor.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.SurrenderTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SurrenderTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(SurrenderTemplateCache.class);

    static class SingletonHolder {
        static SurrenderTemplateCache instance = new SurrenderTemplateCache();
    }

    public static SurrenderTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, SurrenderTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = SurrenderTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, SurrenderTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    SurrenderTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public SurrenderTemplate getSurrenderTemplate(int chapterId) {
        return templateMap.get(chapterId);
    }

}