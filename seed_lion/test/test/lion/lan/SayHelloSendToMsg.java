package test.lion.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToMessage;

public class SayHelloSendToMsg extends SendToMessage {

	public SayHelloSendToMsg(ByteBufAllocator alloc, int pMsgCode) {
		super(alloc, pMsgCode);

	}

	public void setMsg(long uid, String helloMsg) {
		writeLong(uid);
		writeString(helloMsg);
	}

}
