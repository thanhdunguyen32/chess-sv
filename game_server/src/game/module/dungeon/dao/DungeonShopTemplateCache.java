package game.module.dungeon.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyDungeonShopTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DungeonShopTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(DungeonShopTemplateCache.class);

    static class SingletonHolder {
        static DungeonShopTemplateCache instance = new DungeonShopTemplateCache();
    }

    public static DungeonShopTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<MyDungeonShopTemplate> templateMap;

    public void reload() {
        try {
            String fileName = MyDungeonShopTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MyDungeonShopTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MyDungeonShopTemplate>>() {
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

    public List<MyDungeonShopTemplate> getMyDungeonShopTemplates() {
        return templateMap;
    }

    public static void main(String[] args) {
        new DungeonShopTemplateCache().reload();
    }

}