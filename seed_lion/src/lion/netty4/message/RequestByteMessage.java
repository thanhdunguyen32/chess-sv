package lion.netty4.message;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class RequestByteMessage {

	private int msgcode;

	private ByteBuf buffer;

	public RequestByteMessage(int msgcode, ByteBuf byteBuf) {
		buffer = byteBuf;
		this.msgcode = msgcode;
	}

	public int getMsgCode() {
		return msgcode;
	}

	public int readInt() {
		return buffer.readInt();
	}

	public long readLong() {
		return buffer.readLong();
	}

	public byte readByte() {
		return buffer.readByte();
	}

	public short readUnsignedByte() {
		return buffer.readUnsignedByte();
	}

	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}

	public long readUnsignedInt() {
		return buffer.readUnsignedInt();
	}

	public byte[] readByteArray() {
		int utflen = buffer.readUnsignedShort();
		byte[] newBytes = new byte[utflen];
		buffer.readBytes(newBytes);
		return newBytes;
	}

	public void writeByteArray(byte[] content) {
		buffer.writeShort(content.length);
		if (content.length > 0) {
			buffer.writeBytes(content);
		}
	}

	public String readString() {
		int utflen = buffer.readUnsignedShort();
		if (utflen == 0) {
			return "";
		}
		String retStr = buffer.toString(buffer.readerIndex(), utflen, CharsetUtil.UTF_8);
		buffer.readerIndex(buffer.readerIndex() + utflen);
		return retStr;
	}

	public boolean readBool() {
		return buffer.readBoolean();
	}
	
	public void releaseBuffer(){
		buffer.release();
	}

	public void retain() {
		buffer.retain();
	}

}
