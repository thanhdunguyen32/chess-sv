package game.module.activity.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyXiangouTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiangouTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(XiangouTemplateCache.class);

    static class SingletonHolder {
        static XiangouTemplateCache instance = new XiangouTemplateCache();
    }

    public static XiangouTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, MyXiangouTemplate> levelXiangouMap;
    private volatile Map<Integer, MyXiangouTemplate> gstarXiangouMap;

    public void reload() {
        try {
            String fileName = MyXiangouTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MyXiangouTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MyXiangouTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            Map<Integer, MyXiangouTemplate> levelXiangouMapTmp = new HashMap<>();
            Map<Integer, MyXiangouTemplate> gstarXiangouMapTmp = new HashMap<>();
            for (MyXiangouTemplate myXiangouTemplate : templateWrapperMap) {
                if (myXiangouTemplate.getLevel() != null) {
                    levelXiangouMapTmp.put(myXiangouTemplate.getLevel(), myXiangouTemplate);
                } else if (myXiangouTemplate.getGstar() != null) {
                    gstarXiangouMapTmp.put(myXiangouTemplate.getGstar(), myXiangouTemplate);
                }
            }
            levelXiangouMap = levelXiangouMapTmp;
            gstarXiangouMap = gstarXiangouMapTmp;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MyXiangouTemplate getLevelXiangouTemplate(int alevel) {
        return levelXiangouMap.get(alevel);
    }

    public Collection<MyXiangouTemplate> getLevelXiangouTemplateAll(){
        return levelXiangouMap.values();
    }

    public MyXiangouTemplate getGstarXiangouTemplate(int gstar) {
        return gstarXiangouMap.get(gstar);
    }

    public static void main(String[] args) {
        new XiangouTemplateCache().reload();
    }

}