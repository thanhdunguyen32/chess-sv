package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GetZoneIdMessage extends SendToByteMessage {

	public GetZoneIdMessage(ByteBufAllocator alloc) {
		super(alloc, 10013);
	}

}
