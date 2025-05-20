package lion.lan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(HttpClientInboundHandler.class);

	private IHttpIoExecutor lanIoExecutor;

	public HttpClientInboundHandler(IHttpIoExecutor lanIoExecutor) {
		this.lanIoExecutor = lanIoExecutor;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelActive,remote_ip={}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.error("lost connection!");
	}

	// @Override
	// protected void channelRead(ChannelHandlerContext ctx, Object msg) throws
	// Exception {
	// int msgCode = msg.getMsgCode();
	// logger.info("receive msg,code={}", msgCode);
	// lanIoExecutor.execute(ctx.channel(), msg);
	// }

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;
			lanIoExecutor.execute(ctx.channel(), response);
			// System.out.println("CONTENT_TYPE:" +
			// response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			lanIoExecutor.execute(ctx.channel(), content);
			 ByteBuf buf = content.content();
			// System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
			 buf.release();
		}

	}

}
