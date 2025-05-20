//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SDouyuLogin;
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
//@MsgCodeAnn(msgcode = 10049, accessLimit = 200)
//public class DouyuLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(DouyuLoginProcessor.class);
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
//		C2SDouyuLogin garenaLogin = ProtoUtil.getProtoObj(C2SDouyuLogin.parser(), request);
//		String uin = garenaLogin.getUin();
//		String token = garenaLogin.getToken();
//		logger.info("10049-douyu-login,uin={},token={}", uin, token);
//		LoginStat.getInstance().saveLoginTime(token);
//		loginAccountManager.douyuLogin(player, uin, token);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
