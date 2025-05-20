package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.TreasureTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TreasureTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TreasureTemplateCache.class);

    static class SingletonHolder {
        static TreasureTemplateCache instance = new TreasureTemplateCache();
    }

    public static TreasureTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, TreasureTemplate> templateMap;

    private final int[][] LEGEND_TREASURES = {{34251,34261,34271,34281,34291,34301,34311,34321,34331,34341,34351,34401,34411},{34441,34451,34461,34471,
            34481,34491},{34371,34381,34391,34501,34511,34521,34531,34541}};

    public void reload() {
        try {
            String fileName = TreasureTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, TreasureTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, TreasureTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,TreasureTemplate> itemPair : templateWrapperMap.entrySet()){
                itemPair.getValue().setId(itemPair.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int templateId) {
        return templateMap.containsKey(templateId);
    }

    public TreasureTemplate getTreasureTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public int[] getLegendTreasures(int tcond){
        if(tcond<6){
            return null;
        }
        return LEGEND_TREASURES[tcond-6];
    }

    public static void main(String[] args) {
        new TreasureTemplateCache().reload();
    }

}