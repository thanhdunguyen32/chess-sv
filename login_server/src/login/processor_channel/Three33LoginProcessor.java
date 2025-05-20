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
import ws.WsMessageLogin.C2SThree33Login;

@MsgCodeAnn(msgcode = C2SThree33Login.id, accessLimit = 200)
public class Three33LoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(Three33LoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SThree33Login reqMsg = C2SThree33Login.parse(request);
		int gameId = reqMsg.gameId;
		int time = reqMsg.time;
		int uid = reqMsg.uid;
		String userName = reqMsg.userName;
		String sign = reqMsg.sign;
		logger.info("333 login,time={},uid={},sign={}", time, uid, sign);
		LoginStat.getInstance().saveLoginTime(String.valueOf(uid));
		loginAccountManager.three33Login(player, gameId, time, uid,userName, sign);
	}

}
