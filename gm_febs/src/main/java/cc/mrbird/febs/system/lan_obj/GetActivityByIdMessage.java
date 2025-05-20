package cc.mrbird.febs.system.lan_obj;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class GetActivityByIdMessage extends SendToByteMessage {

	public GetActivityByIdMessage(ByteBufAllocator alloc, int activityId) {
		super(alloc, 10019);
		writeInt(activityId);
	}

}
