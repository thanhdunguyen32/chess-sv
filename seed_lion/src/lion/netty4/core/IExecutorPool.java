package lion.netty4.core;

import io.netty.channel.ChannelHandlerContext;
import lion.netty4.message.ClientInputProtoMessage;

public interface IExecutorPool {

	public void execute(ChannelHandlerContext ctx, ClientInputProtoMessage msg);

}
