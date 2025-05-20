//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SZyLogin;
//import lion.common.MsgCodeAnn;
//import lion.netty4.codec.ProtoUtil;
//import lion.netty4.message.GamePlayer;
//import lion.netty4.message.MyRequestMessage;
//import lion.netty4.message.RequestMessage;
//import lion.netty4.message.RequestProtoMessage;
//import lion.netty4.processor.MsgProcessor;
//import login.logic.LoginAccountManager;
//import login.stat.LoginStat;
//
//@MsgCodeAnn(msgcode = 10031, accessLimit = 200)
//public class ZyLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(ZyLoginProcessor.class);
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
//		C2SZyLogin garenaLogin = ProtoUtil.getProtoObj(C2SZyLogin.parser(), request);
//		String accessToken = garenaLogin.getAccessToken();
//		String zyUserId = garenaLogin.getUserId();
//		logger.info("10031-zy-login,accessToken={},zyUserId={}", accessToken, zyUserId);
//		LoginStat.getInstance().saveLoginTime(accessToken);
//		loginAccountManager.zyLogin(player,zyUserId, accessToken);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
