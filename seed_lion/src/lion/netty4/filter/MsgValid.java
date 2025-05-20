package lion.netty4.filter;
/**
 * 
 * @author serv_dev
 *
 */
public class MsgValid {
	/**单位秒**/
	public final static int feifaCountMax = 100; //
	private int msgTime;//毫秒为单位
	private long receivedMsgTime;
	/**用户非法请求次数*/
	private int count = 0; // 
	public int countMax;

	public MsgValid(int msgTime) {
		this.receivedMsgTime = System.currentTimeMillis();
		this.msgTime = msgTime;
		this.count = 0;
		this.countMax = feifaCountMax * msgTime / 1000;
		if (this.countMax < 50) {
			countMax = 50;
		}
	}

	/**
	 * 返回消息合法性 0合法，1正常非法 ，2异常非法
	 * 
	 * @return
	 */
	public int isValidMsg() {
		if (msgTime < 1) {
			return MsgValidManger.OK_Validity;
		}
		long now = System.currentTimeMillis();
		if (now - receivedMsgTime > msgTime) {
			receivedMsgTime = now;
			this.count = 0;
			return MsgValidManger.OK_Validity;
		}
		count++;
		if (countMax <= count) {
			return MsgValidManger.Frequently_Validity;
		}
		return MsgValidManger.Alert_Validity;
	}

	public int getErrorTimes() {
		return count;
	}
}
