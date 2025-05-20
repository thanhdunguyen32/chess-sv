package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.message.RequestByteMessage;

public class Netty4ClientHandler extends SimpleChannelInboundHandler<RequestByteMessage> {

	private static Logger logger = LoggerFactory.getLogger(Netty4ClientHandler.class);

	private IExecutorPool executorPool;

	public Netty4ClientHandler(IExecutorPool executorPool) {
		this.executorPool = executorPool;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestByteMessage msg) throws Exception {
		//executorPool.execute(ctx, msg);
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
