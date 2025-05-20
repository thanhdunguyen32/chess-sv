package game.module.mine.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mine.logic.MineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageMine;

@MsgCodeAnn(msgcode = WsMessageMine.C2SMineRedNotice.id, accessLimit = 200)
public class MineRedNoticeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MineRedNoticeProcessor.class);

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
        logger.info("mine red notice!playerId={}", playerId);
        WsMessageMine.S2CMineRedNotice respMsg = new WsMessageMine.S2CMineRedNotice(false);
        respMsg.ret = MineManager.getInstance().checkRedPoints(playerId);
        // ret
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
