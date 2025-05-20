package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.TreasureChipTemplate;
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

public class TreasureChipTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TreasureChipTemplateCache.class);

    static class SingletonHolder {
        static TreasureChipTemplateCache instance = new TreasureChipTemplateCache();
    }

    public static TreasureChipTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, TreasureChipTemplate> templateMap;
    private volatile Map<Integer, List<TreasureChipTemplate>> qualityTemplateMap;

    @Override
    public void reload() {
        try {
            String fileName = TreasureChipTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, TreasureChipTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    TreasureChipTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            Map<Integer, List<TreasureChipTemplate>> qualityTemplateMapTmp = new HashMap<>();
            for (TreasureChipTemplate treasureChipTemplate : templateWrapperMap.values()) {
                if (treasureChipTemplate.getCTYPE() == 1) {
                    qualityTemplateMapTmp.putIfAbsent(treasureChipTemplate.getQUALITY(), new ArrayList<>());
                    qualityTemplateMapTmp.get(treasureChipTemplate.getQUALITY()).add(treasureChipTemplate);
                }
            }
            qualityTemplateMap = qualityTemplateMapTmp;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int id) {
        return templateMap.containsKey(id);
    }

    public TreasureChipTemplate getTreasureChipTemplate(int id) {
        return templateMap.get(id);
    }

    public List<TreasureChipTemplate> getTreasureChipTemplateByQuality(int aQuality){
        return qualityTemplateMap.get(aQuality);
    }

    public static void main(String[] args) {
        TreasureChipTemplateCache.getInstance().reload();
        boolean ret1 = TreasureChipTemplateCache.getInstance().containsId(192);
        System.out.println(ret1);
    }

}