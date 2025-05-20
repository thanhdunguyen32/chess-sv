package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.dao.FriendCache;
import game.module.friend.logic.FriendConstants;
import game.module.friend.logic.FriendManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendRecommandList.id, accessLimit = 200)
public class FriendRecommandProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendRecommandProcessor.class);

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
        logger.info("friend recommand!player={}",playerId);
        //好友列表
        WsMessageFriend.S2CFriendRecommandList respmsg = new WsMessageFriend.S2CFriendRecommandList();
        respmsg.items = new ArrayList<>();
        Map<Integer, FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        Collection<PlayingRole> onlinePlayers = SessionManager.getInstance().getAllPlayers();
        if (onlinePlayers.size() > FriendConstants.FRIEND_RECOMMAND_SIZE) {
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (PlayingRole playingRole1 : onlinePlayers) {
                int friendId = playingRole1.getId();
                if (friendId == playerId) {
                    continue;
                }
                if (friends != null && friends.containsKey(friendId)) {
                    continue;
                }
                rd.put(1, friendId);
            }
            //随机5个
            for (int i = 0; i < FriendConstants.FRIEND_RECOMMAND_SIZE && rd.size() > 0; i++) {
                Integer randPlayerId = rd.randomRemove();
                WsMessageBase.IOFriendEntity ioFriendEntity = new WsMessageBase.IOFriendEntity();
                ioFriendEntity.id = randPlayerId;
                FriendManager.getInstance().buildIoFriend(ioFriendEntity, playerId, randPlayerId);
                respmsg.items.add(ioFriendEntity);
            }
        } else {
            //最少5个
            for (PlayingRole playingRole1 : onlinePlayers) {
                int friendId = playingRole1.getId();
                if (friendId == playerId) {
                    continue;
                }
                if (friends != null && friends.containsKey(friendId)) {
                    continue;
                }
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
