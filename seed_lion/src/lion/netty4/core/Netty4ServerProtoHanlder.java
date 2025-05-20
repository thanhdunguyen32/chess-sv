package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import lion.netty4.message.RequestProtoMessage;

public class Netty4ServerProtoHanlder extends SimpleChannelInboundHandler<RequestProtoMessage> {

	private static Logger logger = LoggerFactory.getLogger(Netty4ServerProtoHanlder.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// logger.info("channel active");
		BaseProtoIoExecutor.initGamePlayer(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		BaseProtoIoExecutor.removeGamePlayer(ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestProtoMessage msg) throws Exception {
		// int msgCode = msg.getMsgCode();
		// logger.info("receive msg,code={}", msgCode);
		BaseProtoIoExecutor.executeIoRequest(ctx.channel(), msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof ReadTimeoutException) {
			logger.info("readTimeout 5min,remove player,addr={}", ctx.channel().remoteAddress());
		} else {
			logger.info("Unexpected exception,msg={},type={}", cause.getMessage(), cause.toString());
		}
		ctx.close();
	}

}
