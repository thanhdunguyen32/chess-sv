package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendServerStatus2GmMessage extends SendToByteMessage {

	public SendServerStatus2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10044);
	}

	public void setVal(int serverStatus) {
		writeInt(serverStatus);
	}

}
