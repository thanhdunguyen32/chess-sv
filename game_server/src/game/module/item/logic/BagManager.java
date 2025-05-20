package game.module.item.logic;

import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.pay.logic.ChargeInfoManager;
import game.module.template.VipTemplate;
import game.module.user.logic.PlayerManager;
import game.module.vip.dao.VipTemplateCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BagManager {

    private static Logger logger = LoggerFactory.getLogger(BagManager.class);

    static class SingletonHolder {

        static BagManager instance = new BagManager();
    }

    public static BagManager getInstance() {
        return BagManager.SingletonHolder.instance;
    }

    /**
     * 获取武将背包携带数量
     *
     * @param playerId
     * @return
     */
    public int getGeneralBagSize(int playerId, int vipLevel) {
        int bagBuyCount = PlayerManager.getInstance().getOtherCount(playerId, ItemConstants.GENERAL_BAG_BUY_COUNT);
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(vipLevel);
        return ItemConstants.INIT_GENERAL_BAG_SIZE + ItemConstants.GENERAL_BAG_PER_SIZE * bagBuyCount + vipTemplate.getRIGHT().getBAG() + ChargeInfoManager.getInstance().getZxnsAddon(playerId);
    }

    public boolean checkBagFull(int playerId, int addNum, int vipLevel) {
        int generalBagSize = getGeneralBagSize(playerId, vipLevel);
        Map<Long, GeneralBean> heros = GeneralCache.getInstance().getHeros(playerId);
        int generalSize = heros.size();
        return generalSize + addNum > generalBagSize;
    }

    public int getGNum(int playerId){
        Map<Long, GeneralBean> heros = GeneralCache.getInstance().getHeros(playerId);
        return heros.size();
    }

}
