package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.GeneralChipTemplate;
import game.module.template.CBoxTemplate;
import game.module.template.RewardTemplateChance;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(GiftTemplateCache.class);

    static class SingletonHolder {
        static GiftTemplateCache instance = new GiftTemplateCache();
    }

    public static GiftTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, CBoxTemplate> templateMap;

    public void reload() {
        try {
            String fileName = "dbGift.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, CBoxTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, CBoxTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for (Map.Entry<Integer, CBoxTemplate> itemPair : templateWrapperMap.entrySet()) {
                itemPair.getValue().setId(itemPair.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public CBoxTemplate getGiftTemplate(int gsid){
        return templateMap.get(gsid);
    }

    public static void main(String[] args) {
        new GiftTemplateCache().reload();
    }

}