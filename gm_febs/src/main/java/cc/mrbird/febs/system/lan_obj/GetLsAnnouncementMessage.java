package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GetLsAnnouncementMessage extends SendToByteMessage {

	public GetLsAnnouncementMessage(ByteBufAllocator alloc) {
		super(alloc, 10039);
	}

}
