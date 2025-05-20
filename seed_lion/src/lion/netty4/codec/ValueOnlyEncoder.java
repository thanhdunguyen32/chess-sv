package lion.netty4.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lion.netty4.message.SendToByteMessage;

public class ValueOnlyEncoder extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		SendToByteMessage respMsg = (SendToByteMessage) msg;
		ctx.writeAndFlush(respMsg.entireMsg(), promise);
	}

}
