package game.module.activity.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.season.dao.SeasonCache;
import game.module.template.*;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ActivityWeekTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ActivityWeekTemplateCache.class);

    static class SingletonHolder {
        static ActivityWeekTemplateCache instance = new ActivityWeekTemplateCache();
    }

    public static ActivityWeekTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<List<ZhdCjxg2Template>> cjxg2Templates;
    private volatile Map<String,List<ZhdCzlbTemplate>> czlbTemplates;
    private volatile List<ZhdCzlbTemplate> gjCzlbTemplates;
    private volatile List<ZhdCzlbTemplate> gxCzlbTemplates;
    private volatile List<ZhdCzlbTemplate> gzCzlbTemplates;
    private volatile ZhdDjjfTemplate djjfTemplate;
    private volatile ZhdDjjfTemplate gxdbTemplate;
    private volatile List<ZhdCjxg1Template> cjxg1Templates;
    private volatile List<ZhdSzhcTemplate> szhcTemplates;
    private volatile ZhdTnqwTemplate tnqwTemplate;
    private volatile List<ZhdTnqwBossTemplate> tnqwBossTemplates;
    private volatile ZhdDjjfTemplate zmjfTemplate;
    private volatile List<ZhdXsdhTemplate> xsdhTemplates;
    private volatile List<ZhdCjxg1Template> gjdlTemplates;
    private volatile List<ZhdDjrwTemplate> djrwTemplates;
    private volatile ZhdJfbxTemplate jfbxTemplate;
    private volatile ZhdMjbgTemplate mjbgTemplate;
    private volatile ZhdTgslTemplate tgslTemplate;
    private volatile ZhdCxryTemplate cxryTemplate;

    public void reload() {
        try {
            String fileName = "actCjxg2.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<List<ZhdCjxg2Template>> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<List<ZhdCjxg2Template>>>() {
                    });
            logger.info("actCjxg2,size={}", templateWrapperMap.size());
            cjxg2Templates = templateWrapperMap;
            //
            fileName = "actCzlb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            Map<String,List<ZhdCzlbTemplate>> templateWrapperMapCzlb = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<Map<String,List<ZhdCzlbTemplate>>>() {
                    });
            logger.info("actCzlb,size={}", templateWrapperMapCzlb.size());
            czlbTemplates = templateWrapperMapCzlb;
            //
            fileName = "actGjCzlb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdCzlbTemplate> templateWrapperMapGjCzlb = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdCzlbTemplate>>() {
                    });
            logger.info("actGjCzlb,size={}", templateWrapperMapCzlb.size());
            gjCzlbTemplates = templateWrapperMapGjCzlb;
            //
            fileName = "actGxCzlb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdCzlbTemplate> templateWrapperMapGxCzlb = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdCzlbTemplate>>() {
                    });
            logger.info("actGxCzlb,size={}", templateWrapperMapCzlb.size());
            gxCzlbTemplates = templateWrapperMapGxCzlb;
            //
            fileName = "actGzCzlb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdCzlbTemplate> templateWrapperMapGzCzlb = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdCzlbTemplate>>() {
                    });
            logger.info("actGzCzlb,size={}", templateWrapperMapCzlb.size());
            gzCzlbTemplates = templateWrapperMapGzCzlb;
            //
            fileName = "actDjjf.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdDjjfTemplate templateWrapperMapDjjf = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdDjjfTemplate>() {
                    });
            logger.info("actDjjf,size={}", templateWrapperMapDjjf.getMissions().size());
            djjfTemplate = templateWrapperMapDjjf;
            //
            fileName = "actGxdb.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMapDjjf = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdDjjfTemplate>() {
                    });
            logger.info("actGxdb,size={}", templateWrapperMapDjjf.getMissions().size());
            gxdbTemplate = templateWrapperMapDjjf;
            //
            fileName = "actCjxg1.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdCjxg1Template> templateWrapperMapCjxg1 = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdCjxg1Template>>() {
                    });
            logger.info("actCjxg1,size={}", templateWrapperMapCjxg1.size());
            cjxg1Templates = templateWrapperMapCjxg1;
            //
            fileName = "actSzhc.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdSzhcTemplate> templateWrapperMapSzhc = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdSzhcTemplate>>() {
                    });
            logger.info("actSzhc,size={}", templateWrapperMapSzhc.size());
            szhcTemplates = templateWrapperMapSzhc;
            //
            fileName = "actTnqw.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdTnqwTemplate templateWrapperMapTnqw = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdTnqwTemplate>() {
                    });
            logger.info("actTnqw,size={}", templateWrapperMapTnqw.getBosslist().size());
            tnqwTemplate = templateWrapperMapTnqw;
            //
            fileName = "actTnqwBoss.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdTnqwBossTemplate> templateWrapperMapTnqwBoss = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdTnqwBossTemplate>>() {
                    });
            logger.info("actTnqwBoss,size={}", templateWrapperMapTnqwBoss.size());
            tnqwBossTemplates = templateWrapperMapTnqwBoss;
            //
            fileName = "actZmjf.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMapDjjf = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdDjjfTemplate>() {
                    });
            logger.info("actZmjf,size={}", templateWrapperMapDjjf.getMissions().size());
            zmjfTemplate = templateWrapperMapDjjf;
            //
            fileName = "actXsdh.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdXsdhTemplate> templateWrapperMapXsdh = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdXsdhTemplate>>() {
                    });
            logger.info("actXsdh,size={}", templateWrapperMapXsdh.size());
            xsdhTemplates = templateWrapperMapXsdh;
            //
            fileName = "actGjdl.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdCjxg1Template> templateWrapperMapGjdl = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdCjxg1Template>>() {
                    });
            logger.info("actGjdl,size={}", templateWrapperMapGjdl.size());
            gjdlTemplates = templateWrapperMapGjdl;
            //
            fileName = "actDjrw.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ZhdDjrwTemplate> templateWrapperMapDjrw = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<ZhdDjrwTemplate>>() {
                    });
            logger.info("actDjrw,size={}", templateWrapperMapDjrw.size());
            djrwTemplates = templateWrapperMapDjrw;
            //
            fileName = "actJfbx.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdJfbxTemplate templateWrapperMapJfbx = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdJfbxTemplate>() {
                    });
            logger.info("actJfbx,size={}", templateWrapperMapJfbx.getBox().size());
            jfbxTemplate = templateWrapperMapJfbx;
            //
            fileName = "actMjbg.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdMjbgTemplate templateWrapperMapMjbg = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdMjbgTemplate>() {
                    });
            logger.info("actMjbg,size={}", templateWrapperMapMjbg.getItems().size());
            mjbgTemplate = templateWrapperMapMjbg;
            //
            fileName = "actTgsl.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdTgslTemplate templateWrapperMapTgsl = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdTgslTemplate>() {
                    });
            logger.info("actTgsl,size={}", templateWrapperMapTgsl.getReward().size());
            tgslTemplate = templateWrapperMapTgsl;
            //
            fileName = "actCxry.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            ZhdCxryTemplate templateWrapperMapCxry = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<ZhdCxryTemplate>() {
                    });
            logger.info("actCxry,size={}", templateWrapperMapMjbg.getItems().size());
            cxryTemplate = templateWrapperMapCxry;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<List<ZhdCjxg2Template>> getCjxg2Templates() {
        return cjxg2Templates;
    }

    public List<ZhdCzlbTemplate> getCzlbTemplates() {
        String zhdName = SeasonCache.getInstance().getBattleSeason().getZhdName();
        return czlbTemplates.get(zhdName);
    }

    public List<ZhdCzlbTemplate> getGjCzlbTemplates() {
        return gjCzlbTemplates;
    }

    public List<ZhdCzlbTemplate> getGxCzlbTemplates() {
        return gxCzlbTemplates;
    }

    public List<ZhdCzlbTemplate> getGzCzlbTemplates() {
        return gzCzlbTemplates;
    }

    public ZhdDjjfTemplate getDjjfTemplate() {
        return djjfTemplate;
    }

    public ZhdDjjfTemplate getGxdbTemplate() {
        return gxdbTemplate;
    }

    public List<ZhdCjxg1Template> getCjxg1Templates(){
        return cjxg1Templates;
    }

    public List<ZhdSzhcTemplate> getSzhcTemplate(){
        return szhcTemplates;
    }

    public ZhdTnqwTemplate getTnqwTemplate(){
        return tnqwTemplate;
    }

    public List<ZhdTnqwBossTemplate> getTnqwBossTemplates(){
        return tnqwBossTemplates;
    }

    public ZhdDjjfTemplate getZmjfTemplate(){
        return zmjfTemplate;
    }

    public List<ZhdXsdhTemplate> getXsdhTemplates(){
        return xsdhTemplates;
    }

    public List<ZhdCjxg1Template> getGjdlTemplates(){
        return gjdlTemplates;
    }

    public List<ZhdDjrwTemplate> getDjrwTemplates(){
        return djrwTemplates;
    }

    public ZhdJfbxTemplate getJfbxTemplate(){
        return jfbxTemplate;
    }

    public ZhdMjbgTemplate getMjbgTemplate(){
        return mjbgTemplate;
    }

    public ZhdTgslTemplate getTgslTemplate(){
        return tgslTemplate;
    }

    public ZhdCxryTemplate getCxryTemplate(){
        return cxryTemplate;
    }

    public static void main(String[] args) {
        new ActivityWeekTemplateCache().reload();
    }

}