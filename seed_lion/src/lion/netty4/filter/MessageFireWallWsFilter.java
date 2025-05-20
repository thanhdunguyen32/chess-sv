package lion.netty4.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.core.MyIoExecutor;
import lion.netty4.core.SocketProtoServer;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

/**
 * 在这里检查消息的频繁程度。
 * 
 * @author hexuhui
 * 
 */
public class MessageFireWallWsFilter extends SimpleChannelInboundHandler<MyRequestMessage> {

	private static final Logger logger = LoggerFactory.getLogger(MessageFireWallWsFilter.class);

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
	protected void channelRead0(ChannelHandlerContext ctx, MyRequestMessage requestmsg) throws Exception {
		int msgCode = requestmsg.getMsgCode();
		MsgValidManger msgValidManger = ctx.channel().attr(SocketProtoServer.KEY_MSG_VISIT_TIME).get();
		if (msgValidManger == null) {
			logger.error("msg filter fail!session is null");
			return;
		}
		int result = msgValidManger.isValidMsg(msgCode);
		switch (result) {
		case MsgValidManger.OK_Validity:
			MyIoExecutor.executeIoRequest(ctx.channel(), requestmsg);
			break;
		case MsgValidManger.Alert_Validity:
			int errorCount = msgValidManger.getErrorCount(msgCode);
			logger.warn("message is discarded for Frequently! msgCode={},errorTimes={},channelId={}", msgCode, errorCount,
					ctx.channel().hashCode());
			GamePlayer gamePlayer = MyIoExecutor.getGamePlayer(ctx.channel().id());
			if (gamePlayer != null && gamePlayer.isChannelActive()) {
				WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(msgCode + 1, 13);
				gamePlayer.writeAndFlush(respMsg.build(ctx.alloc()));
			}
			ctx.fireChannelReadComplete();
			break;
		case MsgValidManger.Frequently_Validity:
			logger.error("message too Frequently,close channel!：msgCode={} result={}", msgCode, result);
			ctx.channel().close();
			break;
		}
	}
}
