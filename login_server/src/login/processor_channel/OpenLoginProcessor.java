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
import ws.WsMessageLogin.C2SOpenLogin;

@MsgCodeAnn(msgcode = C2SOpenLogin.id, accessLimit = 200)
public class OpenLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(OpenLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SOpenLogin reqMsg = C2SOpenLogin.parse(request);
		int platform_type = reqMsg.platform_type;
		String open_id = reqMsg.open_id;
		logger.info("open login,platform_type={},open_id={}", platform_type, open_id);
		LoginStat.getInstance().saveLoginTime(String.valueOf(open_id));
		loginAccountManager.openLogin(player, platform_type, open_id);
	}

}
