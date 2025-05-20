package game.module.user.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.lan.bean.LoginSessionBean;
import game.module.lan.dao.LoginSessionCache;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SQueryHasRole;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageBase.WanbaLoginGift;

@MsgCodeAnn(msgcode = C2SQueryHasRole.id, accessLimit = 200)
public class QueryHasRoleProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QueryHasRoleProcessor.class);

	private PlayerManager playerManager = PlayerManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SQueryHasRole reqMsg = C2SQueryHasRole.parse(request);
		long loginSessionId = reqMsg.session_id;
		WanbaLoginGift qqOpenData = reqMsg.qq_open_data;
		logger.info("获取角色列表：loginSessionId={},qqOpenData={}", loginSessionId, qqOpenData);
		// 判断session是否有效
		LoginSessionBean loginSessionBean = LoginSessionCache.getInstance().getLoginSessionBean(loginSessionId);
		if (loginSessionBean == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CQueryHasRole.msgCode, 106);
			player.writeAndFlush(respMsg.build(player.alloc()));
			return;
		}
		// 查询角色列表
		playerManager.queryCharacterList(player, loginSessionBean, qqOpenData);
		// update cache
		LoginSessionCache.getInstance().updateLoginSession(loginSessionBean);
	}

}
