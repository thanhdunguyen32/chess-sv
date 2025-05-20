package game.module.hero_exclusive.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ExclusiveTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExclusiveTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveTemplateCache.class);

    public boolean containsId(int gsid) {
        return false;
    }

    static class SingletonHolder {
        static ExclusiveTemplateCache instance = new ExclusiveTemplateCache();
    }

    public static ExclusiveTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<ExclusiveTemplate> templateMap;

    public void reload() {
        try {
            String fileName = ExclusiveTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<ExclusiveTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    List<ExclusiveTemplate>>>() {
            });
            List<ExclusiveTemplate> templateList = templateWrapperMap.values().iterator().next();
            logger.info("size={}", templateList.size());
            templateMap = templateList;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public ExclusiveTemplate getExclusiveTemplate(int exclusiveLevel) {
        return templateMap.get(exclusiveLevel - 1);
    }

    public static void main(String[] args) {
        new ExclusiveTemplateCache().reload();
    }

}