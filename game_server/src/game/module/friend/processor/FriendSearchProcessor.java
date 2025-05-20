package game.module.friend.processor;

import game.GameServer;
import game.common.CommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.logic.FriendManager;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageFriend;
import ws.WsMessageHall;

import java.util.Collections;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageFriend.C2SFriendSearch.id, accessLimit = 200)
public class FriendSearchProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendSearchProcessor.class);

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
        WsMessageFriend.C2SFriendSearch reqmsg = WsMessageFriend.C2SFriendSearch.parse(request);
        int playerId = playingRole.getId();
        logger.info("friend search!player={},req={}", playerId, reqmsg);
        String rname = reqmsg.rname;
        if (StringUtils.isBlank(rname) || CommonUtils.getStrLength(rname) > 12) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendSearch.msgCode, 110);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        if (rname.equals(playingRole.getPlayerBean().getName())) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendSearch.msgCode, 112);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 包含\n
        if (rname.indexOf('\n') > -1) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageFriend.S2CFriendSearch.msgCode, 110);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // do
        GameServer.executorService.execute(() -> {
            //ret
            WsMessageFriend.S2CFriendSearch respmsg = new WsMessageFriend.S2CFriendSearch();
            respmsg.rname = rname;
            PlayerBean existUser = null;
            if (StringUtils.isNumeric(rname)) {
                existUser = PlayerDao.getInstance().getPlayerById(Integer.parseInt(rname));
            } else {
                existUser = PlayerDao.getInstance().getPlayerByName(rname);
            }
            if (existUser != null) {
                WsMessageBase.IOFriendEntity retFriendEntity = new WsMessageBase.IOFriendEntity();
                retFriendEntity.id = existUser.getId();
                FriendManager.getInstance().buildIoFriend(retFriendEntity, playerId, existUser.getId());
                respmsg.items = Collections.singletonList(retFriendEntity);
            }
            //ret
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

}
