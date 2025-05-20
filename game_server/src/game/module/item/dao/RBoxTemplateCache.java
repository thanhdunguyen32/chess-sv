package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.GeneralChipTemplate;
import game.module.template.RBoxTemplate;
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

public class RBoxTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(RBoxTemplateCache.class);

    static class SingletonHolder {
        static RBoxTemplateCache instance = new RBoxTemplateCache();
    }

    public static RBoxTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, RBoxTemplate> templateMap;
    private volatile Map<Integer, List<Integer>> normalGeneralMap;
    private volatile List<Integer> normalGeneralList;
    private volatile Map<Integer, List<Integer>> eliteGeneralMap;
    private volatile List<Integer> eliteGeneralList;
    private volatile Map<Integer, List<Integer>> legendGeneralMap;
    private volatile List<Integer> legendGeneralList;

    public void reload() {
        try {
            String fileName = RBoxTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, RBoxTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, RBoxTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for (Map.Entry<Integer, RBoxTemplate> itemPair : templateWrapperMap.entrySet()) {
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

    public void initElite5Star() {
        eliteGeneralMap = new HashMap<>();
        eliteGeneralList = new ArrayList<>();
        init5Star(32101, eliteGeneralMap, eliteGeneralList);
    }

    public void init5Star(int templateId, Map<Integer, List<Integer>> star5GeneralMap, List<Integer> generalList) {
        RBoxTemplate rBoxTemplate = templateMap.get(templateId);
        List<RewardTemplateChance> items = rBoxTemplate.getITEMS();
        for (RewardTemplateChance item : items) {
            Integer generalTemplateId = item.getGSID();
            Integer camp = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId).getCAMP();
            List<Integer> generals = star5GeneralMap.computeIfAbsent(camp, k -> new ArrayList<>());
            Integer chipTemplateId = GeneralChipTemplateCache.getInstance().getChipTemplateByGeneralId(generalTemplateId);
            generals.add(chipTemplateId);
            generalList.add(chipTemplateId);
        }
    }

    public void initLegendStar() {
        legendGeneralMap = new HashMap<>();
        legendGeneralList = new ArrayList<>();
        init5Star(32110, legendGeneralMap, legendGeneralList);
        //神话武将
        init5Star(32105, legendGeneralMap, legendGeneralList);
    }

    public void initNormal5Star() {
        List<GeneralChipTemplate> generalTemplates = GeneralChipTemplateCache.getInstance().getGeneralTemplates(5);
        Map<Integer, List<Integer>> normalGeneralMapTmp = new HashMap<>();
        List<Integer> normalGeneralListTmp = new ArrayList<>();
        for (GeneralChipTemplate generalChipTemplate : generalTemplates) {
            if (!(generalChipTemplate.getGCOND() instanceof Integer)) {
                continue;
            }
            Integer generalChipTemplateId = generalChipTemplate.getId();
            boolean isExist = false;
            for (List<Integer> eliteGeneralChip1Camp : eliteGeneralMap.values()) {
                if (eliteGeneralChip1Camp.contains(generalChipTemplateId)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                for (List<Integer> eliteGeneralChip1Camp : legendGeneralMap.values()) {
                    if (eliteGeneralChip1Camp.contains(generalChipTemplateId)) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (!isExist) {
                int generalTemplateId = (Integer) generalChipTemplate.getGCOND();
                Integer camp = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId).getCAMP();
                List<Integer> generals = normalGeneralMapTmp.computeIfAbsent(camp, k -> new ArrayList<>());
                generals.add(generalChipTemplateId);
                normalGeneralListTmp.add(generalChipTemplateId);
            }
        }
        normalGeneralMap = normalGeneralMapTmp;
        normalGeneralList = normalGeneralListTmp;
    }

    public List<Integer> getCamp5StarNormalChips(int aCamp){
        return normalGeneralMap.get(aCamp);
    }

    public List<Integer> getCamp5StarEliteChips(int aCamp){
        return eliteGeneralMap.get(aCamp);
    }

    public List<Integer> getCamp5StarLegendChips(int aCamp){
        return legendGeneralMap.get(aCamp);
    }

    public RBoxTemplate getRBoxTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public List<Integer> get5StarNormalChips(){
        return normalGeneralList;
    }

    public List<Integer> get5StarEliteChips(){
        return eliteGeneralList;
    }

    public List<Integer> get5StarLegendChips(){
        return legendGeneralList;
    }

    public static void main(String[] args) {
        new RBoxTemplateCache().reload();
    }

}