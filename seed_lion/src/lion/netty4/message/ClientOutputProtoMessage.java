package lion.netty4.message;

import com.google.protobuf.GeneratedMessage;

public class ClientOutputProtoMessage {

	private int messageCode;

	private GeneratedMessage protoMessage;

	public ClientOutputProtoMessage(int messageCode, GeneratedMessage protoMessage) {
		this.messageCode = messageCode;
		this.protoMessage = protoMessage;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public GeneratedMessage getProtoMessage() {
		return protoMessage;
	}

	public void setProtoMessage(GeneratedMessage protoMessage) {
		this.protoMessage = protoMessage;
	}

}
