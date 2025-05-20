package game.module.pvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.bean.PvpPlayer;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.logic.PvpManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SGetPvpAgainst.id, accessLimit = 200)
public class GetPvpAgainstProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetPvpAgainstProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("get pvp against,player={}", playerId);
        //init pvp score
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        if (!pvpBean.getPvpScore().containsKey(playerId)) {
            PvpManager.getInstance().initMyPvpScore(playerId);
        }
        //ret
        Map<Integer, Integer> myRankMap = pvpBean.getMyRankMap();
        Integer myRank = myRankMap.get(playerId);
        WsMessageBattle.S2CGetPvpAgainst respmsg = new WsMessageBattle.S2CGetPvpAgainst();
        List<Integer> rids = new ArrayList<>();
        PvpManager.getInstance().getPvpRankLock().readLock().lock();
        List<Integer> players = pvpBean.getPlayers();
        if (myRank == null) {
            //select last 3
            for (int i = players.size() - 1, j = 0; i >= 0 && j <= 3; i--) {
                Integer pvpPlayer = players.get(i);
                rids.add(pvpPlayer);
                j++;
            }
        } else {
            //forward 2, back 1
            int rankMin = myRank - 10;
            int rankMax = myRank + 10;
            rankMin = Math.max(0, rankMin);
            rankMax = Math.min(players.size() - 1, rankMax);
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = rankMin; i <= rankMax; i++) {
                rd.put(1, i);
            }
            int selectSize = 0;
            while (rd.size() > 0 && selectSize < 3) {
                Integer randIndex = rd.randomRemove();
                Integer pvpPlayer = players.get(randIndex);
                if (pvpPlayer != playerId) {
                    rids.add(pvpPlayer);
                    selectSize++;
                }
            }
        }
        PvpManager.getInstance().getPvpRankLock().readLock().unlock();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();
        rids.sort(Comparator.comparingInt(pvpScore::get).reversed());
        respmsg.rids = rids;
        //save bean
        Map<Integer, PvpPlayer> myPvpInfo = pvpBean.getPvpPlayerInfo();
        if (!myPvpInfo.containsKey(playerId)) {
            PvpPlayer pvpPlayer = new PvpPlayer(playerId);
            pvpPlayer.setAgainstPlayers(rids);
            myPvpInfo.put(playerId, pvpPlayer);
        } else {
            PvpPlayer pvpPlayer = myPvpInfo.get(playerId);
            pvpPlayer.setAgainstPlayers(rids);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
