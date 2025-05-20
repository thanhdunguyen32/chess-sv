package login.concurrent;

import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GsSyncRequest {

	private static Logger logger = LoggerFactory.getLogger(GsSyncRequest.class);

	static class SingletonHolder {
		static GsSyncRequest instance = new GsSyncRequest();
	}

	public static GsSyncRequest getInstance() {
		return SingletonHolder.instance;
	}

	private Lock lock = new ReentrantLock();

	private Condition isSending = lock.newCondition();

	private RequestByteMessage retMsg;

	public void doSend() {
		if (retMsg != null) {
			return;
		}
		lock.lock();
		try {
			// logger.info("doSend#1");
			// retMsg = null;
			isSending.await(5, TimeUnit.SECONDS);
			// logger.info("doSend#2");
		} catch (InterruptedException e) {
			logger.error("", e);
		} finally {
			lock.unlock();
		}
	}

	public void doReceive(RequestByteMessage msg) {
		// logger.info("doReceive#1");
		lock.lock();
		try {
			// logger.info("doReceive#2");
			retMsg = msg;
			isSending.signal();
		} finally {
			lock.unlock();
		}
	}

	public RequestByteMessage getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(RequestByteMessage retMsg) {
		this.retMsg = retMsg;
	}

}
