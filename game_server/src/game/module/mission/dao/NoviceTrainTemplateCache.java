package game.module.mission.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.NoviceTrainTemplate;
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
public class NoviceTrainTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(NoviceTrainTemplateCache.class);

    static class SingletonHolder {
        static NoviceTrainTemplateCache instance = new NoviceTrainTemplateCache();
    }

    public static NoviceTrainTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile Map<String, NoviceTrainTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = NoviceTrainTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<String, List<NoviceTrainTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<String,
                    List<NoviceTrainTemplate>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = new HashMap<>();
            for (List<NoviceTrainTemplate> noviceTrainTemplates : templateWrapperMap.values()) {
                for (NoviceTrainTemplate noviceTrainTemplate : noviceTrainTemplates) {
                    templateMap.put(noviceTrainTemplate.getEXID(), noviceTrainTemplate);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public NoviceTrainTemplate getNoviceTrainTemplate(String exid) {
        return templateMap.get(exid);
    }

    public static void main(String[] args) {
        NoviceTrainTemplateCache.getInstance().reload();
    }

}
