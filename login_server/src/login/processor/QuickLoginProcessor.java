package login.processor;

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
import ws.WsMessageLogin;

@MsgCodeAnn(msgcode = WsMessageLogin.C2SQuickLogin.id, accessLimit = 200)
public class QuickLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QuickLoginProcessor.class);

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
		WsMessageLogin.C2SQuickLogin garenaLogin = WsMessageLogin.C2SQuickLogin.parse(request);
		String token = garenaLogin.token;
		String product_code = garenaLogin.product_code;
		String uid = garenaLogin.uid;
		String channel_code = garenaLogin.channel_code;
		logger.info("{}-quick-login,token={},product_code={},uid={},channel_code={}", WsMessageLogin.C2SQuickLogin.id, token, product_code, uid, channel_code);
		LoginStat.getInstance().saveLoginTime(channel_code+"-"+uid);
		loginAccountManager.quickLogin(session, token, product_code, uid, channel_code);
	}

}
