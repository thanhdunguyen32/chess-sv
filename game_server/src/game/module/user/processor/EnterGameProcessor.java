package game.module.user.processor;

import game.module.lan.bean.LoginSessionBean;
import game.module.lan.dao.LoginSessionCache;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SEnterGame;
import ws.WsMessageBase.WanbaLoginGift;

@MsgCodeAnn(msgcode = C2SEnterGame.id, accessLimit = 200)
public class EnterGameProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(EnterGameProcessor.class);

    private PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    public void process(GamePlayer session, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
//		C2SEnterGame enterGame = ProtoUtil.getProtoObj(C2SEnterGame.parser(), request);
//		long loginSessionId = enterGame.getSessionId();
//		logger.info("enter game,loginSessionId={}", loginSessionId);
//		// 判断session是否有效
//		LoginSessionBean loginSessionBean = LoginSessionCache.getInstance().getLoginSessionBean(loginSessionId);
//		if (loginSessionBean == null) {
//			player.writeAndFlush(10106, RetCode.INVALID_SESSION_ID);
//			return;
//		}
//		// 创建角色
//		playerManager.enterGame(player, loginSessionBean);
    }

    @Override
    public void process(GamePlayer player, MyRequestMessage request) throws Exception {
        C2SEnterGame reqMsg = C2SEnterGame.parse(request);
        long loginSessionId = reqMsg.session_id;
        WanbaLoginGift wanbaLoginGift = reqMsg.wanba_gift;
        logger.info("enter game,loginSessionId={},wanbaLoginGift={}", loginSessionId, wanbaLoginGift);
        // 判断session是否有效
        LoginSessionBean loginSessionBean = LoginSessionCache.getInstance().getLoginSessionBean(loginSessionId);
        if (loginSessionBean == null) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CEnterGame.msgCode, 106);
            player.writeAndFlush(retMsg.build(player.alloc()));
            return;
        }
        // 添加session信息
        player.saveSessionId(loginSessionId);
        // 创建角色
        playerManager.enterGame(player, loginSessionBean, wanbaLoginGift);
    }

}
