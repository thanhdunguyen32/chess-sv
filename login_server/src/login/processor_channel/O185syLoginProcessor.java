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
import ws.WsMessageLogin.C2S185syLogin;

@MsgCodeAnn(msgcode = C2S185syLogin.id, accessLimit = 200)
public class O185syLoginProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(O185syLoginProcessor.class);

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
		C2S185syLogin garenaLogin = C2S185syLogin.parse(request);
		String userID = garenaLogin.userID;
		String token = garenaLogin.token;
		logger.info("{}-185sy-login,userID={},token={}", C2S185syLogin.id, userID, token);
		LoginStat.getInstance().saveLoginTime(userID);
		loginAccountManager.o185syLogin(session, userID, token);
	}

}
