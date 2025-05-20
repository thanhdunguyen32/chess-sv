package game.module.dungeon.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.DungeonChooseBuffTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonChooseBuffTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(DungeonChooseBuffTemplateCache.class);

    static class SingletonHolder {
        static DungeonChooseBuffTemplateCache instance = new DungeonChooseBuffTemplateCache();
    }

    public static DungeonChooseBuffTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, DungeonChooseBuffTemplate> templateMap;

    private volatile Map<Integer, List<DungeonChooseBuffTemplate>> templateList;

    public void reload() {
        try {
            String fileName = DungeonChooseBuffTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer, DungeonChooseBuffTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<Integer, DungeonChooseBuffTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            templateList = new HashMap<>();
            for (Map.Entry<Integer, DungeonChooseBuffTemplate> aEntry : templateWrapperMap.entrySet()) {
                DungeonChooseBuffTemplate dungeonChooseBuffTemplate = aEntry.getValue();
                dungeonChooseBuffTemplate.setId(aEntry.getKey());
                Integer chaperid = dungeonChooseBuffTemplate.getCHAPERID();
                if (templateList.containsKey(chaperid)) {
                    templateList.get(chaperid).add(dungeonChooseBuffTemplate);
                } else {
                    List<DungeonChooseBuffTemplate> templates = new ArrayList<>();
                    templateList.put(chaperid, templates);
                    templates.add(dungeonChooseBuffTemplate);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getSize() {
        return templateMap.size();
    }

    public DungeonChooseBuffTemplate getDungeonChooseBuffTemplate(int buffid) {
        return templateMap.get(buffid);
    }

    public List<DungeonChooseBuffTemplate> getTemplateList(int chapterId) {
        return templateList.get(chapterId);
    }

    public List<DungeonChooseBuffTemplate> getTemplateList(int chapterId,int bufftype) {
        List<DungeonChooseBuffTemplate> templates = templateList.get(chapterId);
        List<DungeonChooseBuffTemplate> retlist = new ArrayList<>();
        for (DungeonChooseBuffTemplate dungeonChooseBuffTemplate : templates){
            if(dungeonChooseBuffTemplate.getTYPE() == bufftype){
                retlist.add(dungeonChooseBuffTemplate);
            }
        }
        return retlist;
    }

    public static void main(String[] args) {
        new DungeonChooseBuffTemplateCache().reload();
    }

}