package lion.netty4.processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.netty4.message.RequestProtoMessage;

public abstract class HttpProcessor {

	public abstract void process(ChannelHandlerContext ctx,FullHttpRequest request, RequestProtoMessage protoMessage) throws Exception;

}
