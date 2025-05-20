package game.module.manor.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ChapterBattleTemplate;
import game.module.template.MyManorTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MyManorTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyManorTemplateCache.class);

    static class SingletonHolder {
        static MyManorTemplateCache instance = new MyManorTemplateCache();
    }

    public static MyManorTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile MyManorTemplate templateMap;

    public void reload() {
        try {
            String fileName = MyManorTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            MyManorTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<MyManorTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getEnemy().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MyManorTemplate.MyManorBossTemplate getBossTemplate(int manorLevel) {
        return templateMap.getBoss().get(manorLevel - 1);
    }

    public List<Map<Integer, ChapterBattleTemplate>> getEnemyTemplate(int manorLevel) {
        List<Map<Integer, ChapterBattleTemplate>> mapList = templateMap.getEnemy().get(manorLevel - 1);
        return mapList;
    }

    public static void main(String[] args) {
        new MyManorTemplateCache().reload();
    }

}