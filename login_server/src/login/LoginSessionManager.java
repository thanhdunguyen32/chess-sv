package login;

import lion.netty4.core.BaseIoExecutor;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.INetty4EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginSessionManager implements INetty4EventHandler {

	public static final Logger logger = LoggerFactory.getLogger(LoginSessionManager.class);

	static class SingletonHolder {
		static LoginSessionManager instance = new LoginSessionManager();
	}

	public static LoginSessionManager getInstance() {
		return SingletonHolder.instance;
	}

	private LoginSessionManager() {
		BaseIoExecutor.handlerSet.add(this);
		BaseProtoIoExecutor.handlerSet.add(this);
	}

	@Override
	public void channelInactive(GamePlayer gamePlayer) {

	}

	@Override
	public void readTimeout(GamePlayer gamePlayer) {
		logger.info("read timeout,close channel!addr={}", gamePlayer.getAddress());
		gamePlayer.close();
	}

}
