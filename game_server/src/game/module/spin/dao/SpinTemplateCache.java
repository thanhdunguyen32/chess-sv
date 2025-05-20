package game.module.spin.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SpinTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(SpinTemplateCache.class);

    static class SingletonHolder {

        static SpinTemplateCache instance = new SpinTemplateCache();
    }

    public static SpinTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<Map<String, Object>> templateMap;

    public void reload() {
        try {
            String fileName = "dbSpin.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<Map<String, Object>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Map<String, Object> getLookStarNormal() {
        return templateMap.get(0);
    }

    public Map<String, Object> getLookStarAdvance() {
        return templateMap.get(1);
    }

    public Map<String, Object> getSpinTemplate(int buy_type) {
        return templateMap.get(buy_type - 1);
    }

    public static void main(String[] args) {
        new SpinTemplateCache().reload();
    }

}