package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.user.bean.PlayerHead;
import game.module.user.dao.PlayerHeadCache;
import game.module.user.logic.PlayerHeadManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageHall.C2SChangeHeadFrame.id, accessLimit = 200)
public class ChangeHeadFrameProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChangeHeadFrameProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SChangeHeadFrame reqMsg = WsMessageHall.C2SChangeHeadFrame.parse(request);
        final int headFrameId = reqMsg.head_frame_id;
        int playerId = playingRole.getId();
        logger.info("change head frame,player={},frame={}", playerId, headFrameId);
        //是否拥有
        PlayerHead playerHead = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        if (playerHead == null || playerHead.getHeadFrames() == null) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadFrame.msgCode, 161);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        Set<Integer> headFrames = playerHead.getHeadFrames();
        if (!headFrames.contains(headFrameId)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadFrame.msgCode, 161);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        playingRole.getPlayerBean().setFrameid(headFrameId);
        //push
        WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(100007,headFrameId);
        //ret
        WsMessageHall.S2CChangeHeadFrame respmsg = new WsMessageHall.S2CChangeHeadFrame(headFrameId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
