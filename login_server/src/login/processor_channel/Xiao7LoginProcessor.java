//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SXiao7Login;
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
//@MsgCodeAnn(msgcode = 10043, accessLimit = 200)
//public class Xiao7LoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(Xiao7LoginProcessor.class);
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
//		C2SXiao7Login garenaLogin = ProtoUtil.getProtoObj(C2SXiao7Login.parser(), request);
//		String tokenKey = garenaLogin.getTokenKey();
//		logger.info("10043-xiao7-login,uid={},userToken={}", tokenKey);
//		LoginStat.getInstance().saveLoginTime(tokenKey);
//		loginAccountManager.xiao7Login(player, tokenKey);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
