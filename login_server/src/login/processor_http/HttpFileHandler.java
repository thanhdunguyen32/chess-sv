package login.processor_http;

import com.google.common.io.Files;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lion.http.HttpMapUriHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author HeXuhui
 * @date
 */
public class HttpFileHandler extends HttpMapUriHandler {

    private static Logger logger = LoggerFactory.getLogger(HttpFileHandler.class);

    public HttpFileHandler(String... name) {
        super(name);
    }

    private transient String qqGroupStr = null;
    private transient String getdirtyStr = null;
    private transient String noticeStr = null;

    static HttpFileHandler _instance;

    public static HttpFileHandler getInstance() {
        return _instance;
    }

    public static HttpFileHandler initInstance(String... name) {
        _instance = new HttpFileHandler(name);
        return _instance;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        String uriStr = httpRequest.uri();
        if (StringUtils.indexOf(uriStr, "version/notice/qqGroup.json") > -1) {
            //get qq group
            if (qqGroupStr == null) {
                qqGroupStr = Files.asCharSource(new File("file/qqGroup.json"), CharsetUtil.UTF_8).read();
            }
            sendHttpResponse(ctx, httpRequest, qqGroupStr);
        } else if (StringUtils.indexOf(uriStr, "glory_account/account/getdirty.do") > -1) {
            if (getdirtyStr == null) {
                getdirtyStr = Files.asCharSource(new File("file/getdirty.json"), CharsetUtil.UTF_8).read();
            }
            sendHttpResponse(ctx, httpRequest, getdirtyStr);
        } else if (StringUtils.indexOf(uriStr, "getnotice.do") > -1) {
            if (noticeStr == null) {
                noticeStr = Files.asCharSource(new File("file/notice.json"), CharsetUtil.UTF_8).read();
            }
            sendHttpResponse(ctx, httpRequest, noticeStr);
        } else {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.NOT_FOUND);
            ctx.writeAndFlush(response);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest httpRequest, String responseStr) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(responseStr.getBytes(CharsetUtil.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (HttpUtil.isKeepAlive(httpRequest)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }

    public String getQqGroupStr() {
        return qqGroupStr;
    }

    public void setQqGroupStr(String qqGroupStr) {
        this.qqGroupStr = qqGroupStr;
    }

    public String getGetdirtyStr() {
        return getdirtyStr;
    }

    public void setGetdirtyStr(String getdirtyStr) {
        this.getdirtyStr = getdirtyStr;
    }

    public String getNoticeStr() {
        return noticeStr;
    }

    public void setNoticeStr(String noticeStr) {
        this.noticeStr = noticeStr;
    }

    public void updateNoticeJson(String announcementContent) {
        try {
            Files.asCharSink(new File("file/notice.json"), CharsetUtil.UTF_8).write(announcementContent);
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}