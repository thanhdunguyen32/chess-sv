package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendRequest;
import game.module.friend.dao.FriendCache;
import game.module.friend.logic.FriendManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendApplyList.id, accessLimit = 200)
public class FriendApplyListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendApplyListProcessor.class);

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
        logger.info("friend apply list!");
        int playerId = playingRole.getId();
        //好友请求列表
        WsMessageFriend.S2CFriendApplyList respmsg = new WsMessageFriend.S2CFriendApplyList();
        Collection<FriendRequest> friendRequests = FriendCache.getInstance().getFriendRequests(playerId).values();
        if (friendRequests.size() > 0) {
            respmsg.items = new ArrayList<>(friendRequests.size());
            for (FriendRequest friendRequest : friendRequests) {
                Integer friendId = friendRequest.getRequestPlayerId();
                WsMessageBase.IOFriendEntity ioFriendEntity = new WsMessageBase.IOFriendEntity();
                ioFriendEntity.id = friendId;
                FriendManager.getInstance().buildIoFriend(ioFriendEntity, playerId, friendId);
                respmsg.items.add(ioFriendEntity);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
