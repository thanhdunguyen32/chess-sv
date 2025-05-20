package lion.netty4.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

public class SendToByteMessage {

	private ByteBuf buffer = null;

	private int msgCode = 0;

	private boolean messageFormated = false;

	public SendToByteMessage(ByteBufAllocator alloc, int pMsgCode) {
		buffer = alloc.buffer(64);
		// 保留0-5位
		buffer.writerIndex(5);
		setMsgCode(pMsgCode);
	}

	public ByteBuf entireMsg() {
		if (messageFormated) {
			ByteBuf bufferNew = buffer.duplicate();
			bufferNew.readerIndex(0);
			return bufferNew;
		}
		buffer.markWriterIndex();
		int currentPos = buffer.writerIndex();
		buffer.writerIndex(0);
		// 加入长度信息
		buffer.writeShort(getMsgCode());
		buffer.writeMedium(currentPos);// package size
		buffer.resetWriterIndex();
		messageFormated = true;
		return buffer;
	}
	
	public void writeBoolean(boolean b) {
		buffer.writeBoolean(b);
	}
	
	public void writeInt(int i) {
		buffer.writeInt(i);
	}

	public void writeLong(long i) {
		buffer.writeLong(i);
	}

	public void writeByte(byte i) {
		buffer.writeByte(i);
	}

	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

//	public void writeAsString(String value) throws UTFDataFormatException {
//		int i = value.length();
//		int j = 0;
//		int l = 0;
//		int k;
//		for (int i1 = 0; i1 < i; ++i1) {
//			k = value.charAt(i1);
//			if ((k >= 1) && (k <= 127))
//				++j;
//			else if (k > 2047)
//				j += 3;
//			else
//				j += 2;
//		}
//		if (j > 65535)
//			throw new UTFDataFormatException("encoded string too long: " + j + " bytes");
//		byte[] arrayOfByte = null;
//		arrayOfByte = new byte[j + 2];
//		arrayOfByte[(l++)] = (byte) (j >>> 8 & 0xFF);
//		arrayOfByte[(l++)] = (byte) (j >>> 0 & 0xFF);
//		int i2 = 0;
//		for (i2 = 0; i2 < i; ++i2) {
//			k = value.charAt(i2);
//			if (k < 1)
//				break;
//			if (k > 127)
//				break;
//			arrayOfByte[(l++)] = (byte) k;
//		}
//		while (i2 < i) {
//			k = value.charAt(i2);
//			if ((k >= 1) && (k <= 127)) {
//				arrayOfByte[(l++)] = (byte) k;
//			} else if (k > 2047) {
//				arrayOfByte[(l++)] = (byte) (0xE0 | k >> 12 & 0xF);
//				arrayOfByte[(l++)] = (byte) (0x80 | k >> 6 & 0x3F);
//				arrayOfByte[(l++)] = (byte) (0x80 | k >> 0 & 0x3F);
//			} else {
//				arrayOfByte[(l++)] = (byte) (0xC0 | k >> 6 & 0x1F);
//				arrayOfByte[(l++)] = (byte) (0x80 | k >> 0 & 0x3F);
//			}
//			++i2;
//		}
//		buffer.writeBytes(arrayOfByte, 0, j + 2);
//	}

	public void writeString(String str) {
		if (str == null) {
			buffer.writeShort(0);
			return;
		}
		byte[] byteStr = str.getBytes(CharsetUtil.UTF_8);
		buffer.writeShort(byteStr.length);
		buffer.writeBytes(byteStr);
	}

	public void writeBytes(byte[] bytes) {
		buffer.writeShort(bytes.length);
		if (bytes.length > 0) {
			buffer.writeBytes(bytes);
		}
	}

}
