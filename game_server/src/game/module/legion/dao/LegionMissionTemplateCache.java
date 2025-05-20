package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionMissionTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class LegionMissionTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(LegionMissionTemplateCache.class);

    static class SingletonHolder {
        static LegionMissionTemplateCache instance = new LegionMissionTemplateCache();
    }

    public static LegionMissionTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile List<LegionMissionTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = LegionMissionTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<LegionMissionTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<LegionMissionTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getMaxId() {
        return templateMap.size();
    }

    public LegionMissionTemplate getLegionMissionTemplate(int missionId) {
        return templateMap.get(missionId - 1);
    }

    public static void main(String[] args) {
        LegionMissionTemplateCache.getInstance().reload();
    }
}
