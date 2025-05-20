package game.module.user.logic;

import game.common.CommonUtils;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.template.GoldTemplate;
import game.module.user.bean.GoldBuy;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.GoldBuyCache;
import game.module.user.dao.GoldTemplateCache;
import game.module.user.dao.PlayerOtherCache;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GoldBuyManager {

    private static Logger logger = LoggerFactory.getLogger(GoldBuyManager.class);

    static class SingletonHolder {

        static GoldBuyManager instance = new GoldBuyManager();
    }

    public static GoldBuyManager getInstance() {
        return SingletonHolder.instance;
    }

    public int getTimeZone(long buySeconds) {
        //天数
        int days = (int) (buySeconds / (24 * 3600));
        GoldTemplate goldTemplate = GoldTemplateCache.getInstance().getGoldTemplate();
        List<Integer> refreshHours = goldTemplate.getREFRESH();
        int i = 0;
        for (int aHour : refreshHours) {
            if (buySeconds >= aHour * 3600) {
                i++;
            } else {
                break;
            }
        }
        return i + days * 3;
    }

    public boolean isSameTimeZone(long buySecond1, long buySecond2) {
        int zone1 = getTimeZone(buySecond1);
        int zone2 = getTimeZone(buySecond2);
        return zone1 == zone2;
    }

    public GoldBuy createGoldBuy(int playerId) {
        GoldBuy goldBuy = new GoldBuy();
        goldBuy.setPlayerId(playerId);
        GoldTemplate goldTemplate = GoldTemplateCache.getInstance().getGoldTemplate();
        List<Long> alist = CommonUtils.fillList(goldTemplate.getREFRESH().size(), 0L);
        goldBuy.setBuyTime(alist);
        return goldBuy;
    }

    public void updateGoldBuyProperty(PlayingRole playingRole, boolean isPush) {
        int playerId = playingRole.getId();
        GoldBuy goldBuy = GoldBuyCache.getInstance().getGoldBuy(playerId);
        Date now = new Date();
        long currentPassSeconds = DateUtils.getFragmentInSeconds(now, Calendar.YEAR);
        List<GoldTemplate.GoldTemplateInfo> goldTemplateInfos = GoldTemplateCache.getInstance().getInfo();
        for (int i = 0; i < goldTemplateInfos.size(); i++) {
            Integer mark = goldTemplateInfos.get(i).getMARK();
            int newVal = 0;
            if(goldBuy != null) {
                Long aBuyTime = goldBuy.getBuyTime().get(i);
                long oldBuySeconds = DateUtils.getFragmentInSeconds(new Date(aBuyTime), Calendar.YEAR);
                boolean sameTimeZone = isSameTimeZone(currentPassSeconds, oldBuySeconds);
                if (sameTimeZone) {
                    newVal = 1;
                }
            }
            Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
            if (!playerOthers.containsKey(mark) && newVal == 1 || playerOthers.containsKey(mark) && playerOthers.get(mark).getCount() != newVal) {
                AwardUtils.setRes(playingRole, mark, newVal, isPush);
            }
        }
    }

}
