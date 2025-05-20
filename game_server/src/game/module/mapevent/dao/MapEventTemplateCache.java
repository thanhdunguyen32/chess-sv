package game.module.mapevent.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MapEventTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapEventTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MapEventTemplateCache.class);

    public MapEventTemplate.MapEvent1Template getMapEvent1(int eid) {
        return templateMap.getEVENT().getEVENT1().get(eid);
    }

    public MapEventTemplate.MapEvent2Template getMapEvent2(int eid) {
        return templateMap.getEVENT().getEVENT2().get(eid);
    }

    public MapEventTemplate.MapEvent6Template getMapEvent6(int eid) {
        return templateMap.getEVENT().getEVENT6().get(eid);
    }

    static class SingletonHolder {
        static MapEventTemplateCache instance = new MapEventTemplateCache();
    }

    public static MapEventTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile MapEventTemplate templateMap;

    public void reload() {
        try {
            String fileName = MapEventTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            MapEventTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<MapEventTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getEVENT().getEVENT1().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<Integer> getEventIds(int eventType) {
        switch (eventType) {
            case 1:
                return new ArrayList<>(templateMap.getEVENT().getEVENT1().keySet());
            case 2:
                return new ArrayList<>(templateMap.getEVENT().getEVENT2().keySet());
            case 6:
                return new ArrayList<>(templateMap.getEVENT().getEVENT6().keySet());
        }
        return null;
    }

    public static void main(String[] args) {
        new MapEventTemplateCache().reload();
    }

}