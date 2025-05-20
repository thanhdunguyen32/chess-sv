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
import ws.WsMessageLogin.C2SQunheiLogin;

@MsgCodeAnn(msgcode = C2SQunheiLogin.id, accessLimit = 200)
public class QunheiLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QunheiLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SQunheiLogin reqMsg = C2SQunheiLogin.parse(request);
		String username = reqMsg.username;
		int serverid = reqMsg.serverid;
		int isadult = reqMsg.isadult;
		int time = reqMsg.time;
		String flag = reqMsg.flag;
		logger.info("qun hei login,username={},serverid={},isadult={},time={},flag={}", username, serverid, isadult,
				time, flag);
		LoginStat.getInstance().saveLoginTime(username);
		loginAccountManager.qunheiLogin(player, username, serverid, isadult, time, flag);
	}

}
