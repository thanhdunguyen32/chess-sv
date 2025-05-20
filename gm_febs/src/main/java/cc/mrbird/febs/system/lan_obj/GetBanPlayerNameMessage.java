package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GetBanPlayerNameMessage extends SendToByteMessage {

	public GetBanPlayerNameMessage(ByteBufAllocator alloc) {
		super(alloc, 10023);
	}

}
