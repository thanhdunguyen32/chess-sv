package game.module.award.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.FixAwardTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixAwardTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(FixAwardTemplateCache.class);

    static class SingletonHolder {
        static FixAwardTemplateCache instance = new FixAwardTemplateCache();
    }

    public static FixAwardTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, FixAwardTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = FixAwardTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, FixAwardTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, FixAwardTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public FixAwardTemplate getFixAwardTemplateById(int awardId) {
        return templateMap.get(awardId);
    }

    public static void main(String[] args) {
        new FixAwardTemplateCache().reload();
    }

}
