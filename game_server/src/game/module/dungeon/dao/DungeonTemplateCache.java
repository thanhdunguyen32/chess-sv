package game.module.dungeon.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyDungeonTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DungeonTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(DungeonTemplateCache.class);

    static class SingletonHolder {
        static DungeonTemplateCache instance = new DungeonTemplateCache();
    }

    public static DungeonTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile MyDungeonTemplate templateMap;

    public void reload() {
        try {
            String fileName = MyDungeonTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            MyDungeonTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<MyDungeonTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getSteps().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.getSteps().size();
    }

    public MyDungeonTemplate.MyDungeonTemplateStep getDungeonTemplateStep(int chapterIndex){
        return templateMap.getSteps().get(chapterIndex);
    }

    public List<MyDungeonTemplate.MyDungeonTemplateShop> getDungeonTemplateShop() {
        return templateMap.getShops();
    }

    public static void main(String[] args) {
        new DungeonTemplateCache().reload();
    }

}