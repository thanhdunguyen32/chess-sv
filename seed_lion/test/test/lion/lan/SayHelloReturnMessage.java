package test.lion.lan;

import io.netty.buffer.ByteBufAllocator;
import lion.netty4.message.SendToMessage;

public class SayHelloReturnMessage extends SendToMessage {

	public SayHelloReturnMessage(ByteBufAllocator alloc, int pMsgCode) {
		super(alloc, pMsgCode);
	}

	public void setMessage(String name, String position, int age) {
		writeString(name);
		writeString(position);
		writeInt(age);
	}

}
