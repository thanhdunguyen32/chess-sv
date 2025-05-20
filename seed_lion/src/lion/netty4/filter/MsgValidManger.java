package lion.netty4.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgValidManger {
	public final static String MSG_FIRE_WALL = "msgFirewALL";
	public final static int OK_Validity = 0;
	public final static int Alert_Validity = 1;
	public final static int Frequently_Validity = 2;

	private Map<Integer, MsgValid> msgTimeMap = new ConcurrentHashMap<Integer, MsgValid>();

	/**
	 * 返回消息合法性 0合法，1正常非法 ，2异常非法
	 * 
	 * @param code
	 * @return
	 */
	public int isValidMsg(int code) {
		MsgValid mv = msgTimeMap.get(code);
		if (mv == null) {
			addMsgValid(code);
			return 0;
		}
		return mv.isValidMsg();
	}

	public int getErrorCount(int msgCode) {
		int ret = 0;
		MsgValid mv = msgTimeMap.get(msgCode);
		if (mv != null) {
			ret = mv.getErrorTimes();
		}
		return ret;
	}

	private void addMsgValid(int code) {
		Integer time = MsgAccessLimitCache.getInstance().getMsgIntervalTime(code);
		if (time == null) {
			msgTimeMap.put(code, new MsgValid(0));
		} else {
			msgTimeMap.put(code, new MsgValid(time));
		}
	}
}
