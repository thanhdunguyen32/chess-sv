package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GetLsServerStatusMessage extends SendToByteMessage {

	public GetLsServerStatusMessage(ByteBufAllocator alloc) {
		super(alloc, 10043);
	}

}
