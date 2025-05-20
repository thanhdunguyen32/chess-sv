package lion.netty4.message;

import com.google.protobuf.GeneratedMessageV3;

public class ResponseProtoCodeMessage extends ResponseProtoMessage {

	private int errorCode;
	
	public ResponseProtoCodeMessage(int messageCode, GeneratedMessageV3 protoMessage, int pErrorCode) {
		super(messageCode, protoMessage);
		this.setErrorCode(pErrorCode);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
