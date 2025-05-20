//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SSifubaLogin;
//import lion.common.MsgCodeAnn;
//import lion.netty4.codec.ProtoUtil;
//import lion.netty4.message.GamePlayer;
//import lion.netty4.message.MyRequestMessage;
//import lion.netty4.message.RequestMessage;
//import lion.netty4.message.RequestProtoMessage;
//import lion.netty4.processor.MsgProcessor;
//import login.logic.LoginAccountManager;
//
//@MsgCodeAnn(msgcode = 10025, accessLimit = 200)
//public class SifubaLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(SifubaLoginProcessor.class);
//
//	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();
//
//	@Override
//	public void process(GamePlayer session, RequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
//		C2SSifubaLogin sifubaLogin = ProtoUtil.getProtoObj(C2SSifubaLogin.parser(), request);
//		String userId = sifubaLogin.getUserId();
//		logger.info("Sifuba login,userId={}", userId);
//		// TODO 进行消息验证
//		loginAccountManager.sifubaLogin(player, userId);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
