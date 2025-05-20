//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.module.user.ProtoMessageLogin.C2SYunDingLogin;
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
//@MsgCodeAnn(msgcode = 10041, accessLimit = 200)
//public class YunDingLoginProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(YunDingLoginProcessor.class);
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
//		C2SYunDingLogin garenaLogin = ProtoUtil.getProtoObj(C2SYunDingLogin.parser(), request);
//		int uid = garenaLogin.getUid();
//		String userToken = garenaLogin.getUserToken();
//		logger.info("10041-yunding-login,uid={},userToken={}", uid, userToken);
//		LoginStat.getInstance().saveLoginTime(userToken);
//		loginAccountManager.yunDingLogin(player, uid, userToken);
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
