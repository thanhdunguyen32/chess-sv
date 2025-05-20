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
import ws.WsMessageLogin.C2SLiuyeLogin2;

@MsgCodeAnn(msgcode = C2SLiuyeLogin2.id, accessLimit = 200)
public class LiuyeLogin2Processor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(LiuyeLogin2Processor.class);

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
		C2SLiuyeLogin2 garenaLogin = C2SLiuyeLogin2.parse(request);
		String mem_id = garenaLogin.mem_id;
		String user_token = garenaLogin.user_token;
		logger.info("{}-liuye-login,mem_id={},token={}", C2SLiuyeLogin2.id, mem_id, user_token);
		LoginStat.getInstance().saveLoginTime(mem_id);
		String app_id = "6010";
		String app_key = "4c873926bf199a10c6aca728dc731bbc";
		loginAccountManager.liuyeLogin(session, mem_id, user_token,app_id,app_key);
	}

}
