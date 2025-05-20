package game.module.draw.logic;

import game.entity.PlayingRole;
import game.module.draw.bean.PubDraw;
import game.module.draw.dao.PubDrawCache;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Date;

public class PubDrawManager {

    private static final Logger logger = LoggerFactory.getLogger(PubDrawManager.class);

    static class SingletonHolder {
        static PubDrawManager instance = new PubDrawManager();
    }

    public static PubDrawManager getInstance() {
        return SingletonHolder.instance;
    }

    public PubDraw createPubDraw(PlayingRole playingRole, boolean isFree, int buy_type) {
        int playerId = playingRole.getId();
        PubDraw pubDraw = new PubDraw();
        pubDraw.setPlayerId(playerId);
        if (isFree) {
            Date now = new Date();
            if (buy_type == 1) {
                pubDraw.setLastNormalTime(now);
                //push
                WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100001,
                        DateUtils.addHours(now, PubDrawConstants.DRAW_NORMAL_HOURS).getTime());
                playingRole.write(pushmsg.build(playingRole.alloc()));
            } else {
                pubDraw.setLastAdvanceTime(now);
                WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100002,
                        DateUtils.addHours(now, PubDrawConstants.DRAW_ADVANCE_HOURS).getTime());
                playingRole.write(pushmsg.build(playingRole.alloc()));
            }
        }
        return pubDraw;
    }

    public WsMessageBase.IORecruitFree buildRecruitFree(int playerId) {
        WsMessageBase.IORecruitFree ret = new WsMessageBase.IORecruitFree(0,0);
        PubDraw pubDraw = PubDrawCache.getInstance().getPubDraw(playerId);
        if (pubDraw != null) {
            Date lastNormalTime = pubDraw.getLastNormalTime();
            if (lastNormalTime != null) {
                ret.normal = DateUtils.addHours(lastNormalTime, PubDrawConstants.DRAW_NORMAL_HOURS).getTime();
            }
            Date lastAdvanceTime = pubDraw.getLastAdvanceTime();
            if (lastAdvanceTime != null) {
                ret.premium = DateUtils.addHours(lastAdvanceTime, PubDrawConstants.DRAW_ADVANCE_HOURS).getTime();
            }
        }
        return ret;
    }

}
