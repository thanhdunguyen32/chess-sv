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
import ws.WsMessageLogin.C2SYunbeeLogin;

@MsgCodeAnn(msgcode = C2SYunbeeLogin.id, accessLimit = 200)
public class YunbeeLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(YunbeeLoginProcessor.class);

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
		C2SYunbeeLogin garenaLogin = C2SYunbeeLogin.parse(request);
		String user_id = garenaLogin.user_id;
		String token = garenaLogin.token;
		logger.info("{}-yunbee-login,userId={},token={}", C2SYunbeeLogin.id, user_id, token);
		LoginStat.getInstance().saveLoginTime(user_id);
		loginAccountManager.yunbeeLogin(session, user_id, token);
	}

}
