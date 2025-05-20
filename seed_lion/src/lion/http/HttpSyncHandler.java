package lion.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by shaoh on 2017/5/2.
 */
public abstract class HttpSyncHandler extends HttpMapUriHandler {

	public HttpSyncHandler(String name) {
		super(name);
	}

	@Override
	public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		FullHttpResponse response = this.httpHandle(ctx, request);
		this.httpWrite(ctx, request, response);
	}

	public abstract FullHttpResponse httpHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;

	public abstract void httpWrite(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
			throws Exception;
}
