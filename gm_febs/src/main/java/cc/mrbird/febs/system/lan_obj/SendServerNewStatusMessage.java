package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendServerNewStatusMessage extends SendToByteMessage {

	public SendServerNewStatusMessage(ByteBufAllocator alloc) {
		super(alloc, 10045);
	}

	public void setVal(int serverId, int newStatus) {
		writeInt(serverId);
		writeInt(newStatus);
	}

}
