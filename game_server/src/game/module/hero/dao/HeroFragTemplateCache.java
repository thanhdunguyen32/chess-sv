package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.HeroFragTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HeroFragTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(HeroFragTemplateCache.class);

    static class SingletonHolder {
        static HeroFragTemplateCache instance = new HeroFragTemplateCache();
    }

    public static HeroFragTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, HeroFragTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = HeroFragTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<Integer,HeroFragTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<Integer,HeroFragTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            for(Map.Entry<Integer,HeroFragTemplate> templateEntry : templateWrapperMap.entrySet()){
                templateEntry.getValue().setId(templateEntry.getKey());
            }
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean containsId(int id) {
        return templateMap.containsKey(id);
    }

    public HeroFragTemplate getHeroFragTemplate(int id) {
        return templateMap.get(id);
    }

    public static void main(String[] args) {
        HeroFragTemplateCache.getInstance().reload();
        HeroFragTemplate ret1 = HeroFragTemplateCache.getInstance().getHeroFragTemplate(36083);
        System.out.println(ret1);
    }

}