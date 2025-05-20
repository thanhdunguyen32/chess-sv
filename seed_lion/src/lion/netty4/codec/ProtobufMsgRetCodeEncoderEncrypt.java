package lion.netty4.codec;

import java.io.ByteArrayOutputStream;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lion.common.DataEncryption;
import lion.netty4.message.ResponseProtoMessage;

/**
 * bodyLength + msgCode + retCode + ProtoMessage
 * 
 * @author hexuhui
 *
 */
@Sharable
public class ProtobufMsgRetCodeEncoderEncrypt extends MessageToByteEncoder<ResponseProtoMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseProtoMessage msg, ByteBuf out) throws Exception {
		int msgCode = msg.getMessageCode();
		GeneratedMessageV3 generatedMessage = msg.getProtoMessage();
		int msgBytesLength = 0;
		if (generatedMessage != null) {
			msgBytesLength = generatedMessage.getSerializedSize();
		}
		int bodyLen = CodedOutputStream.computeUInt32SizeNoTag(msgCode)
				+ msgBytesLength;

		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(bodyLen);
		CodedOutputStream outputStream = CodedOutputStream.newInstance(arrayOutputStream, bodyLen);
		outputStream.writeInt32NoTag(msgCode);
		if (generatedMessage != null) {
			generatedMessage.writeTo(outputStream);
		}
		outputStream.flush();
		byte[] rawBytes = arrayOutputStream.toByteArray();
		// encode
		byte[] encryptedBytes = DataEncryption.encrypt((byte) 55, rawBytes, 0, rawBytes.length);
		bodyLen = encryptedBytes.length;
		int headerLen = CodedOutputStream.computeUInt32SizeNoTag(bodyLen);
		out.ensureWritable(headerLen + bodyLen);
		
		outputStream = CodedOutputStream.newInstance(new ByteBufOutputStream(out), headerLen + bodyLen);
		outputStream.writeInt32NoTag(bodyLen);
		outputStream.writeRawBytes(encryptedBytes);
		outputStream.flush();
	}

}
