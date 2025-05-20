package game.module.pay.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.MzlbPayTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MzlbMylbTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MzlbMylbTemplateCache.class);

    static class SingletonHolder {

        static MzlbMylbTemplateCache instance = new MzlbMylbTemplateCache();
    }

    public static MzlbMylbTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<MzlbPayTemplate> mzlbTemplateList;

    private volatile List<MzlbPayTemplate> mylbTemplateList;

    @Override
    public void reload() {
        try {
            String fileName = "myMzlb.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MzlbPayTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MzlbPayTemplate>>() {
            });
            logger.info("mzlb size={}", templateWrapperMap.size());
            mzlbTemplateList = templateWrapperMap;
            //
            fileName = "myMylb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MzlbPayTemplate> templateWrapperMap2 = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MzlbPayTemplate>>() {
            });
            logger.info("mylb size={}", templateWrapperMap2.size());
            mylbTemplateList = templateWrapperMap2;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<MzlbPayTemplate> getMzlbTemplates() {
        return mzlbTemplateList;
    }

    public List<MzlbPayTemplate> getMylbTemplates() {
        return mylbTemplateList;
    }

    public static void main(String[] args) {
        MzlbMylbTemplateCache.getInstance().reload();
    }

}