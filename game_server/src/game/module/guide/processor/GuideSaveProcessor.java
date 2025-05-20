package game.module.guide.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGuideSave.id, accessLimit = 200)
public class GuideSaveProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuideSaveProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SGuideSave reqmsg = WsMessageHall.C2SGuideSave.parse(request);
        int playerId = playingRole.getId();
        logger.info("guide save,player={},req={}", playerId, reqmsg);
        //ret
        WsMessageHall.S2CGuideSave respmsg = new WsMessageHall.S2CGuideSave();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
