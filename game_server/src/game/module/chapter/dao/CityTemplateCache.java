package game.module.chapter.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.CityTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CityTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(CityTemplateCache.class);

    static class SingletonHolder {
        static CityTemplateCache instance = new CityTemplateCache();
    }

    public static CityTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, CityTemplate> templateMap;

    private Integer initCityId;

    private volatile Map<Integer, Integer> cityIdChain;

    private List<Integer> cityIds;

    private Map<Integer,Boolean> eidCitySide;

    public void reload() {
        try {
            String fileName = CityTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, CityTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer, CityTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            List<Integer> cityIdList = new ArrayList<>();
            eidCitySide = new HashMap<>();
            for (Map.Entry<Integer, CityTemplate> itemPair : templateWrapperMap.entrySet()) {
                CityTemplate cityTemplate = itemPair.getValue();
                cityTemplate.setId(itemPair.getKey());
                cityIdList.add(itemPair.getKey());
                List<Integer> eids = cityTemplate.getEID();
                eidCitySide.put(eids.get(0), false);
                if(eids.size()==2){
                    eidCitySide.put(eids.get(1), true);
                }
            }
            templateMap = templateWrapperMap;
            //
            cityIdList.sort(Comparator.comparingInt(o -> o));
            cityIds = cityIdList;
            initCityId = cityIdList.get(0);
            Map<Integer, Integer> tmpCityIdChain = new HashMap<>();
            for (int i = 0; i < cityIdList.size() - 1; i++) {
                tmpCityIdChain.put(cityIdList.get(i), cityIdList.get(i + 1));
            }
            cityIdChain = tmpCityIdChain;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int templateId) {
        return templateMap.containsKey(templateId);
    }

    public CityTemplate getCityTemplateById(int templateId) {
        return templateMap.get(templateId);
    }

    public int getInitCityId() {
        return initCityId;
    }

    public Integer getNextCityId(int cityId) {
        return cityIdChain.get(cityId);
    }

    public List<Integer> getCityIds(){
        return cityIds;
    }

    public Boolean isEidCitySide(int eid){
        return eidCitySide.get(eid);
    }

    public static void main(String[] args) {
        new CityTemplateCache().reload();
    }

}