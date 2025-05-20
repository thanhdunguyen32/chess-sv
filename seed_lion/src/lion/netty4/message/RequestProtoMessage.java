package lion.netty4.message;

public class RequestProtoMessage {

	private int msgCode;

	private byte[] array;

	private int offset;

	private int length;

	public RequestProtoMessage(int msgCode, byte[] array, int offset, int length) {
		super();
		this.msgCode = msgCode;
		this.array = array;
		this.offset = offset;
		this.length = length;
	}

	public int getMsgCode() {
		return msgCode;
	}

	public byte[] getArray() {
		return array;
	}

	public void setArray(byte[] array) {
		this.array = array;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
