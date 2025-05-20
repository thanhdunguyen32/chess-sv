package game.module.tower.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.tower.bean.TowerReplay;
import game.module.tower.dao.TowerReplayCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBattle;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2STowerReplay.id, accessLimit = 200)
public class TowerReplayProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TowerReplayProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2STowerReplay reqmsg = WsMessageBattle.C2STowerReplay.parse(request);
        logger.info("tower replay!player={},req={}", playerId, reqmsg);
        //ret
        WsMessageBattle.S2CTowerReplay respmsg = new WsMessageBattle.S2CTowerReplay();
        int tower_level = reqmsg.tower_level;
        TowerReplay towerReplay = TowerReplayCache.getInstance().getTowerReplay(tower_level);
        if (towerReplay != null && towerReplay.getDbTowerReplay() != null && towerReplay.getDbTowerReplay().getRecords() != null) {
            respmsg.records = towerReplay.getDbTowerReplay().getRecords();
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
