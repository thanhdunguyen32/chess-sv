package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.ChapterBattleTemplate;
import game.module.template.MyLegionBossConfig;
import game.module.template.MyLegionTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MyLegionBossTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyLegionBossTemplateCache.class);

    static class SingletonHolder {
        static MyLegionBossTemplateCache instance = new MyLegionBossTemplateCache();
    }

    public static MyLegionBossTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<MyLegionBossConfig> templateMap;

    public void reload() {
        try {
            String fileName = MyLegionBossConfig.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MyLegionBossConfig> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MyLegionBossConfig>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    public MyLegionBossConfig getLegionBossTemplate(int chapterIndex) {
        return templateMap.get(chapterIndex);
    }

    public static void main(String[] args) {
        new MyLegionBossTemplateCache().reload();
    }

}