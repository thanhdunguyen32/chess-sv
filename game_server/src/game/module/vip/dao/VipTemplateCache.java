package game.module.vip.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.VipTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VipTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(VipTemplateCache.class);

    static class SingletonHolder {

        static VipTemplateCache instance = new VipTemplateCache();
    }
    public static VipTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<VipTemplate> templateList;

    @Override
    public void reload() {
        try {
            String fileName = VipTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<VipTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<VipTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateList = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<VipTemplate> getTemplateList() {
        return templateList;
    }

    public VipTemplate getVipTemplate(Integer vipLevel) {
        return templateList.get(vipLevel);
    }

    public static void main(String[] args) {
        VipTemplateCache.getInstance().reload();
//		List<String> retlist = LevelsTemplateCache.getInstance()
//				.getDataListFromLine("30,600,\"[[2,1,5000],[2,2,5000],[2,3,5000],[4,5031,2],[4,20001,2]]\"");
//		logger.info("{}",retlist);
    }

}