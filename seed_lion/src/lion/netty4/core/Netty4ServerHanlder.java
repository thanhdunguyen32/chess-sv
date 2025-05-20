package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.message.RequestByteMessage;

public class Netty4ServerHanlder extends SimpleChannelInboundHandler<RequestByteMessage> {

	private static Logger logger = LoggerFactory.getLogger(Netty4ServerHanlder.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		BaseIoExecutor.initGamePlayer(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		BaseIoExecutor.removeGamePlayer(ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestByteMessage msg) throws Exception {
		int msgCode = msg.getMsgCode();
		logger.info("receive msg,code={}", msgCode);
		BaseIoExecutor.executeIoRequest(ctx.channel(), msg);
	}

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

}
