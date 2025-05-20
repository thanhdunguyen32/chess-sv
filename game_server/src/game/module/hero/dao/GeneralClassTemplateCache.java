package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.GeneralClassTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GeneralClassTemplateCache implements Reloadable {

    private static final Logger logger = LoggerFactory.getLogger(GeneralClassTemplateCache.class);

    static class SingletonHolder {

        static GeneralClassTemplateCache instance = new GeneralClassTemplateCache();
    }
    public static GeneralClassTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<GeneralClassTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = GeneralClassTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<GeneralClassTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<GeneralClassTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public GeneralClassTemplate getConfigByLevel(int aLevel) {
        return templateMap.get(aLevel);
    }

    public List<GeneralClassTemplate> getConfigAll() {
        return templateMap;
    }

    public int getMaxClass() {
        return templateMap.size();
    }

    public static void main(String[] args) {
        GeneralClassTemplateCache.getInstance().reload();
    }

}