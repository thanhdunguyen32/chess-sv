package lion.netty4.message;

public class ClientInputProtoMessage {

	private int messageCode;

	private byte[] datas;

	private int offset;

	private int length;

	public ClientInputProtoMessage(int messageCode, byte[] array, int offset, int length) {
		this.messageCode = messageCode;
		this.datas = array;
		this.offset = offset;
		this.length = length;
	}

	public ClientInputProtoMessage(int msgCode) {
		this.messageCode = msgCode;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(int messageCode) {
		this.messageCode = messageCode;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
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
