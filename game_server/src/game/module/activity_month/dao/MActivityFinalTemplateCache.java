package game.module.activity_month.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MActivityFinalTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MActivityFinalTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MActivityFinalTemplateCache.class);

    static class SingletonHolder {
        static MActivityFinalTemplateCache instance = new MActivityFinalTemplateCache();
    }

    public static MActivityFinalTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<MActivityFinalTemplate> templateMap;

    public void reload() {
        try {
            String fileName = MActivityFinalTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MActivityFinalTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MActivityFinalTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.size();
    }

    public MActivityFinalTemplate getMActivityFinalTemplate(int levelId) {
        return templateMap.get(levelId);
    }

    public List<MActivityFinalTemplate> getMActivityFinalTemplate() {
        return templateMap;
    }

    public static void main(String[] args) {
        new MActivityFinalTemplateCache().reload();
    }

}