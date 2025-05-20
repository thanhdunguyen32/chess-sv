package lion.netty4.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.core.SocketProtoServer;
import lion.netty4.message.RequestByteMessage;

/**
 * 在这里检查消息的频繁程度。
 * 
 * @author hexuhui
 * 
 */
public class MessageFireWallFilter extends SimpleChannelInboundHandler<RequestByteMessage> {

	private static final Logger logger = LoggerFactory.getLogger(MessageFireWallFilter.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.attr(SocketProtoServer.KEY_MSG_VISIT_TIME).set(new MsgValidManger());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.attr(SocketProtoServer.KEY_MSG_VISIT_TIME).remove();
		super.channelInactive(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestByteMessage requestmsg) throws Exception {
		int code = requestmsg.getMsgCode();
		MsgValidManger msgValidManger = ctx.attr(SocketProtoServer.KEY_MSG_VISIT_TIME).get();
		if (msgValidManger == null) {
			logger.error("msg filter fail!session is null");
			return;
		}
		int result = msgValidManger.isValidMsg(code);
		switch (result) {
		case MsgValidManger.OK_Validity:
			ctx.fireChannelRead(requestmsg);
			break;
		case MsgValidManger.Alert_Validity:
			logger.warn("message is discarded for Frequently! code={},times={},channelId={}", new Object[] { code, result, ctx.channel().hashCode() });
			ctx.fireChannelRead(requestmsg);
			break;
		case MsgValidManger.Frequently_Validity:
			logger.error("message too Frequently,close channel!：code={} result={}", code, result);
			ctx.channel().close();
			break;
		}
	}
}
