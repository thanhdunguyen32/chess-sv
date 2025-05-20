package game.module.mythical.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MythicalActiveSkillTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 任务：配置文件缓存
 *
 * @author zhangning
 */
public class MythicalActiveSkillTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MythicalActiveSkillTemplateCache.class);

    static class SingletonHolder {
        static MythicalActiveSkillTemplateCache instance = new MythicalActiveSkillTemplateCache();
    }

    public static MythicalActiveSkillTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile Map<Integer, List<MythicalActiveSkillTemplate>> templateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public List<MythicalActiveSkillTemplate> getMythicalActiveSkillTemplate(int templateId) {
        return templateMap.get(templateId);
    }

    @Override
    public void reload() {
        try {
            String fileName = MythicalActiveSkillTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, List<MythicalActiveSkillTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    List<MythicalActiveSkillTemplate>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        MythicalActiveSkillTemplateCache.getInstance().reload();
    }
}
