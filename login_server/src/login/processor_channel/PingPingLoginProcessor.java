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
import ws.WsMessageLogin.C2SPingPingLogin;

@MsgCodeAnn(msgcode = C2SPingPingLogin.id, accessLimit = 200)
public class PingPingLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(PingPingLoginProcessor.class);

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
		C2SPingPingLogin garenaLogin = C2SPingPingLogin.parse(request);
		String game_id = garenaLogin.game_id;
		String user_code = garenaLogin.user_code;
		String login_token = garenaLogin.login_token;
		String game_key = garenaLogin.game_key;
		logger.info("{}-pingping-login,game_id={},user_code={},login_token={}", C2SPingPingLogin.id, game_id, user_code, login_token);
		LoginStat.getInstance().saveLoginTime(user_code);
		loginAccountManager.pingPingLogin(session, game_id, user_code, login_token, game_key);
	}

}
