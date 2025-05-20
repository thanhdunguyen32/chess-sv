package game.module.lan.msg;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendLevelup2BusMessage extends SendToByteMessage {

	public SendLevelup2BusMessage(ByteBufAllocator alloc) {
		super(alloc, 20003);
	}

	public void setMessage(String garenaOpenId, int zoneId, int level) {
		writeString(garenaOpenId);
		writeInt(zoneId);
		writeInt(level);
	}

}
