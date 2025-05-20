package login.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Xiao7PayListManager {

	private static Logger logger = LoggerFactory.getLogger(Xiao7PayListManager.class);

	static class SingletonHolder {
		static Xiao7PayListManager instance = new Xiao7PayListManager();
	}

	public static Xiao7PayListManager getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Long, Xiao7PayEntity> payMap = new ConcurrentHashMap<>();

	public void addPayInfo(Long randOrderId, String openId, int serverId, int productId) {
		payMap.put(randOrderId, new Xiao7PayEntity(randOrderId, openId, serverId, productId));
	}

	public Xiao7PayEntity getPayEntity(Long randOrderId) {
		return payMap.get(randOrderId);
	}

	public void remove(Long randOrderId) {
		payMap.remove(randOrderId);
	}

	public static final class Xiao7PayEntity {
		public Long randOrderId;
		public String openId;
		public int serverId;
		public int productId;

		public Xiao7PayEntity(Long randOrderId, String openId, int serverId, int productId) {
			super();
			this.randOrderId = randOrderId;
			this.openId = openId;
			this.serverId = serverId;
			this.productId = productId;
		}
	}

}
