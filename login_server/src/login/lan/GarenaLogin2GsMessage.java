package login.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GarenaLogin2GsMessage extends SendToByteMessage {

	public GarenaLogin2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 10015);
	}

	public void setMessage(Long sessionId, String openId, String accessToken,int serverId) {
		writeLong(sessionId);
		writeString(openId);
		writeString(accessToken);
		writeInt(serverId);
	}

}
