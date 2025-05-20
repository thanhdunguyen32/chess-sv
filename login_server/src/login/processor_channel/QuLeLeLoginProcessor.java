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
import ws.WsMessageLogin.C2SQuLeLeLogin;

@MsgCodeAnn(msgcode = C2SQuLeLeLogin.id, accessLimit = 200)
public class QuLeLeLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QuLeLeLoginProcessor.class);

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
		C2SQuLeLeLogin garenaLogin = C2SQuLeLeLogin.parse(request);
		String appId = garenaLogin.app_id;
		String user_id = garenaLogin.user_id;
		String session_id = garenaLogin.session_id;
		logger.info("{}-qulele-login,appId={},userId={},token={},appKey={}", C2SQuLeLeLogin.id, appId, user_id, session_id);
		LoginStat.getInstance().saveLoginTime(user_id);
		loginAccountManager.quLeLeLogin(session, appId, user_id, session_id, "qll-");
	}

}
