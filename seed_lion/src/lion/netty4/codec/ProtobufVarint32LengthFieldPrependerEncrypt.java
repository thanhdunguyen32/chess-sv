package lion.netty4.codec;

import com.google.protobuf.CodedOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lion.common.DataEncryption;

/**
 * @see ProtobufVarint32LengthFieldPrepender
 * @author hexuhui
 * 
 */
public class ProtobufVarint32LengthFieldPrependerEncrypt extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		int bodyLen = msg.readableBytes();
		//encrypt
		DataEncryption.xorEveryByte(msg);
		//write head length
		int headerLen = CodedOutputStream.computeUInt32SizeNoTag(bodyLen);
		out.ensureWritable(headerLen + bodyLen);

		CodedOutputStream headerOut = CodedOutputStream.newInstance(new ByteBufOutputStream(out));
		headerOut.writeInt32NoTag(bodyLen);
		headerOut.flush();

		out.writeBytes(msg, msg.readerIndex(), bodyLen);
	}
}
