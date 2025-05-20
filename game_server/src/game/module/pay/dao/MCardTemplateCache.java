package game.module.pay.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MCardTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MCardTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MCardTemplateCache.class);

    static class SingletonHolder {

        static MCardTemplateCache instance = new MCardTemplateCache();
    }
    public static MCardTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<String,MCardTemplate> templateList;

    @Override
    public void reload() {
        try {
            String fileName = MCardTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            logger.info("MCardTemplateCache fileName={}", fileName);
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<String,MCardTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<Map<String,MCardTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateList = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MCardTemplate getGzMCardTemplate() {
        return templateList.get("贵族月卡");
    }

    public MCardTemplate getZzMCardTemplate() {
        return templateList.get("至尊月卡");
    }

    public static void main(String[] args) {
        MCardTemplateCache.getInstance().reload();
    }

}