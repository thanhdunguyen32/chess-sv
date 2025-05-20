//package login.processor;
//
//import game.module.user.ProtoMessageLogin.C2SGarenaLogin;
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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@MsgCodeAnn(msgcode = 10021, accessLimit = 200)
//public class GarenaLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(GarenaLoginProcessor.class);
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
//		C2SGarenaLogin garenaLogin = ProtoUtil.getProtoObj(C2SGarenaLogin.parser(), request);
//		String accessToken = garenaLogin.getAccessToken();
//		logger.info("10021-login,accessToken={}", accessToken);
//		LoginStat.getInstance().saveLoginTime(accessToken);
//		loginAccountManager.garenaLogin(player, accessToken);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
