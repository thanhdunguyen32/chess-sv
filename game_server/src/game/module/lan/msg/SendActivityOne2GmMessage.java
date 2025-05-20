package game.module.lan.msg;

import java.util.Date;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendActivityOne2GmMessage extends SendToByteMessage {

	public SendActivityOne2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10020);
	}

	public void setVal(int type, Date startTime, Date endTime, int isOpen, String title, String description,
			byte[] param) {
		writeInt(type);
		writeLong(startTime != null ? startTime.getTime() : 0);
		writeLong(endTime != null ? endTime.getTime() : 0);
		writeInt(isOpen);
		writeString(title);
		writeString(description);
		writeBytes(param == null ? new byte[0] : param);
	}

}
