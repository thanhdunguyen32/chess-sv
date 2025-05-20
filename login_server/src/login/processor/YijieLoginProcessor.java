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
import ws.WsMessageLogin.C2SYiJieLogin;

@MsgCodeAnn(msgcode = C2SYiJieLogin.id, accessLimit = 200)
public class YijieLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(YijieLoginProcessor.class);

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
		C2SYiJieLogin garenaLogin = C2SYiJieLogin.parse(request);
		String appId = garenaLogin.appId;
		String channelid = garenaLogin.channelId;
		String userId = garenaLogin.userId;
		String token = garenaLogin.token;
		logger.info("10051-yijie-login,appId={},channelid={},userId={},token={}", appId, channelid, userId, token);
		LoginStat.getInstance().saveLoginTime(userId);
		loginAccountManager.yijieLogin(session, appId, channelid, userId, token);
	}

}
