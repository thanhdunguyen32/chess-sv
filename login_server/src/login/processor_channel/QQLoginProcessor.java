//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SQQLogin;
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
//@MsgCodeAnn(msgcode = 10029, accessLimit = 200)
//public class QQLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(QQLoginProcessor.class);
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
//		C2SQQLogin garenaLogin = ProtoUtil.getProtoObj(C2SQQLogin.parser(), request);
//		String accessToken = garenaLogin.getAccessToken();
//		String qqOpenId = garenaLogin.getOpenId();
//		logger.info("10029-qq-login,accessToken={},qqOpenId={}", accessToken, qqOpenId);
//		LoginStat.getInstance().saveLoginTime(accessToken);
//		loginAccountManager.qqLogin(player,qqOpenId, accessToken);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
