package lion.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * Created by shaoh on 2017/5/3.
 */
public abstract class HttpPostStrHandler extends HttpSyncHandler {

    public HttpPostStrHandler(String name) {
        super(name);
    }
    
    @Override
    public FullHttpResponse httpHandle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String res = this.strHttpHandle(ctx, request, this.getContent(request));
        FullHttpResponse response;
        if(res == null) {
            response = this.createFullHttpResponseSimple(HttpResponseStatus.NOT_FOUND);
            return response;
        }

        response = createFullHttpResponseSimple(res);

        if(HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        return response;
    }

    @Override
    public void httpWrite(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) throws Exception {
        ctx.writeAndFlush(response);
    }

    public abstract String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception;

    public FullHttpResponse createFullHttpResponseSimple(HttpResponseStatus status, String content) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(content.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    public FullHttpResponse createFullHttpResponseSimple(String content) throws Exception {
        return createFullHttpResponseSimple(HttpResponseStatus.OK, content);
    }

    public FullHttpResponse createFullHttpResponseSimple(HttpResponseStatus status) throws Exception {
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status);
    }
    
    public String getContent(FullHttpRequest request) {
        return request.content().toString(CharsetUtil.UTF_8);
    }
}