package lion.netty4.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.core.SocketProtoServer;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.message.ResponseProtoCodeMessage;

/**
 * 在这里检查消息的频繁程度。
 * 
 * @author hexuhui
 * 
 */
public class MessageFireWallProtoFilter extends SimpleChannelInboundHandler<RequestProtoMessage> {

	private static final Logger logger = LoggerFactory.getLogger(MessageFireWallProtoFilter.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(SocketProtoServer.KEY_MSG_VISIT_TIME).set(new MsgValidManger());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(SocketProtoServer.KEY_MSG_VISIT_TIME).set(null);
		super.channelInactive(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestProtoMessage requestmsg) throws Exception {
		int code = requestmsg.getMsgCode();
		MsgValidManger msgValidManger = ctx.channel().attr(SocketProtoServer.KEY_MSG_VISIT_TIME).get();
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
			int errorCount = msgValidManger.getErrorCount(code);
			logger.warn("message is discarded for Frequently! code={},errorTimes={},channelId={}", code, errorCount,
					ctx.channel().hashCode());
			ctx.writeAndFlush(new ResponseProtoCodeMessage(code + 1, null, 204));//发送消息过于频繁提示
			ctx.fireChannelReadComplete();
			break;
		case MsgValidManger.Frequently_Validity:
			logger.error("message too Frequently,close channel!：code={} result={}", code, result);
			ctx.channel().close();
			break;
		}
	}
}
