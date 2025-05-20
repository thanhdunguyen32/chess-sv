package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendSubtitleMessage extends SendToByteMessage {

	public SendSubtitleMessage(ByteBufAllocator alloc) {
		super(alloc, 10037);
	}

	public void setVal(String content, int repeateCount) {
		writeString(content);
		writeInt(repeateCount);
	}

}
