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
import ws.WsMessageLogin.C2SXingTengLogin2;

@MsgCodeAnn(msgcode = C2SXingTengLogin2.id, accessLimit = 200)
public class XingTengLoginProcessor2 extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(XingTengLoginProcessor2.class);

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
		C2SXingTengLogin2 garenaLogin = C2SXingTengLogin2.parse(request);
		String appId = garenaLogin.app_id;
		String uin = garenaLogin.uin;
		String token = garenaLogin.login_token;
		String appKey = garenaLogin.app_key;
		logger.info("{}-xingteng-login,appId={},userId={},token={},appKey={}", C2SXingTengLogin2.id, appId, uin, token, appKey);
		LoginStat.getInstance().saveLoginTime(uin);
		loginAccountManager.yijieSwitchLogin(session, appId, uin, token, appKey, "xt-");
	}

}
