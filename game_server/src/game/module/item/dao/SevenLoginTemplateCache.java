package game.module.item.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.SevenLoginTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SevenLoginTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(SevenLoginTemplateCache.class);

    static class SingletonHolder {
        static SevenLoginTemplateCache instance = new SevenLoginTemplateCache();
    }

    public static SevenLoginTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private Integer loginSignGsid;

    private Map<String, SevenLoginTemplate.SevenLoginRewardTemplate> sevenLoginRewardTemplateMap;

    public void reload() {
        try {
            String fileName = SevenLoginTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            SevenLoginTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<SevenLoginTemplate>() {
            });
            logger.info("size={}", templateWrapperMap.getREWARD().size());
            loginSignGsid = templateWrapperMap.getLOGINSIGN();
            sevenLoginRewardTemplateMap = new HashMap<>();
            for (SevenLoginTemplate.SevenLoginRewardTemplate sevenLoginRewardTemplate : templateWrapperMap.getREWARD()) {
                sevenLoginRewardTemplateMap.put(sevenLoginRewardTemplate.getEXID(), sevenLoginRewardTemplate);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public int getLoginSign() {
        return loginSignGsid;
    }

    public SevenLoginTemplate.SevenLoginRewardTemplate getSevenLoginRewardTemplates(String exid) {
        return sevenLoginRewardTemplateMap.get(exid);
    }

    public static void main(String[] args) {
        new SevenLoginTemplateCache().reload();
    }

}