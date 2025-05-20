package game.module.chapter.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyChapterTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyChapterTemplateCache.class);

    static class SingletonHolder {
        static MyChapterTemplateCache instance = new MyChapterTemplateCache();
    }

    public static MyChapterTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Integer> templateMap;

    public void reload() {
        try {
            String fileName = "myChapter.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, Integer> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, Integer>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Integer getItemDropRate(int gsid) {
        return templateMap.get(gsid);
    }

    public static void main(String[] args) {
        new MyChapterTemplateCache().reload();
    }

}