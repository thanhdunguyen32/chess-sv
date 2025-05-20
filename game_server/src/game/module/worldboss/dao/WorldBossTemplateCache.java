package game.module.worldboss.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.WorldBossTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WorldBossTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(WorldBossTemplateCache.class);

    static class SingletonHolder {
        static WorldBossTemplateCache instance = new WorldBossTemplateCache();
    }

    public static WorldBossTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<WorldBossTemplate> templateMap;

    public void reload() {
        try {
            String fileName = WorldBossTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<WorldBossTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<WorldBossTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public WorldBossTemplate getWorldBossTemplate(int chapterIndex) {
        return templateMap.get(chapterIndex);
    }

    public static void main(String[] args) {
        new WorldBossTemplateCache().reload();
    }

}