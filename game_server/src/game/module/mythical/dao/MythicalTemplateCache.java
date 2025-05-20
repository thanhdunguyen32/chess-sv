package game.module.mythical.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MythicalTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class MythicalTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MythicalTemplateCache.class);

    static class SingletonHolder {

        static MythicalTemplateCache instance = new MythicalTemplateCache();
    }
    public static MythicalTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile Map<Integer, MythicalTemplate> templateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public MythicalTemplate getMythicalTemplate(int templateId) {
        return templateMap.get(templateId);
    }

    public int getRewardSize() {
        return templateMap.size();
    }

    @Override
    public void reload() {
        try {
            String fileName = MythicalTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, MythicalTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, MythicalTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for (Map.Entry<Integer, MythicalTemplate> aEntry : templateWrapperMap.entrySet()) {
                aEntry.getValue().setId(aEntry.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsTemplate(int mythicalId) {
        return templateMap.containsKey(mythicalId);
    }

    public static void main(String[] args) {
        MythicalTemplateCache.getInstance().reload();
    }

    public Set<Integer> getMythicalTemplateIds() {
        return templateMap.keySet();
    }
}
