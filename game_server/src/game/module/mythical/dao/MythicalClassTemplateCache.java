package game.module.mythical.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MythicalClassTemplate;
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
public class MythicalClassTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MythicalClassTemplateCache.class);

    static class SingletonHolder {
        static MythicalClassTemplateCache instance = new MythicalClassTemplateCache();
    }

    public static MythicalClassTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有任务<br/>
     * Key：任务类型 1主线任务，2日常任务，3多天任务
     */
    private volatile List<MythicalClassTemplate> templateMap;

    /**
     * 所有的任务进程
     *
     * @return
     */
    public MythicalClassTemplate getMythicalClassTemplate(int classId) {
        return templateMap.get(classId);
    }

    public int getClassSize() {
        return templateMap.size();
    }

    @Override
    public void reload() {
        try {
            String fileName = MythicalClassTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MythicalClassTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MythicalClassTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {
        MythicalClassTemplateCache.getInstance().reload();
    }
}
