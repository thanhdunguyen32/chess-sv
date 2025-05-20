package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.GeneralChipTemplate;
import game.module.template.GeneralTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GeneralChipTemplateCache implements Reloadable {

    private static final Logger logger = LoggerFactory.getLogger(GeneralChipTemplateCache.class);

    static class SingletonHolder {
        static GeneralChipTemplateCache instance = new GeneralChipTemplateCache();
    }

    public static GeneralChipTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, GeneralChipTemplate> templateMap;

    private volatile Map<Integer, List<GeneralChipTemplate>> starMap;

    private volatile Map<Integer, Integer> general2ChipMap;

    private volatile List<GeneralChipTemplate> star4DimoList;

    private volatile Map<Integer, List<GeneralChipTemplate>> star4CampMap;

    /**
     * 特定camp5星武将碎片
     */
    private volatile Map<Integer, List<GeneralChipTemplate>> star5CampMap;

    @Override
    public void reload() {
        try {
            String fileName = GeneralChipTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, GeneralChipTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    GeneralChipTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            Map<Integer, Integer> general2ChipMapTmp = new HashMap<>();
            for (Map.Entry<Integer, GeneralChipTemplate> aEntry : templateWrapperMap.entrySet()) {
                aEntry.getValue().setId(aEntry.getKey());
                if (aEntry.getValue().getGCOND() instanceof Integer) {
                    general2ChipMapTmp.put((Integer) (aEntry.getValue().getGCOND()), aEntry.getKey());
                }
            }
            templateMap = templateWrapperMap;
            general2ChipMap = general2ChipMapTmp;
            //star
            Map<Integer, List<GeneralChipTemplate>> starMapTmp = new HashMap<>();
            for (GeneralChipTemplate generalTemplate : templateWrapperMap.values()) {
                if (generalTemplate.getCTYPE() == 2) {//随机碎片
                    continue;
                }
                List<GeneralChipTemplate> generalTemplates = starMapTmp.computeIfAbsent(generalTemplate.getSTAR(), k -> new ArrayList<>());
                generalTemplates.add(generalTemplate);
            }
            starMap = starMapTmp;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int id) {
        return templateMap.containsKey(id);
    }

    public GeneralChipTemplate getGeneralChipTemplate(int id) {
        return templateMap.get(id);
    }

    public List<GeneralChipTemplate> getGeneralTemplates(int aStar) {
        return starMap.get(aStar);
    }

    public void init5StarCamp() {
        //5star camp
        Map<Integer, List<GeneralChipTemplate>> star5CampMapTmp = new HashMap<>();
        for (GeneralChipTemplate generalChipTemplate : templateMap.values()) {
            if (generalChipTemplate.getSTAR() == 5) {
                Object generalTemplateId = generalChipTemplate.getGCOND();
                if (generalTemplateId instanceof Integer) {
                    GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate((Integer) generalTemplateId);
                    List<GeneralChipTemplate> generalTemplates = star5CampMapTmp.computeIfAbsent(generalTemplate.getCAMP(), k -> new ArrayList<>());
                    generalTemplates.add(generalChipTemplate);
                }
            }
        }
        star5CampMap = star5CampMapTmp;
    }

    public void initStar4Camp() {
        //star 4 dimo
        List<GeneralChipTemplate> star4DimoListTmp = new ArrayList<>();
        Map<Integer, List<GeneralChipTemplate>> star4CampMapTmp = new HashMap<>();
        for (GeneralChipTemplate generalChipTemplate : templateMap.values()) {
            Object generalTemplateId = generalChipTemplate.getGCOND();
            if (generalChipTemplate.getSTAR() == 4 && generalTemplateId instanceof Integer) {
                GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate((Integer) generalTemplateId);
                if (generalTemplate.getCAMP() == 5 || generalTemplate.getCAMP() == 6) {
                    star4DimoListTmp.add(generalChipTemplate);
                } else {
                    List<GeneralChipTemplate> generalTemplates = star4CampMapTmp.computeIfAbsent(generalTemplate.getCAMP(), k -> new ArrayList<>());
                    generalTemplates.add(generalChipTemplate);
                }
            }
        }
        star4DimoList = star4DimoListTmp;
        star4CampMap = star4CampMapTmp;
    }

    public List<GeneralChipTemplate> getStar4DimoTemplates() {
        return star4DimoList;
    }

    public List<GeneralChipTemplate> getStar4TemplatesByCamp(int aCamp) {
        return star4CampMap.get(aCamp);
    }

    public List<GeneralChipTemplate> getStar5TemplatesByCamp(int aCamp) {
        return star5CampMap.get(aCamp);
    }

    public Integer getChipTemplateByGeneralId(int generalTemplateId) {
        return general2ChipMap.get(generalTemplateId);
    }

    public Collection<GeneralChipTemplate> getAllChips() {
        return templateMap.values();
    }

    public static void main(String[] args) {
        GeneralChipTemplateCache.getInstance().reload();
        boolean ret1 = GeneralChipTemplateCache.getInstance().containsId(192);
        System.out.println(ret1);
    }

}