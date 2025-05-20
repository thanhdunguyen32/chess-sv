package game.module.guide.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.CampaignHelpTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CampaignHelpTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(CampaignHelpTemplateCache.class);

    static class SingletonHolder {
        static CampaignHelpTemplateCache instance = new CampaignHelpTemplateCache();
    }

    public static CampaignHelpTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile CampaignHelpTemplate templateMap;

    public void reload() {
        try {
            String fileName = CampaignHelpTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            CampaignHelpTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<CampaignHelpTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getTHREECHOOSEONE().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public CampaignHelpTemplate.Three1Template getFriendExploreConfig(int chooseIndex) {
        return templateMap.getTHREECHOOSEONE().get(chooseIndex);
    }

    public int getSize() {
        return templateMap.getTHREECHOOSEONE().size();
    }

    public static void main(String[] args) {
        new CampaignHelpTemplateCache().reload();
    }

}