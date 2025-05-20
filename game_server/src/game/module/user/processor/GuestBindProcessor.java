//package game.module.user.processor;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.GameServer;
//import game.common.PlayingRoleMsgProcessor;
//import game.entity.PlayingRole;
//import game.module.user.ProtoMessageLogin.C2SGuestBind;
//import game.module.user.ProtoMessageLogin.S2CGuestBind;
//import game.module.user.bean.PlayerBean;
//import game.module.user.dao.PlayerDao;
//import lion.common.MsgCodeAnn;
//import lion.netty4.codec.ProtoUtil;
//import lion.netty4.message.MyRequestMessage;
//import lion.netty4.message.RequestMessage;
//import lion.netty4.message.RequestProtoMessage;
//
//@MsgCodeAnn(msgcode = 10035, accessLimit = 200)
//public class GuestBindProcessor extends PlayingRoleMsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(GuestBindProcessor.class);
//
//	@Override
//	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
//
//	}
//
//	@Override
//	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		C2SGuestBind garenaGuestBind = ProtoUtil.getProtoObj(C2SGuestBind.parser(), request);
//		final String openId = garenaGuestBind.getOpenId();
//		logger.info("guest bind,openId={}", openId);
//		GameServer.executorService.execute(new Runnable() {
//			@Override
//			public void run() {
//				// 账号是否已经绑定
//				List<PlayerBean> existPlayers = PlayerDao.getInstance().getPlayerByOpenId(openId,playingRole.getPlayerBean().getServerId());
//				if (existPlayers != null && existPlayers.size() > 0) {
//					playingRole.getGamePlayer().writeAndFlush(10036, S2CGuestBind.newBuilder().setRet(1));
//					return;
//				}
//				// do
//				playingRole.getPlayerBean().setAccountId(openId);
//				PlayerDao.getInstance().updateGarenaLoginInfo("", openId, playingRole.getId());
//				playingRole.getGamePlayer().writeAndFlush(10036, S2CGuestBind.newBuilder().setRet(0));
//			}
//		});
//	}
//
//	@Override
//	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
