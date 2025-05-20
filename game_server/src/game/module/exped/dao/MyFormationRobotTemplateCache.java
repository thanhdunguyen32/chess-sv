package game.module.exped.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyFormationRobotTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyFormationRobotTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyFormationRobotTemplateCache.class);

    static class SingletonHolder {
        static MyFormationRobotTemplateCache instance = new MyFormationRobotTemplateCache();
    }

    public static MyFormationRobotTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, MyFormationRobotTemplate> templateMap;

    public void reload() {
        try {
            String fileName = MyFormationRobotTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, MyFormationRobotTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<Integer, MyFormationRobotTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
//            String prettyJsonStr = JacksonUtils.getInstance().getPrettyJsonStr(templateWrapperMap);
//            Files.write(prettyJsonStr,new File("data/"+"myFormationRobot"+".json"), Charset.forName("UTF-8"));
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.size();
    }

    public MyFormationRobotTemplate getMyFormationRobotTemplate(int levelId) {
        return templateMap.get(levelId);
    }

    public static void main(String[] args) {
        new MyFormationRobotTemplateCache().reload();
    }

}