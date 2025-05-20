package game.module.lan.msg;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendRole2LoginMessage extends SendToByteMessage {

	public SendRole2LoginMessage(ByteBufAllocator alloc) {
		super(alloc, 11004);
	}

	public void setData(int playerId, String playerName) {
		writeInt(playerId);
		writeString(playerName);
	}

}
