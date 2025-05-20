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

@MsgCodeAnn(msgcode = WsMessageHall.C2SChangeHeadIcon.id, accessLimit = 200)
public class ChangeHeadIconProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChangeHeadIconProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SChangeHeadIcon reqMsg = WsMessageHall.C2SChangeHeadIcon.parse(request);
        final int headId = reqMsg.head_id;
        int playerId = playingRole.getId();
        logger.info("change head icon,player={},icon={}", playerId, headId);
        //是否拥有
        PlayerHead playerHead = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        if (playerHead == null || playerHead.getHeadIcons() == null) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadIcon.msgCode, 160);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        Set<Integer> headIcons = playerHead.getHeadIcons();
        if (!headIcons.contains(headId)) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CChangeHeadIcon.msgCode, 160);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //do
        playingRole.getPlayerBean().setHeadid(headId);
        //push
        int iconId = PlayerHeadManager.getInstance().headId2IconId(headId);
        playingRole.getPlayerBean().setIconid(iconId);
        WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(100005,iconId);
        playingRole.write(pushMsg.build(playingRole.alloc()));
        WsMessageHall.PushPropChange pushMsg2 = new WsMessageHall.PushPropChange(100006,headId);
        playingRole.write(pushMsg2.build(playingRole.alloc()));
        //ret
        WsMessageHall.S2CChangeHeadIcon respmsg = new WsMessageHall.S2CChangeHeadIcon(headId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
