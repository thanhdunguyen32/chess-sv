package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MsgCodeAnn(msgcode = 10817, accessLimit = 200)
public class Xiao7GetOrderIdProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(Xiao7GetOrderIdProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		C2SXiao7GetOrderId reqMsg = ProtoUtil.getProtoObj(C2SXiao7GetOrderId.parser(), request);
//		final int productId = reqMsg.getProductId();
//		logger.info("xiao7 get order id!,productId={}", productId);
//		final long randOrderId = SessionManager.getInstance().generateSessionId();
//		GameServer.executorService.execute(new Runnable() {
//			public void run() {
//				// 发送到login server
//				String openId = playingRole.getPlayerBean().getAccountId();
//				String loginHost = "xiao7.lejigames.com";
//				int loginPort = 8651;
//				if (GameServer.getInstance().getLanClientManager().connect(loginHost, loginPort)) {
//					int serverId = playingRole.getPlayerBean().getServerId();
//					GameServer.getInstance().getLanClientManager().sendPay2Ls(loginHost, loginPort, randOrderId,
//							serverId, openId, productId);
//				}
//			}
//		});
//		playingRole.getGamePlayer().writeAndFlush(10818,
//				S2CXiao7GetOrderId.newBuilder().setOrderId(String.valueOf(randOrderId)).setProductId(productId).build());
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
