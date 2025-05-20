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

@MsgCodeAnn(msgcode = WsMessageHall.C2SGuideOne.id, accessLimit = 200)
public class GuideOneProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuideOneProcessor.class);

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
        logger.info("guide one,player={}", playerId);
        //ret
        WsMessageHall.S2CGuideOne respmsg = new WsMessageHall.S2CGuideOne(false);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
