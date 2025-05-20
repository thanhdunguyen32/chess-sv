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
import ws.WsMessageLogin.C2SMiaoRenLogin;

@MsgCodeAnn(msgcode = C2SMiaoRenLogin.id, accessLimit = 200)
public class MoXiLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(MoXiLoginProcessor.class);

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
		C2SMiaoRenLogin garenaLogin = C2SMiaoRenLogin.parse(request);
		String appId = garenaLogin.app_id;
		String account_id = garenaLogin.account_id;
		String token_key = garenaLogin.token_key;
		logger.info("{}-miaoren-login,appId={},account_id={},token_key={}", C2SMiaoRenLogin.id, appId, account_id, token_key);
		LoginStat.getInstance().saveLoginTime(account_id);
		loginAccountManager.miaoRenLogin(session, appId, account_id, token_key, "mr-");
	}

}
