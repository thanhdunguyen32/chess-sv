package game.module.friend.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.FchapterTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FchapterTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(FchapterTemplateCache.class);

    static class SingletonHolder {
        static FchapterTemplateCache instance = new FchapterTemplateCache();
    }

    public static FchapterTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<FchapterTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = FchapterTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<FchapterTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<FchapterTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public FchapterTemplate getFchapterTemplates(int officialIndex) {
        return templateMap.get(officialIndex);
    }

    public List<FchapterTemplate> getChapterList(){
        return templateMap;
    }

}