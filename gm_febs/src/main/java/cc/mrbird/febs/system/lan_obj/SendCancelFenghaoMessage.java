package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendCancelFenghaoMessage extends SendToByteMessage {

	public SendCancelFenghaoMessage(ByteBufAllocator alloc) {
		super(alloc, 10027);
	}

	public void setVal(Integer playerId) {
		writeInt(playerId);
	}

}
