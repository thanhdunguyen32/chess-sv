package game.module.pay.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.FirstChargeTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FirstChargeTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(FirstChargeTemplateCache.class);

    static class SingletonHolder {

        static FirstChargeTemplateCache instance = new FirstChargeTemplateCache();
    }
    public static FirstChargeTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<List<FirstChargeTemplate>> templateList;

    @Override
    public void reload() {
        try {
            String fileName = FirstChargeTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            logger.info("FirstChargeTemplateCache fileName={}", fileName);
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<List<FirstChargeTemplate>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<List<FirstChargeTemplate>>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateList = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public FirstChargeTemplate get6FirstChargeTemplate(int pro) {
        return templateList.get(0).get(pro);
    }

    public FirstChargeTemplate get100FirstChargeTemplate(int pro) {
        return templateList.get(1).get(pro);
    }

    public static void main(String[] args) {
        FirstChargeTemplateCache.getInstance().reload();
    }

}