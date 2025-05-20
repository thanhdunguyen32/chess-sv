package game.module.mission.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MissionAchieveTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class MissionAchieveTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MissionAchieveTemplateCache.class);

    static class SingletonHolder {
        static MissionAchieveTemplateCache instance = new MissionAchieveTemplateCache();
    }

    public static MissionAchieveTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile List<MissionAchieveTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = MissionAchieveTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MissionAchieveTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MissionAchieveTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize(){
        return templateMap.size();
    }

    public MissionAchieveTemplate getMissionAchieveTemplate(int missionTypeIndex) {
        return templateMap.get(missionTypeIndex);
    }

    public static void main(String[] args) {
        MissionAchieveTemplateCache.getInstance().reload();
    }

}
