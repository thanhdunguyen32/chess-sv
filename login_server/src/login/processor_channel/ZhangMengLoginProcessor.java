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
import ws.WsMessageLogin.C2SZhangMengLogin;

@MsgCodeAnn(msgcode = C2SZhangMengLogin.id, accessLimit = 200)
public class ZhangMengLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ZhangMengLoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SZhangMengLogin reqMsg = C2SZhangMengLogin.parse(request);
		String sign = reqMsg.sign;
		String t = reqMsg.t;
		String uid = reqMsg.uid;
		logger.info("zhang meng login,t={},uid={},sign={}", t,uid,sign);
		LoginStat.getInstance().saveLoginTime(uid);
		loginAccountManager.zhangmengLogin(player, t,uid,sign);
	}

}
