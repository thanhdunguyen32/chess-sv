package game.module.online.logic;

import game.entity.PlayingRole;
import game.module.online.dao.OnlineGiftTemplateCache;
import game.module.template.OnlineGiftTemplate;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author HeXuhui
 */
public class OnlineGiftManager {

    private static Logger logger = LoggerFactory.getLogger(OnlineGiftManager.class);

    static class SingletonHolder {

        static OnlineGiftManager instance = new OnlineGiftManager();

    }
    public static OnlineGiftManager getInstance() {
        return SingletonHolder.instance;
    }

    public long getNextOnlineTime(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        long rettime = 0;
        int giveSign = OnlineGiftTemplateCache.getInstance().getGiveSign();
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        int signCount = 0;
        if(playerOthers.containsKey(giveSign)){
            signCount = playerOthers.get(giveSign).getCount();
        }
        if(signCount < OnlineGiftTemplateCache.getInstance().getRewardSize()){
            OnlineGiftTemplate.OnlineGiftTemplateReward onlineReward = OnlineGiftTemplateCache.getInstance().getOnlineReward(signCount);
            if (onlineReward != null) {
                rettime = onlineReward.getTIME() * 60000 + playingRole.getPlayerCacheStatus().getEnterGameTime().getTime();
            }
        }
        return rettime;
    }

}
