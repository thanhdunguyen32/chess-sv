package lion.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * Created by shaoh on 2017/5/2.
 */
public class HttpMapUri implements ChannelReadHttpHandler {

	private static Logger logger = LoggerFactory.getLogger(HttpMapUri.class);
	
    private Map<String, HttpMapUriHandler> handlers;

    public HttpMapUri(HttpMapUriHandler... handlers) {
        this.handlers = new HashMap<String, HttpMapUriHandler>();
        for(HttpMapUriHandler handler : handlers) {
        	String[] names = handler.getName();
        	for (String aKeyName : names) {
        		this.handlers.put(aKeyName, handler);
			}
        }
    }
    
    public void addHanlder(HttpMapUriHandler a_handler) {
    	String[] names = a_handler.getName();
    	for (String aKeyName : names) {
    		this.handlers.put(aKeyName, a_handler);
		}
    }

    private HttpMapUriHandler getHttpHandler(String path) {
        if(path == null || path.length() <= 1) {
            return null;
        }
//        int nameStart = 1;
//        int nameEnd = path.indexOf("/", nameStart);
//        String name = null;
//        String subPath = null;
//        if(nameEnd < 0) {
//            name = path.substring(nameStart);
//            subPath = "";
//        } else {
//            name = path.substring(nameStart, nameEnd);
//            subPath = path.substring(nameEnd);
//        }
//        HttpMapUriHandler handler = this.handlers.get(name);
        HttpMapUriHandler handler = this.handlers.get(path);
        if(handler == null) {
            return null;
        }
        return handler;
    }

    public String getOriginalIp(FullHttpRequest request) {
        return request.headers().get("ORIGINAL-IP");
    }

    public String getIp(ChannelHandlerContext ctx) {
        String str = ctx.channel().remoteAddress().toString();
        int index = str.indexOf("/");
        if(index >= 0) {
            str = str.substring(index + 1);
        }
        index = str.indexOf(":");
        if(index >= 0) {
            str = str.substring(0, index);
        }
        return str;
    }

    public Map<String, String> getUrlParams(FullHttpRequest request) throws Exception {
        String strUri = request.uri();
        URI uri = new URI(strUri);
        String path = uri.getPath();


        Map<String, String> params = new HashMap<String, String>();

        //if(questionMarkIndex < 0) {
        //    return params;
        //}
        //
        //String paramsStr = uri.substring(questionMarkIndex + 1);
        //
        //String[] paramStrs = paramsStr.split("&");
        //for(String paramStr : paramStrs) {
        //    int equalMarkIndex = paramStr.indexOf("=");
        //    String key = paramStr.substring(0, equalMarkIndex);
        //    String value = paramStr.substring(equalMarkIndex + 1);
        //    params.put(key, value);
        //}

        return params;
    }

    public String getContent(FullHttpRequest request) {
        return request.content().toString(CharsetUtil.UTF_8);
    }

	@Override
	public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// 解析uri，获取路径
		String uri_str = request.uri();
		logger.info("receive http request:{}", uri_str);
		URI uri = new URI(uri_str);
		String path = uri.getPath();
		HttpMapUriHandler httpHander = this.getHttpHandler(path);
		if (httpHander == null) {
			logger.warn("http hanlder not found!query={}", path);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
			ctx.writeAndFlush(response);
		} else {
			httpHander.handle(ctx, request);
		}
	}
}