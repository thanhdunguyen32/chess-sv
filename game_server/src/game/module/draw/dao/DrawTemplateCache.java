package game.module.draw.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.DrawTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DrawTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(DrawTemplateCache.class);

    static class SingletonHolder {
        static DrawTemplateCache instance = new DrawTemplateCache();
    }

    public static DrawTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Map<Integer, DrawTemplate>> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = DrawTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, Map<Integer, DrawTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    Map<Integer, DrawTemplate>>>() {
            });
            logger.info("DrawTemplate,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            //
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Map<Integer, DrawTemplate> getDrawTemplate(int id) {
        return templateMap.get(id);
    }

    public static void main(String[] args) {
        DrawTemplateCache.getInstance().reload();
    }

}