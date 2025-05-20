package lion.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lion.netty4.message.MySendToMessage;

/**
 * bodyLength + msgCode + retCode + ProtoMessage
 * 
 * @author hexuhui
 *
 */
@Sharable
public class WebSocketFrameEncoder extends ChannelOutboundHandlerAdapter {

	@Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		MySendToMessage respMsg = (MySendToMessage) msg;
		ByteBuf rawBytes = respMsg.entireMsg();
		BinaryWebSocketFrame retFrame = new BinaryWebSocketFrame(rawBytes);
		ctx.write(retFrame, promise);
	}

}
