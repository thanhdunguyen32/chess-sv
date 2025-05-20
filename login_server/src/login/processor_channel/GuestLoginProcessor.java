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
import ws.WsMessageLogin.C2SGuestLogin;

@MsgCodeAnn(msgcode = C2SGuestLogin.id, accessLimit = 200)
public class GuestLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuestLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
//		C2SGuestLogin garenaLogin = ProtoUtil.getProtoObj(C2SGuestLogin.parser(), request);
//		int accountId = garenaLogin.getAccountId();
//		logger.info("10033-guest-login,accountId={}", accountId);
//		LoginStat.getInstance().saveLoginTime(String.valueOf(accountId));
//		loginAccountManager.guestLogin(player, accountId);
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SGuestLogin reqMsg = C2SGuestLogin.parse(request);
		int raw_uid = reqMsg.uid;
		logger.info("10015-guest-login,accountId={}", raw_uid);
		LoginStat.getInstance().saveLoginTime(String.valueOf(raw_uid));
		loginAccountManager.guestLogin(player, raw_uid);
	}

}
