package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendAnnouncementMessage extends SendToByteMessage {

	public SendAnnouncementMessage(ByteBufAllocator alloc) {
		super(alloc, 10041);
	}

	public void setVal(String content) {
		writeString(content);
	}

}
