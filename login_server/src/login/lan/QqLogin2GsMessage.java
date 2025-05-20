package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class QqLogin2GsMessage extends SendToByteMessage {

	public QqLogin2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 11007);
	}

	public void setMessage(Long sessionId, String userId,int serverId) {
		writeLong(sessionId);
		writeString(userId);
		writeInt(serverId);
	}

}
