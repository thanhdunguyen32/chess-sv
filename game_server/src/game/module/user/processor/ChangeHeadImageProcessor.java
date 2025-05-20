package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.user.bean.PlayerHead;
import game.module.user.dao.PlayerHeadCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageHall.C2SChangeHeadImage.id, accessLimit = 200)
public class ChangeHeadImageProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChangeHeadImageProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SChangeHeadImage reqMsg = WsMessageHall.C2SChangeHeadImage.parse(request);
        final int imageId = reqMsg.image_id;
        int playerId = playingRole.getId();
        logger.info("change head image,player={},image={}", playerId, imageId);
        //是否拥有
        PlayerHead playerHead = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        if (playerHead == null || playerHead.getHeadImages() == null) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadImage.msgCode, 162);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        Set<Integer> headImages = playerHead.getHeadImages();
        if (!headImages.contains(imageId)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadImage.msgCode, 162);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        playingRole.getPlayerBean().setImageid(imageId);
        //push
        WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(100008,imageId);
        //ret
        WsMessageHall.S2CChangeHeadImage respmsg = new WsMessageHall.S2CChangeHeadImage(imageId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
