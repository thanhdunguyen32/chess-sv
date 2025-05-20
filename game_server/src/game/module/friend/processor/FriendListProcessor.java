package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.bean.FriendBean;
import game.module.friend.bean.FriendHeartSend;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendshipSendCache;
import game.module.friend.logic.FriendManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.session.PlayerOnlineCacheMng;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendList.id, accessLimit = 200)
public class FriendListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendListProcessor.class);

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
        logger.info("friend list!");
        int playerId = playingRole.getId();
        //好友列表
        WsMessageFriend.S2CFriendList respmsg = new WsMessageFriend.S2CFriendList();
        respmsg.maxcount = 50;
        respmsg.surplusgiftmax = 10;
        Map<Integer,FriendBean> friends = FriendCache.getInstance().getFriends(playerId);
        if (friends != null && friends.size() > 0) {
            respmsg.arrfriend = new ArrayList<>(friends.size());
            for (FriendBean friendBean : friends.values()) {
                Integer friendId = friendBean.getFriendId();
                WsMessageBase.IOFriendEntity ioFriendEntity = new WsMessageBase.IOFriendEntity();
                ioFriendEntity.id = friendId;
                FriendManager.getInstance().buildIoFriend(ioFriendEntity,playerId,friendId);
                respmsg.arrfriend.add(ioFriendEntity);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
