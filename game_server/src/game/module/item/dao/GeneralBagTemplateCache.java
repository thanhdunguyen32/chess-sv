package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GeneralBagTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GeneralBagTemplateCache.class);

    static class SingletonHolder {
        static GeneralBagTemplateCache instance = new GeneralBagTemplateCache();
    }

    public static GeneralBagTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<Integer> templateMap;

    public void reload() {
        try {
            String fileName = "dbGeneralBag.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<Integer> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<Integer>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.size();
    }

    public int getMoneyCost(int buyCount) {
        return templateMap.get(buyCount);
    }

    public static void main(String[] args) {
        new GeneralBagTemplateCache().reload();
    }

}