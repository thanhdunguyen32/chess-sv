package game.module.user.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.HeadIconTemplate;
import game.module.template.HeadImageTemplate;
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

public class HeadImageTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(HeadImageTemplateCache.class);

    static class SingletonHolder {
        static HeadImageTemplateCache instance = new HeadImageTemplateCache();
    }

    public static HeadImageTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, HeadImageTemplate> templateMap;

    private volatile Map<String, List<HeadImageTemplate>> nameIdMap;

    @Override
    public void reload() {
        try {
            String fileName = HeadImageTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, HeadImageTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,
                    HeadImageTemplate>>() {
            });
            logger.info("HeadImageTemplate,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            nameIdMap = new HashMap<>();
            for (HeadImageTemplate headImageTemplate : templateWrapperMap.values()) {
                String generalName = headImageTemplate.getNAME();
                if(nameIdMap.containsKey(generalName)){
                    nameIdMap.get(generalName).add(headImageTemplate);
                }else{
                    List<HeadImageTemplate> templates = new ArrayList<>();
                    templates.add(headImageTemplate);
                    nameIdMap.put(generalName,templates);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public HeadImageTemplate getHeadImageTemplate(int id) {
        return templateMap.get(id);
    }

    public int getHeadImageIdByName(String generalName, Integer star){
        List<HeadImageTemplate> templates = nameIdMap.get(generalName);
        for (HeadImageTemplate headImageTemplate : templates){
            if(headImageTemplate.getSTAR() >= star){
                return headImageTemplate.getIMAGEID();
            }
        }
        return 0;
    }

    public boolean containsId(int gsid) {
        return templateMap.containsKey(gsid);
    }

    public static void main(String[] args) {
        HeadImageTemplateCache.getInstance().reload();
    }

}