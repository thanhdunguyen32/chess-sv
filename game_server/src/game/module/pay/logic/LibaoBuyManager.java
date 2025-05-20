package game.module.pay.logic;

import game.GameServer;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.pay.bean.LibaoBuy;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.dao.LibaoBuyDao;
import game.module.pay.dao.LibaoBuyDaoHelper;
import game.module.pay.dao.MzlbMylbTemplateCache;
import game.module.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibaoBuyManager {

    private static Logger logger = LoggerFactory.getLogger(LibaoBuyManager.class);

    static class SingletonHolder {
        static LibaoBuyManager instance = new LibaoBuyManager();
    }

    public static LibaoBuyManager getInstance() {
        return SingletonHolder.instance;
    }

    public int getLibaoBuy(int playerId, String libaoProductId) {
        String normalizedId = libaoProductId.replace("_", "");
        
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        if (libaoBuy == null || libaoBuy.getBuyCount() == null) {
            return 0;
        }
        Integer buyCount = libaoBuy.getBuyCount().get(normalizedId);
        if (buyCount != null) {
            return buyCount;
        }
        return 0;
    }

    public void addLibaoBuy(int playerId, String libaoProductId) {
        addLibaoBuy(playerId, libaoProductId, 1);
    }

    public void addLibaoBuy(int playerId, String libaoProductId, int addNum) {
        String normalizedId = libaoProductId.replace("_", "");
        
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        if (libaoBuy == null) {
            libaoBuy = createLibaoBuy(playerId);
            LibaoBuyCache.getInstance().addLibaoBuy(libaoBuy);
        }
        Map<String, Integer> buyCount = libaoBuy.getBuyCount();
        if (buyCount == null) {
            buyCount = new HashMap<>();
            libaoBuy.setBuyCount(buyCount);
        }
        
        if (buyCount.containsKey(normalizedId)) {
            buyCount.put(normalizedId, buyCount.get(normalizedId) + addNum);
        } else {
            buyCount.put(normalizedId, addNum);
        }

        logger.info("Updated buy count for player {}, package {}: count={}", 
            playerId, normalizedId, buyCount.get(normalizedId));

        if (libaoBuy.getId() == null) {
            LibaoBuyDaoHelper.asyncInsertAffair(libaoBuy);
        } else {
            LibaoBuyDaoHelper.asyncUpdateAffair(libaoBuy);
        }
    }

    public void addLibaoBuyOffline(int playerId, String libaoProductId) {
        String normalizedId = libaoProductId.replace("_", "");
        
        LibaoBuy libaoBuy = LibaoBuyCache.getInstance().getLibaoBuy(playerId);
        if (libaoBuy == null) {
            libaoBuy = LibaoBuyDao.getInstance().getLibaoBuy(playerId);
        }
        if (libaoBuy == null) {
            libaoBuy = createLibaoBuy(playerId);
        }
        Map<String, Integer> buyCount = libaoBuy.getBuyCount();
        if (buyCount == null) {
            buyCount = new HashMap<>();
            libaoBuy.setBuyCount(buyCount);
        }
        
        if (buyCount.containsKey(normalizedId)) {
            buyCount.put(normalizedId, buyCount.get(normalizedId) + 1);
        } else {
            buyCount.put(normalizedId, 1);
        }

        logger.info("Updated offline buy count for player {}, package {}: count={}", 
            playerId, normalizedId, buyCount.get(normalizedId));

        if (libaoBuy.getId() == null) {
            LibaoBuyDao.getInstance().addLibaoBuy(libaoBuy);
        } else {
            LibaoBuyDao.getInstance().updateLibaoBuy(libaoBuy);
        }
    }

    private LibaoBuy createLibaoBuy(int playerId) {
        LibaoBuy libaoBuy = new LibaoBuy();
        libaoBuy.setPlayerId(playerId);
        return libaoBuy;
    }

    public void resetWeekBuyInfo(int seasonId) {
        Map<Integer, LibaoBuy> libaoBuyCache = LibaoBuyCache.getInstance().getLibaoBuyCache();
        List<MzlbPayTemplate> mzlbTemplates = MzlbMylbTemplateCache.getInstance().getMzlbTemplates();
        List<ZhdCjxg2Template> cjxg2Templates = ActivityWeekTemplateCache.getInstance().getCjxg2Templates().get(seasonId-1);
        List<ZhdCzlbTemplate> gxCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGxCzlbTemplates();
        List<ZhdSzhcTemplate> szhcTemplates = ActivityWeekTemplateCache.getInstance().getSzhcTemplate();
        List<ZhdCjxg1Template> cjxg1Templates = ActivityWeekTemplateCache.getInstance().getCjxg1Templates();
        List<ZhdCzlbTemplate> gjCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGjCzlbTemplates();
        List<ZhdCjxg1Template> gjdlTemplates = ActivityWeekTemplateCache.getInstance().getGjdlTemplates();
        List<ZhdCzlbTemplate> czlbTemplates = ActivityWeekTemplateCache.getInstance().getCzlbTemplates();
        List<ZhdXsdhTemplate> xsdhTemplateList = ActivityWeekTemplateCache.getInstance().getXsdhTemplates();
        List<ZhdCzlbTemplate> gzCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGzCzlbTemplates();
        for (LibaoBuy libaoBuy : libaoBuyCache.values()) {
            Map<String, Integer> buyCount = libaoBuy.getBuyCount();
            if (buyCount != null) {
                for (int idx = 0; idx < mzlbTemplates.size(); idx++) {
                    String libaoProductId = "mzlb" + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < cjxg2Templates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_CJXG2 + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < gxCzlbTemplates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_GXLB + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < szhcTemplates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_SZHC + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < cjxg1Templates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_CJXG1 + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < gjCzlbTemplates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_GJLB + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < gjdlTemplates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_GJDL + idx;
                    buyCount.remove(libaoProductId);
                }
                if(czlbTemplates != null) {
                    for (int idx = 0; idx < czlbTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_CZLB + idx;
                        buyCount.remove(libaoProductId);
                    }
                }
                for (int idx = 0; idx < xsdhTemplateList.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_XSDH + idx;
                    buyCount.remove(libaoProductId);
                }
                for (int idx = 0; idx < gzCzlbTemplates.size(); idx++) {
                    String libaoProductId = PayConstants.PRODUCT_ID_GZLB + idx;
                    buyCount.remove(libaoProductId);
                }
            }
        }
        //remove db
        GameServer.executorService.execute(() -> {
            List<LibaoBuy> libaoBuyAll = LibaoBuyDao.getInstance().getLibaoBuyAll();
            for (LibaoBuy libaoBuy : libaoBuyAll) {
                Map<String, Integer> buyCount = libaoBuy.getBuyCount();
                if (buyCount != null) {
                    for (int idx = 0; idx < mzlbTemplates.size(); idx++) {
                        String libaoProductId = "mzlb" + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < cjxg2Templates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_CJXG2 + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < gxCzlbTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_CJXG2 + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < szhcTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_SZHC + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < cjxg1Templates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_CJXG1 + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < gjCzlbTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_GJLB + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < gjdlTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_GJDL + idx;
                        buyCount.remove(libaoProductId);
                    }
                    if(czlbTemplates != null) {
                        for (int idx = 0; idx < czlbTemplates.size(); idx++) {
                            String libaoProductId = PayConstants.PRODUCT_ID_CZLB + idx;
                            buyCount.remove(libaoProductId);
                        }
                    }
                    for (int idx = 0; idx < xsdhTemplateList.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_XSDH + idx;
                        buyCount.remove(libaoProductId);
                    }
                    for (int idx = 0; idx < gzCzlbTemplates.size(); idx++) {
                        String libaoProductId = PayConstants.PRODUCT_ID_GZLB + idx;
                        buyCount.remove(libaoProductId);
                    }
                }
                LibaoBuyDao.getInstance().updateLibaoBuy(libaoBuy);
            }
        });
    }

    public void resetMonthBuyInfo() {
        Map<Integer, LibaoBuy> libaoBuyCache = LibaoBuyCache.getInstance().getLibaoBuyCache();
        List<MzlbPayTemplate> mylbTemplates = MzlbMylbTemplateCache.getInstance().getMylbTemplates();
        for (LibaoBuy libaoBuy : libaoBuyCache.values()) {
            Map<String, Integer> buyCount = libaoBuy.getBuyCount();
            if (buyCount != null) {
                for (int idx = 0; idx < mylbTemplates.size(); idx++) {
                    String libaoProductId = "mylb" + idx;
                    buyCount.remove(libaoProductId);
                }
            }
        }
        //remove db
        GameServer.executorService.execute(() -> {
            List<LibaoBuy> libaoBuyAll = LibaoBuyDao.getInstance().getLibaoBuyAll();
            for (LibaoBuy libaoBuy : libaoBuyAll) {
                Map<String, Integer> buyCount = libaoBuy.getBuyCount();
                if (buyCount != null) {
                    for (int idx = 0; idx < mylbTemplates.size(); idx++) {
                        String libaoProductId = "mylb" + idx;
                        buyCount.remove(libaoProductId);
                    }
                }
                LibaoBuyDao.getInstance().updateLibaoBuy(libaoBuy);
            }
        });
    }

}
