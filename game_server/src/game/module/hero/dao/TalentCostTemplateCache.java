package game.module.hero.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.RewardTemplateSimple;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TalentCostTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(TalentCostTemplateCache.class);

    static class SingletonHolder {
        static TalentCostTemplateCache instance = new TalentCostTemplateCache();
    }

    public static TalentCostTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<RewardTemplateSimple> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = "dbTalentCost.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<RewardTemplateSimple> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<RewardTemplateSimple>>() {
            });
            logger.info("TalentCostTemplate,size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
            //
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public RewardTemplateSimple getTalentCost(int doCount) {
        if (doCount >= templateMap.size()) {
            return templateMap.get(templateMap.size() - 1);
        }
        return templateMap.get(doCount);
    }

}