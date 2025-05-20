package lion.socket;

import com.google.protobuf.CodedOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lion.netty4.message.MySendToMessage;

/**
 * bodyLength + msgCode + retCode + ProtoMessage
 * 
 * @author hexuhui
 *
 */
@Sharable
public class SocketFrameEncoder extends MessageToByteEncoder<MySendToMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MySendToMessage respMsg, ByteBuf out) throws Exception {
		
		ByteBuf rawBytes = respMsg.entireMsg();
		int msgBytesLength = rawBytes.readableBytes();
		int headerLen = CodedOutputStream.computeUInt32SizeNoTag(msgBytesLength);
		out.ensureWritable(headerLen + msgBytesLength);
		
		CodedOutputStream outputStream = CodedOutputStream.newInstance(new ByteBufOutputStream(out), headerLen);
		outputStream.writeInt32NoTag(msgBytesLength);
		outputStream.flush();
		out.writeBytes(rawBytes);
	}

}
