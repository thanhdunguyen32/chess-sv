package game.module.shop.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.module.template.ShopTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ShopTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(ShopTemplateCache.class);

    static class SingletonHolder {
        static ShopTemplateCache instance = new ShopTemplateCache();
    }

    public static ShopTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile List<ShopTemplate> dayShopTemplates;

    private volatile List<ShopTemplate> decompShopTemplates;
    private volatile List<ShopTemplate> generalShopTemplates;
    private volatile List<ShopTemplate> killShopTemplates;
    private volatile List<ShopTemplate> officalShopTemplates;
    private volatile List<ShopTemplate> pvpShopTemplates;
    private volatile List<ShopTemplate> starShopTemplates;
    private volatile List<ShopTemplate> legionShopTemplates;

    public void reload() {
        try {
            String fileName = "shopDay.json";
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<ShopTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopDay size={}", templateWrapperMap.size());
            dayShopTemplates = templateWrapperMap;
            //
            fileName = "shopDecomp.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopDecomp size={}", templateWrapperMap.size());
            decompShopTemplates = templateWrapperMap;
            //
            fileName = "shopGeneral.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopGeneral size={}", templateWrapperMap.size());
            generalShopTemplates = templateWrapperMap;
            //
            fileName = "shopKill.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopKill size={}", templateWrapperMap.size());
            killShopTemplates = templateWrapperMap;
            //
            fileName = "shopOffical.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopOffical size={}", templateWrapperMap.size());
            officalShopTemplates = templateWrapperMap;
            //
            fileName = "shopPvp.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopPvp size={}", templateWrapperMap.size());
            pvpShopTemplates = templateWrapperMap;
            //
            fileName = "shopStar.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopStar size={}", templateWrapperMap.size());
            starShopTemplates = templateWrapperMap;
            //
            fileName = "shopLegion.json";
            jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<ShopTemplate>>() {
            });
            logger.info("shopLegion size={}", templateWrapperMap.size());
            legionShopTemplates = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public List<ShopTemplate> getDayTemplate() {
        return dayShopTemplates;
    }

    public List<ShopTemplate> getDecompTemplates() {
        return decompShopTemplates;
    }

    public List<ShopTemplate> getGeneralShopTemplates() {
        return generalShopTemplates;
    }

    public List<ShopTemplate> getKillShopTemplates() {
        return killShopTemplates;
    }

    public List<ShopTemplate> getOfficalShopTemplates() {
        return officalShopTemplates;
    }

    public List<ShopTemplate> getPvpShopTemplates() {
        return pvpShopTemplates;
    }

    public List<ShopTemplate> getStarShopTemplates() {
        return starShopTemplates;
    }

    public List<ShopTemplate> getLegionShopTemplates() {
        return legionShopTemplates;
    }


    public List<ShopTemplate> getShopTemplates(String shop_type) {
        List<ShopTemplate> retlist = null;
        switch (shop_type) {
            case "general":
                retlist = generalShopTemplates;
                break;
            case "kill":
                retlist = killShopTemplates;
                break;
            case "offical":
                retlist = officalShopTemplates;
                break;
            case "pvp":
                retlist = pvpShopTemplates;
                break;
        }
        return retlist;
    }

    public static void main(String[] args) {
        new ShopTemplateCache().reload();
    }

}