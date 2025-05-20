package game.module.lan.msg;

import java.util.Map;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToByteMessage;

public class SendBanNames2GmMessage extends SendToByteMessage {

	public SendBanNames2GmMessage(ByteBufAllocator alloc) {
		super(alloc, 10024);
	}

	public void setData(Map<Integer, Long> dataMap) {
		if (dataMap != null && dataMap.size() > 0) {
			writeInt(dataMap.size());
			for (Map.Entry<Integer, Long> aPair : dataMap.entrySet()) {
				writeInt(aPair.getKey());
				writeLong(aPair.getValue());
			}
		} else {
			writeInt(0);
		}
	}

}
