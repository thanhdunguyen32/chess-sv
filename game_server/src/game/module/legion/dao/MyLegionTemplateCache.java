package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyLegionTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class MyLegionTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyLegionTemplateCache.class);

    static class SingletonHolder {
        static MyLegionTemplateCache instance = new MyLegionTemplateCache();
    }

    public static MyLegionTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile MyLegionTemplate templateMap;

    @Override
    public void reload() {
        try {
            String fileName = MyLegionTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            MyLegionTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<MyLegionTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getLevelConfig().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MyLegionTemplate.MyLegionLevelConfig getLegionLevelConfig(int legionLevel) {
        return templateMap.getLevelConfig().get(legionLevel - 1);
    }

    public int getMaxLevel(){
        return templateMap.getLevelConfig().size();
    }

    public MyLegionTemplate getMyLegionTemplate() {
        return templateMap;
    }

    public static void main(String[] args) {
        MyLegionTemplateCache.getInstance().reload();
    }
}
