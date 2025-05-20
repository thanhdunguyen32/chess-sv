package lion.netty4.message;

import com.google.protobuf.GeneratedMessageV3;

public class ResponseProtoMessage {

	private int messageCode;

	private GeneratedMessageV3 protoMessage;

	public ResponseProtoMessage(int messageCode, GeneratedMessageV3 protoMessage) {
		this.messageCode = messageCode;
		this.protoMessage = protoMessage;
	}

	public ResponseProtoMessage(int msgCode) {
		this.messageCode = msgCode;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public GeneratedMessageV3 getProtoMessage() {
		return protoMessage;
	}

	public void setProtoMessage(GeneratedMessageV3 protoMessage) {
		this.protoMessage = protoMessage;
	}

}
