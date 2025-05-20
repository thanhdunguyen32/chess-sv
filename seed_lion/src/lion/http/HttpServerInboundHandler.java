package lion.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHandler.class);

	private ChannelReadHttpHandler channelReadHandler;

	public HttpServerInboundHandler(ChannelReadHttpHandler channelReadHandler) {
		this.channelReadHandler = channelReadHandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			channelReadHandler.handle(ctx, (FullHttpRequest)msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
		super.channelReadComplete(ctx);
	}

//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		logger.error(cause.getMessage(), cause);
//		super.exceptionCaught(ctx, cause);
//	}

}