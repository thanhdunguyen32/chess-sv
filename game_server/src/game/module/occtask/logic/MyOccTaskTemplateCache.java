package game.module.occtask.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyOccTaskTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyOccTaskTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyOccTaskTemplateCache.class);

    static class SingletonHolder {
        static MyOccTaskTemplateCache instance = new MyOccTaskTemplateCache();
    }

    public static MyOccTaskTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile MyOccTaskTemplate templateMap;

    public void reload() {
        try {
            String fileName = MyOccTaskTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            MyOccTaskTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<MyOccTaskTemplate>() {
            });
            logger.info("size={}", templateWrapperMap.getConfig0().size());
            templateMap = templateWrapperMap;
//            Files.write(JacksonUtils.getInstance().getPrettyJsonStr(templateWrapperMap),new File("data/" + fileName), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<RewardTemplateSimple> getConfig0() {
        return templateMap.getConfig0();
    }

    public MyOccTaskTemplate.OccTaskConfig2 getConfig2() {
        return templateMap.getConfig2();
    }

    public MyOccTaskTemplate.OccTaskConfig3 getConfig3() {
        return templateMap.getConfig3();
    }

    public static void main(String[] args) {
        new MyOccTaskTemplateCache().reload();
    }

}