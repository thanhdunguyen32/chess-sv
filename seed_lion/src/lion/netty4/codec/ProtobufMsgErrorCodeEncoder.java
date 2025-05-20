package lion.netty4.codec;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lion.netty4.message.ResponseProtoCodeMessage;

/**
 * bodyLength + msgCode + retCode + ProtoMessage
 * 
 * @author hexuhui
 *
 */
@Sharable
public class ProtobufMsgErrorCodeEncoder extends MessageToByteEncoder<ResponseProtoCodeMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseProtoCodeMessage msg, ByteBuf out) throws Exception {
		int msgCode = msg.getMessageCode();
		int errorCode = msg.getErrorCode();
		GeneratedMessageV3 generatedMessage = msg.getProtoMessage();
		int msgBytesLength = 0;
		if (generatedMessage != null) {
			msgBytesLength = generatedMessage.getSerializedSize();
		}
		int bodyLen = CodedOutputStream.computeUInt32SizeNoTag(msgCode)
				+ CodedOutputStream.computeUInt32SizeNoTag(errorCode) + msgBytesLength;

		int headerLen = CodedOutputStream.computeUInt32SizeNoTag(bodyLen);
		out.ensureWritable(headerLen + bodyLen);

		CodedOutputStream outputStream = CodedOutputStream.newInstance(new ByteBufOutputStream(out), headerLen
				+ bodyLen);
		outputStream.writeInt32NoTag(bodyLen);
		outputStream.writeInt32NoTag(msgCode);
		outputStream.writeInt32NoTag(errorCode);
		if (generatedMessage != null) {
			generatedMessage.writeTo(outputStream);
		}
		outputStream.flush();
	}

}
