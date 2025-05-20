package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class Payment2GsMessage extends SendToByteMessage {

	public Payment2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 11001);
	}

	public void setMessage(String userId, String orderid, int money, int time, String pid, int serverid) {
		writeString(userId);
		writeString(orderid);
		writeInt(money);
		writeInt(time);
		writeString(pid);
		writeInt(serverid);
	}

}
