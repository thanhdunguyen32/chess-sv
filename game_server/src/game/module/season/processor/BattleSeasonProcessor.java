package game.module.season.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.pay.logic.LibaoBuyManager;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.season.dao.SeasonDaoHelper;
import game.module.season.logic.SeasonManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Date;

@MsgCodeAnn(msgcode = WsMessageHall.C2SBattleSeason.id, accessLimit = 200)
public class BattleSeasonProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BattleSeasonProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("battle season get!,player={}", playerId);
        //
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        Date now = new Date();
        if(battleSeason.getEtime().before(now)){
            synchronized (battleSeason) {
                if(battleSeason.getEtime().before(now)) {
                    LibaoBuyManager.getInstance().resetWeekBuyInfo(battleSeason.getSeason());
                    ActivityWeekManager.getInstance().resetWeekActMark();
                    ActivityWeekManager.getInstance().resetWeekActBean();
                    //update season
                    SeasonManager.getInstance().updateSeason(battleSeason);
                    SeasonDaoHelper.asyncUpdateBattleSeason(battleSeason);
                }
            }
        }
        //ret
        WsMessageHall.S2CBattleSeason respmsg = new WsMessageHall.S2CBattleSeason();
        respmsg.vals = battleSeason.getAddonVals();
        respmsg.info = new WsMessageBase.IOSeason();
        respmsg.info.etime = battleSeason.getEtime().getTime();
        respmsg.info.pos = battleSeason.getPos();
        respmsg.info.season = battleSeason.getSeason();
        respmsg.info.year = battleSeason.getYear();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
