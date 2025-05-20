package game.module.mythical.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.RewardTemplateSimple;
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
public class MythicalLvTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MythicalLvTemplateCache.class);

    static class SingletonHolder {
        static MythicalLvTemplateCache instance = new MythicalLvTemplateCache();
    }

    public static MythicalLvTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile List<List<RewardTemplateSimple>> templateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public List<RewardTemplateSimple> getMythicalLvTemplate(int templateId) {
        return templateMap.get(templateId);
    }

    public List<List<RewardTemplateSimple>> getTemplateAll(){
        return templateMap;
    }

    public int getRewardSize() {
        return templateMap.size();
    }

    @Override
    public void reload() {
        try {
            String fileName = "dbMythicalLv.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<List<RewardTemplateSimple>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<List<RewardTemplateSimple>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        MythicalLvTemplateCache.getInstance().reload();
    }
}
