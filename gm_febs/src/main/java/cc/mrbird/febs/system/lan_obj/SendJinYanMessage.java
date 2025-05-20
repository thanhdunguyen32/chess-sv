package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendJinYanMessage extends SendToByteMessage {

	public SendJinYanMessage(ByteBufAllocator alloc) {
		super(alloc, 10029);
	}

	public void setVal(int playerId, Long endTimeMili) {
		writeInt(playerId);
		writeLong(endTimeMili);
	}

}
