package lion.netty4.filter;

import java.util.HashMap;
import java.util.Map;

public class MsgAccessLimitCache {

	private Map<Integer, Integer> msgTimeMap = new HashMap<Integer, Integer>();
	public static MsgAccessLimitCache instance;

	private MsgAccessLimitCache() {
	}

	public static MsgAccessLimitCache getInstance() {
		if (instance == null) {
			instance = new MsgAccessLimitCache();
		}
		return instance;
	}

	public Integer getMsgIntervalTime(int msgCode) {
		return msgTimeMap.get(msgCode);
	}

	public void initMsgTimeTemplate(Map<Integer, Integer> template) {
		msgTimeMap = template;
	}

}
