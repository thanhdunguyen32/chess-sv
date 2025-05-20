package game.module.worldboss.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.LegionPracticeTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.WorldBossTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LegionPracticeTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(LegionPracticeTemplateCache.class);

    static class SingletonHolder {
        static LegionPracticeTemplateCache instance = new LegionPracticeTemplateCache();
    }

    public static LegionPracticeTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile LegionPracticeTemplate templateMap;

    public void reload() {
        try {
            String fileName = LegionPracticeTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            LegionPracticeTemplate templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<LegionPracticeTemplate>() {
                    });
            logger.info("size={}", templateWrapperMap.getBOSS_INFO().size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public LegionPracticeTemplate.LegionPracticeBossTemplate getWorldBossTemplate(int bossid) {
        return templateMap.getBOSS_INFO().get(bossid);
    }

    public List<Integer> getFightCost(){
        return templateMap.getFIGHTING_COST();
    }

    public List<LegionPracticeTemplate.LegionWorldRewardTemplate> getLegionWorldReward(){
        return templateMap.getLEGION_WORLD_REWARD();
    }

    public List<RewardTemplateSimple> getLegionHarmFirst(){
        return templateMap.getLEGION_HARM_FIRST();
    }

    public static void main(String[] args) {
        new LegionPracticeTemplateCache().reload();
    }

}