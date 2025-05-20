package lion.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by shaoh on 2017/5/3.
 */
public interface ChannelReadHttpHandler {

    public void handle(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception;

}