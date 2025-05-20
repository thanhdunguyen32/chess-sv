package game.module.lan.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.lan.bean.LoginSessionBean;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lion.session.GlobalTimer;

public class LoginSessionCache {

	public static final int LOGIN_SESSION_TIMEOUT = 720;// 12 minutes

	private Map<Long, LoginSessionBean> loginSessionMap = new ConcurrentHashMap<Long, LoginSessionBean>();

	private static Logger logger = LoggerFactory.getLogger(LoginSessionCache.class);

	static class SingletonHolder {
		static LoginSessionCache instance = new LoginSessionCache();
	}

	public static LoginSessionCache getInstance() {
		return SingletonHolder.instance;
	}

	private GlobalTimer globalTimer = GlobalTimer.getInstance();

	public void addLoginSession(LoginSessionBean loginSessionBean) {
		logger.info("add login session!sid={},uid={},serverid={}", loginSessionBean.getSessionId(), loginSessionBean.getOpenId(),loginSessionBean.getServerId());
		final Long sessionId = loginSessionBean.getSessionId();
		//已经存在
		if (loginSessionMap.containsKey(sessionId)) {
			LoginSessionBean oldSessionBean = loginSessionMap.get(sessionId);
			if (oldSessionBean.getTimeout() != null) {
				oldSessionBean.getTimeout().cancel();
			}
			loginSessionMap.remove(sessionId);
		}
		loginSessionMap.put(sessionId, loginSessionBean);
		Timeout timeout = globalTimer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				logger.info("loginSessionExpire,sid={},uid={}", sessionId, loginSessionMap.get(sessionId).getOpenId());
				loginSessionMap.remove(sessionId);
				logger.info("after expire size={}", loginSessionMap.size());
			}
		}, LOGIN_SESSION_TIMEOUT);
		loginSessionBean.setTimeout(timeout);
	}

	public void updateLoginSession(LoginSessionBean loginSessionBean) {
		Timeout timeout = loginSessionBean.getTimeout();
		final long sessionId = loginSessionBean.getSessionId();
		if (timeout != null) {
			timeout.cancel();
		}
		Timeout newTimeout = globalTimer.newTimeout(new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				logger.info("loginSessionExpire,sid={},uid={}", sessionId, loginSessionMap.get(sessionId).getOpenId());
				loginSessionMap.remove(sessionId);
				logger.info("after expire size={}", loginSessionMap.size());
			}
		}, LOGIN_SESSION_TIMEOUT);
		loginSessionBean.setTimeout(newTimeout);
	}

	public void removeLoginSession(LoginSessionBean loginSessionBean) {
		Timeout timeout = loginSessionBean.getTimeout();
		long sessionId = loginSessionBean.getSessionId();
		if (timeout != null) {
			timeout.cancel();
		}
		loginSessionMap.remove(sessionId);
	}

	public LoginSessionBean getLoginSessionBean(Long sessionId) {
		return loginSessionMap.get(sessionId);
	}

}
