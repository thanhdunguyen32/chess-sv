package lion.netty4.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import lion.netty4.message.RequestByteMessage;

/**
 * 消息格式：unsigned short=消息长度,表示整个消息包的长度 byte[]=消息内容
 * 
 * @author Administrator
 * 
 */
public class ValueOnlyDecoder extends ByteToMessageDecoder {

	public static final int MIN_MSG_LENGTH = 5;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) {
		ByteBuf leBuf = in;
		if (leBuf.readableBytes() < MIN_MSG_LENGTH) {
			return;
		}
		leBuf.markReaderIndex();
		int msgCode = leBuf.readUnsignedShort();
		int msgLength = leBuf.readUnsignedMedium();
		int bodyLength = msgLength - 5;
		if (leBuf.readableBytes() < bodyLength) {
			leBuf.resetReaderIndex();
			return;
		}
		ByteBuf msgContent = leBuf.readBytes(bodyLength);
//		msgContent.retain();
		out.add(new RequestByteMessage(msgCode,msgContent));
	}
	
	public static void main(String[] args) {
		String myname = "打客服老公少了";
		System.out.println(myname.getBytes(CharsetUtil.UTF_8).length);
	}

}
