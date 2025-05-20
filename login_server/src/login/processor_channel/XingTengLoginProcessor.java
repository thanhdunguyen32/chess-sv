package login.processor_channel;

import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import login.logic.LoginAccountManager;
import login.stat.LoginStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageLogin.C2SXingTengLogin;

@MsgCodeAnn(msgcode = C2SXingTengLogin.id, accessLimit = 200)
public class XingTengLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(XingTengLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {

	}

	@Override
	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
		C2SXingTengLogin garenaLogin = C2SXingTengLogin.parse(request);
		String appId = garenaLogin.app_id;
		String uin = garenaLogin.uin;
		String token = garenaLogin.login_token;
		logger.info("{}-xingteng-login,appId={},userId={},token={}", C2SXingTengLogin.id, appId, uin, token);
		LoginStat.getInstance().saveLoginTime(uin);
		String appKey = "f528feb6b28242b6a7bbf7a6939a38f0";
		loginAccountManager.yijieSwitchLogin(session, appId, uin, token, appKey, "xt-");
	}

}
