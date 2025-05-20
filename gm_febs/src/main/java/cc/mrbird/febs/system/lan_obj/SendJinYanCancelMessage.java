package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendJinYanCancelMessage extends SendToByteMessage {

	public SendJinYanCancelMessage(ByteBufAllocator alloc) {
		super(alloc, 10031);
	}

	public void setVal(int playerId) {
		writeInt(playerId);
	}

}
