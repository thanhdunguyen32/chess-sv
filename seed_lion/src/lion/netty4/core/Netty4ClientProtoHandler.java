package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.message.ClientInputProtoMessage;

public class Netty4ClientProtoHandler extends SimpleChannelInboundHandler<ClientInputProtoMessage> {

	private static Logger logger = LoggerFactory.getLogger(Netty4ClientProtoHandler.class);

	private IExecutorPool executorPool;

	public Netty4ClientProtoHandler(IExecutorPool executorPool) {
		this.executorPool = executorPool;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ClientInputProtoMessage msg)
			throws Exception {
		executorPool.execute(ctx, msg);
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
