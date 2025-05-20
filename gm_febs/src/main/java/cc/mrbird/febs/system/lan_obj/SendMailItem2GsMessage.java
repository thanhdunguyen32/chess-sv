package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendMailItem2GsMessage extends SendToByteMessage {

	public SendMailItem2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 10033);
	}

	public void setMessage(byte addressee, String receiveId, String sender, String title, String content,
			String attach, int validityTime) {
		writeByte(addressee);
		writeString(receiveId);
		writeString(sender);
		writeString(title);
		writeString(content);
		writeString(attach);
		writeInt(validityTime);
	}

}
