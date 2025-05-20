package game.module.lan.msg;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendActivityList2GmMessage extends SendToByteMessage {

	public SendActivityList2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10018);
	}

}
