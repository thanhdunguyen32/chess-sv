package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class Login2GsMessage extends SendToByteMessage {

	public Login2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 10009);
	}

	public void setMessage(Long sessionId, String openId, int serverId) {
		writeLong(sessionId);
		writeString(openId);
		writeInt(serverId);
	}

}
