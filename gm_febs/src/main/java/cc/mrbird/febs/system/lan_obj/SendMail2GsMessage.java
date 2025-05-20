package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendMail2GsMessage extends SendToByteMessage {

	public SendMail2GsMessage(ByteBufAllocator alloc) {
		super(alloc, 10011);
	}

	public void setMessage(int mailTemplateId) {
		writeInt(mailTemplateId);
	}

}
