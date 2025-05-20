package game.module.bigbattle.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.BigConfigTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BigConfigTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(BigConfigTemplateCache.class);

    static class SingletonHolder {
        static BigConfigTemplateCache instance = new BigConfigTemplateCache();
    }

    public static BigConfigTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<BigConfigTemplate> templateMap;

    public void reload() {
        try {
            String fileName = BigConfigTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<BigConfigTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<BigConfigTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public BigConfigTemplate getBigConfigTemplateById(int typeIndex) {
        return templateMap.get(typeIndex);
    }

    public static void main(String[] args) {
        new BigConfigTemplateCache().reload();
    }

}