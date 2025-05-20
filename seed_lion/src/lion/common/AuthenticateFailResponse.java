package lion.common;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class AuthenticateFailResponse extends SendToByteMessage {

	public AuthenticateFailResponse(ByteBufAllocator alloc) {
		super(alloc, 102);
	}

}
