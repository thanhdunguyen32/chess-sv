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
import ws.WsMessageLogin.C2SWanbaLogin;

@MsgCodeAnn(msgcode = C2SWanbaLogin.id, accessLimit = 200)
public class WanbaLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(WanbaLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SWanbaLogin reqMsg = C2SWanbaLogin.parse(request);
		String appid = reqMsg.appid;
		String openid = reqMsg.openid;
		String openkey = reqMsg.openkey;
		int platform = reqMsg.platform;
		String pf = reqMsg.pf;
		logger.info("wan ba login,appid={},openid={},openkey={},playform={},pf={}", appid, openid, openkey, platform, pf);
		LoginStat.getInstance().saveLoginTime(openid);
		loginAccountManager.wanbaLogin(player, appid, openid, openkey, platform, pf);
	}

}
