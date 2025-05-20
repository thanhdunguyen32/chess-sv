package game.module.manor.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.OfficialItemsTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OfficialItemsTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(OfficialItemsTemplateCache.class);

    static class SingletonHolder {
        static OfficialItemsTemplateCache instance = new OfficialItemsTemplateCache();
    }

    public static OfficialItemsTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<OfficialItemsTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = OfficialItemsTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<OfficialItemsTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<OfficialItemsTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public OfficialItemsTemplate getOfficialItemsTemplate(int id) {
        return templateMap.get(id);
    }

}