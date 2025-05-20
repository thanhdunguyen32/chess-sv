package game.module.user.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.HeadIconTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HeadIconTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(HeadIconTemplateCache.class);

    static class SingletonHolder {
        static HeadIconTemplateCache instance = new HeadIconTemplateCache();
    }

    public static HeadIconTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, HeadIconTemplate> templateMap;

    private volatile Map<String, Integer> nameIdMap;

    @Override
    public void reload() {
        try {
            String fileName = HeadIconTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, HeadIconTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    HeadIconTemplate>>() {
            });
            logger.info("HeadIconTemplate,size={}", templateWrapperMap.size());
            Map<String, Integer> nameIdMapTmp = new HashMap<>();
            for (HeadIconTemplate headIconTemplate : templateWrapperMap.values()){
                nameIdMapTmp.put(headIconTemplate.getNAME(),headIconTemplate.getHEADID());
            }
            templateMap = templateWrapperMap;
            nameIdMap = nameIdMapTmp;
            //
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public HeadIconTemplate getHeadIconTemplate(int id) {
        return templateMap.get(id);
    }

    public int getHeadIconIdByName(String generalName){
        return nameIdMap.get(generalName);
    }

    public boolean containsId(int gsid) {
        return templateMap.containsKey(gsid);
    }

    public static void main(String[] args) {
        HeadIconTemplateCache.getInstance().reload();
    }

}