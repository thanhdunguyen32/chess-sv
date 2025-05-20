package login.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuestAccountManager {

	private static Logger logger = LoggerFactory.getLogger(GuestAccountManager.class);

	static class SingletonHolder {
		static GuestAccountManager instance = new GuestAccountManager();
	}

	public static GuestAccountManager getInstance() {
		return SingletonHolder.instance;
	}
	
	
	
}
