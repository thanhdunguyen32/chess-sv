package lion.netty4.message;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public class MyRequestMessage {

	public static final Logger logger = LoggerFactory.getLogger(MyRequestMessage.class);

	private int msgcode;

	private CodedInputStream codedInputStream;
	
	private int msgLength;

	public MyRequestMessage(ByteBuf byteBuf) throws IOException {
		msgLength = byteBuf.readableBytes();
		codedInputStream = CodedInputStream.newInstance(new ByteBufInputStream(byteBuf));
		this.msgcode = codedInputStream.readInt32();
	}
	
	public int getLength() {
		return msgLength;
	}

	public int getMsgCode() {
		return msgcode;
	}

	public int readInt() throws IOException {
		return codedInputStream.readInt32();
	}

	public long readLong() throws IOException {
		return codedInputStream.readInt64();
	}

	public byte readByte() throws IOException {
		return codedInputStream.readRawByte();
	}

	public float readFloat() throws IOException {
		return codedInputStream.readFloat();
	}

	public byte[] readByteArray() throws IOException {
		int utflen = codedInputStream.readInt32();
		return codedInputStream.readRawBytes(utflen);
	}

    public String readString() throws IOException {
		return codedInputStream.readString();
	}

	public boolean readBool() throws IOException {
		return codedInputStream.readBool();
	}

}
