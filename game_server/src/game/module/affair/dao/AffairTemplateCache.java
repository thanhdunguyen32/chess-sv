package game.module.affair.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.AffairsTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class AffairTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(AffairTemplateCache.class);

    static class SingletonHolder {
        static AffairTemplateCache instance = new AffairTemplateCache();
    }

    public static AffairTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile Map<Integer, AffairsTemplate> templateMap;

    private volatile Map<Integer, List<AffairsTemplate>> qualityTemplateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public AffairsTemplate getAffairsTemplate(int taskId) {
        return templateMap.get(taskId);
    }

    public List<AffairsTemplate> getAffairsByQuality(int starId) {
        return qualityTemplateMap.get(starId);
    }

    @Override
    public void reload() {
        try {
            String fileName = AffairsTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<AffairsTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<AffairsTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            Map<Integer, AffairsTemplate> templateMap1 = new HashMap<>();
            Map<Integer, List<AffairsTemplate>> qualityTemplateMap1 = new HashMap<>();
            for (AffairsTemplate affairsTemplate : templateWrapperMap) {
                int templateId = affairsTemplate.getID();
                templateMap1.put(templateId, affairsTemplate);
                Integer affairStar = affairsTemplate.getSTAR();
                if (qualityTemplateMap1.get(affairStar) == null) {
                    List<AffairsTemplate> templates = new ArrayList<>();
                    qualityTemplateMap1.put(affairStar, templates);
                }
                qualityTemplateMap1.get(affairStar).add(affairsTemplate);
            }
            templateMap = templateMap1;
            qualityTemplateMap = qualityTemplateMap1;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        AffairTemplateCache.getInstance().reload();
    }
}
