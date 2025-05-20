package game.module.affair.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.AffairRewardTemplate;
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
public class AffairRewardTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(AffairRewardTemplateCache.class);

    static class SingletonHolder {
        static AffairRewardTemplateCache instance = new AffairRewardTemplateCache();
    }

    public static AffairRewardTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile Map<Integer, AffairRewardTemplate> templateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public AffairRewardTemplate getAffairRewardTemplate(int taskId) {
        return templateMap.get(taskId);
    }

    @Override
    public void reload() {
        try {
            String fileName = AffairRewardTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<AffairRewardTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<AffairRewardTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            Map<Integer, AffairRewardTemplate> templateMap1 = new HashMap<>();
            for (AffairRewardTemplate affairRewardTemplate : templateWrapperMap) {
                templateMap1.put(affairRewardTemplate.getId(), affairRewardTemplate);
            }
            templateMap = templateMap1;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        AffairRewardTemplateCache.getInstance().reload();
    }
}
