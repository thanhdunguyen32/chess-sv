package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SifubaLogin2GsMessage extends SendToByteMessage {

	public SifubaLogin2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 11005);
	}

	public void setMessage(Long sessionId, String userId,int serverId) {
		writeLong(sessionId);
		writeString(userId);
		writeInt(serverId);
	}

}
