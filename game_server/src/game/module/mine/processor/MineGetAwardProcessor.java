package game.module.mine.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageMine.C2SMineGetAward;
import ws.WsMessageMine.S2CMineGetAward;

import java.util.List;

@MsgCodeAnn(msgcode = C2SMineGetAward.id, accessLimit = 200)
public class MineGetAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MineGetAwardProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("mine get award!playerId={}", playerId);
        DBMine mineEntity = MineCache.getInstance().getDBMine();
        if (mineEntity == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CMineGetAward.msgCode, 2104);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        DBMinePlayer minePlayerEntity = mineEntity.getPlayers().get(playerId);
        if (minePlayerEntity == null) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CMineGetAward.msgCode, 2104);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        List<Integer> gainList = minePlayerEntity.getGains();
        boolean isAll0 = true;
        for (Integer aGain : gainList) {
            if (aGain != 0) {
                isAll0 = false;
                break;
            }
        }
        if (isAll0) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CMineGetAward.msgCode, 2104);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // do
        S2CMineGetAward respMsg = MineManager.getInstance().mineGetAward(playingRole);
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
