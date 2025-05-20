package game.module.legion.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionFDonationTemplate;
import game.module.template.LegionFLevelTemplate;
import game.module.template.OnlineGiftTemplate;
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
public class LegionFLevelTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(LegionFLevelTemplateCache.class);

    static class SingletonHolder {
        static LegionFLevelTemplateCache instance = new LegionFLevelTemplateCache();
    }

    public static LegionFLevelTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile List<LegionFLevelTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = LegionFLevelTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<LegionFLevelTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<LegionFLevelTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public LegionFLevelTemplate getLegionFLevelTemplate(int flevel) {
        return templateMap.get(flevel - 1);
    }

    public static void main(String[] args) {
        LegionFLevelTemplateCache.getInstance().reload();
    }
}
