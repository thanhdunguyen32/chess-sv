package lion.lan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.message.RequestByteMessage;

public class Netty4LanHandler extends SimpleChannelInboundHandler<RequestByteMessage> {

	private static Logger logger = LoggerFactory.getLogger(Netty4LanHandler.class);

	private ILanIoExecutor lanIoExecutor;

	public Netty4LanHandler(ILanIoExecutor lanIoExecutor) {
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

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestByteMessage msg) throws Exception {
		int msgCode = msg.getMsgCode();
		logger.info("receive msg,code={}", msgCode);
		try {
			lanIoExecutor.execute(ctx.channel(), msg);
		} catch (Exception e) {
			logger.error("", e);
		}
		msg.releaseBuffer();
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
