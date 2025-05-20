package game.module.user.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.HeadFrameTemplate;
import game.module.template.HeadFrameTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HeadFrameTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(HeadFrameTemplateCache.class);

    public boolean containsId(int gsid) {
        return templateMap.containsKey(gsid);
    }

    static class SingletonHolder {
        static HeadFrameTemplateCache instance = new HeadFrameTemplateCache();
    }

    public static HeadFrameTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, HeadFrameTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = HeadFrameTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, HeadFrameTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    HeadFrameTemplate>>() {
            });
            logger.info("HeadFrameTemplate,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            //
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public HeadFrameTemplate getHeadFrameTemplate(int id) {
        return templateMap.get(id);
    }

    public static void main(String[] args) {
        HeadFrameTemplateCache.getInstance().reload();
    }

}