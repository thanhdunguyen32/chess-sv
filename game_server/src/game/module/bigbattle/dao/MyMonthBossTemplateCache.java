package game.module.bigbattle.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyMonthBossTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyMonthBossTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyMonthBossTemplateCache.class);

    static class SingletonHolder {
        static MyMonthBossTemplateCache instance = new MyMonthBossTemplateCache();
    }

    public static MyMonthBossTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<List<MyMonthBossTemplate>> templateMap;

    public void reload() {
        try {
            String fileName = MyMonthBossTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<List<MyMonthBossTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<List<MyMonthBossTemplate>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MyMonthBossTemplate getMonthBossConfig(int bigIndex, int smallIndex) {
        return templateMap.get(bigIndex).get(smallIndex);
    }

    public List<MyMonthBossTemplate> getMonthBossConfig(int bigIndex){
        return templateMap.get(bigIndex);
    }

    public List<List<MyMonthBossTemplate>> getMonthBossConfig() {
        return templateMap;
    }

    public static void main(String[] args) {
        new MyMonthBossTemplateCache().reload();
    }

}