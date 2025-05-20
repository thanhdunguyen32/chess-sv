package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendAnnounce2GmMessage extends SendToByteMessage {

	public SendAnnounce2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10040);
	}

	public void setVal(String content) {
		writeString(content);
	}

}
