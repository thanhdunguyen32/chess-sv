package game.module.activity.dao;

import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SpPackStarTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(SpPackStarTemplateCache.class);

    static class SingletonHolder {
        static SpPackStarTemplateCache instance = new SpPackStarTemplateCache();
    }

    public static SpPackStarTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, Integer> templateMap;

    public void reload() {
        try {
            String fileName = "dbSpPackStar.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<String, Map<String, Integer>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, Map.class);
            logger.info("size={}", templateWrapperMap.size());
            templateMap = new HashMap<>();
            for (String gstar_s : templateWrapperMap.keySet()) {
                int gstar = Integer.parseInt(gstar_s);
                int dayLimit = templateWrapperMap.get(gstar_s).get("LIMIT");
                templateMap.put(gstar, dayLimit);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Integer getGstarXiangouLimit(int gstar) {
        return templateMap.get(gstar);
    }

    public static void main(String[] args) {
        new SpPackStarTemplateCache().reload();
    }

}