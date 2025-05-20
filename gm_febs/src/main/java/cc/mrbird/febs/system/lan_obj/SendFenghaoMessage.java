package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendFenghaoMessage extends SendToByteMessage {

	public SendFenghaoMessage(ByteBufAllocator alloc) {
		super(alloc, 10025);
	}

	public void setVal(Integer playerId, Long endTimeMili) {
		writeInt(playerId);
		writeLong(endTimeMili);
	}

}
