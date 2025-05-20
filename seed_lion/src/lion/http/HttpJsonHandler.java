package lion.http;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

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
public abstract class HttpJsonHandler extends HttpSyncHandler {
	
	private static Logger logger = LoggerFactory.getLogger(HttpJsonHandler.class);

    public HttpJsonHandler(String name) {
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

    public abstract String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, Object> content) throws Exception;

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
    
    public Map<String, Object> getContent(FullHttpRequest request) {
        String queryString = request.content().toString(CharsetUtil.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
		Map quizAnswer = null;
		try {
			quizAnswer = mapper.readValue(queryString, Map.class);
		} catch (IOException e) {
			logger.error("", e);
		}
		return quizAnswer;
    }

}