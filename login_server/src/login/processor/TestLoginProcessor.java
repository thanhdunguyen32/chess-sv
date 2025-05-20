package login.processor;

import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import login.logic.LoginAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageLogin;

@MsgCodeAnn(msgcode = WsMessageLogin.C2STestLogin.id, accessLimit = 200)
public class TestLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(TestLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
        WsMessageLogin.C2STestLogin reqMsg = WsMessageLogin.C2STestLogin.parse(request);
        logger.info("TestLoginProcessor.process,reqMsg={}", reqMsg);
		String userName = reqMsg.uname;
		String pwd = reqMsg.pwd;
		logger.info("{}-login,userName={},pwd={},loginIp={}", WsMessageLogin.C2STestLogin.id, userName, pwd, player.getAddress());
		loginAccountManager.loginRaw(player, userName, pwd);
	}

}
