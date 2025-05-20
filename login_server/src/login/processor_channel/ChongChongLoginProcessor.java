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
import ws.WsMessageLogin.C2SChongChongLogin;

@MsgCodeAnn(msgcode = C2SChongChongLogin.id, accessLimit = 200)
public class ChongChongLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ChongChongLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SChongChongLogin reqMsg = C2SChongChongLogin.parse(request);
		String userId = reqMsg.userId;
		String time = reqMsg.time;
		String sign = reqMsg.sign;
		logger.info("chong chong login,time={},uid={},sign={}", time, userId, sign);
		LoginStat.getInstance().saveLoginTime(userId);
		loginAccountManager.chongchongLogin(player, userId, time, sign);
	}

}
