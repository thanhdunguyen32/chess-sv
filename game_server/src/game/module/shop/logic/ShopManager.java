package game.module.shop.logic;

import game.module.shop.bean.ShopBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HeXuhui
 */
public class ShopManager {

    private static Logger logger = LoggerFactory.getLogger(ShopManager.class);

    static class SingletonHolder {

        static ShopManager instance = new ShopManager();

    }

    public static ShopManager getInstance() {
        return SingletonHolder.instance;
    }

    public ShopBean createShopBean(int playerId, int buyIndex, String shopType) {
        ShopBean shopBean = new ShopBean();
        shopBean.setPlayerId(playerId);
        if (shopType.equals("day")) {
            Map<Integer, Integer> dayBuyMap = new HashMap<>();
            dayBuyMap.put(buyIndex, 1);
            shopBean.setDayBuyCount(dayBuyMap);
            shopBean.setLastDayBuyTime(new Date());
        } else if (shopType.equals("decomp")) {
            Map<Integer, Integer> dayBuyMap = new HashMap<>();
            dayBuyMap.put(buyIndex, 1);
            shopBean.setDecompBuyCount(dayBuyMap);
        }else if(shopType.equals("star")){
            Map<Integer, Integer> dayBuyMap = new HashMap<>();
            dayBuyMap.put(buyIndex, 1);
            shopBean.setStarBuyCount(dayBuyMap);
        }else if(shopType.equals("legion")){
            Map<Integer, Integer> legionBuyMap = new HashMap<>();
            legionBuyMap.put(buyIndex, 1);
            shopBean.setLegionBuyCount(legionBuyMap);
        }
        return shopBean;
    }

}
