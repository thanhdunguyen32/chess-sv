package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendKickMessage extends SendToByteMessage {

	public SendKickMessage(ByteBufAllocator alloc) {
		super(alloc, 10035);
	}

	public void setVal(Integer playerId) {
		writeInt(playerId);
	}

}
