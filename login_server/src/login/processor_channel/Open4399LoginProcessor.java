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
import ws.WsMessageLogin.C2S4399Login;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@MsgCodeAnn(msgcode = C2S4399Login.id, accessLimit = 200)
public class Open4399LoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(Open4399LoginProcessor.class);

	private LoginAccountManager loginAccountManager = LoginAccountManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2S4399Login reqMsg = C2S4399Login.parse(request);
		int gameId = reqMsg.gameId;
		String userName = URLDecoder.decode(reqMsg.userName, "UTF-8");
		String userId = reqMsg.userId;
		int time = reqMsg.time;
		String sign = reqMsg.sign;
		logger.info("4399 login,time={},userId={},sign={},gameId={},userName={}", time, userId, sign, gameId, userName);
		LoginStat.getInstance().saveLoginTime(userId);
		loginAccountManager.open4399Login(player, userId, time, sign, gameId, userName);
	}
	
	public static void main(String[] args) {
		String a1;
		try {
			a1 = URLDecoder.decode("ceshi%402", "UTF-8");
			System.out.println(a1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
