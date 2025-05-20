package lion.netty4.codec;

import java.util.List;

import com.google.protobuf.CodedInputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lion.common.DataEncryption;

/**
 * @see ProtobufVarint32FrameDecoder
 * @author hexuhui
 * 
 */
public class ProtobufVarint32EncryptDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		final byte[] buf = new byte[5];
		for (int i = 0; i < buf.length; i++) {
			if (!in.isReadable()) {
				in.resetReaderIndex();
				return;
			}

			buf[i] = in.readByte();
			if (buf[i] >= 0) {
				int length = CodedInputStream.newInstance(buf, 0, i + 1).readRawVarint32();
				if (length < 0) {
					throw new CorruptedFrameException("negative length: " + length);
				}

				if (in.readableBytes() < length) {
					in.resetReaderIndex();
					return;
				} else {
					ByteBuf toReadBytes = in.readBytes(length);
					DataEncryption.xorEveryByte(toReadBytes);
					out.add(toReadBytes);
					return;
				}
			}
		}

		// Couldn't find the byte whose MSB is off.
		throw new CorruptedFrameException("length wider than 32-bit");
	}
}