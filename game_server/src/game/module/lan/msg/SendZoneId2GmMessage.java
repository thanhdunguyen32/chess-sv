package game.module.lan.msg;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendZoneId2GmMessage extends SendToByteMessage {

	public SendZoneId2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10014);
	}

	public void setZoneId(int zoneId) {
		writeInt(zoneId);
	}

}
