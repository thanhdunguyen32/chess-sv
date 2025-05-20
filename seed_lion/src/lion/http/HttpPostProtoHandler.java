package lion.http;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.message.RequestProtoMessage;

/**
 * Created by shaoh on 2017/5/3.
 */
public class HttpPostProtoHandler extends HttpMapUriHandler {

    public HttpPostProtoHandler(String name) {
        super(name);
    }

    public FullHttpResponse createFullHttpResponseSimple(HttpResponseStatus status) throws Exception {
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status);
    }
    
    public RequestProtoMessage getContent(FullHttpRequest request) throws IOException {
    	ByteBuf byteData = request.content();
    	int msgLength = readRawVarint32(byteData);
    	if(msgLength > 0) {
    		int msgCode = readRawVarint32(byteData);
    		byte[] array;
    		int offset;
    		int length = byteData.readableBytes();
    		if (byteData.hasArray()) {
    			array = byteData.array();
    			offset = byteData.arrayOffset() + byteData.readerIndex();
    		} else {
    			array = new byte[length];
    			byteData.getBytes(byteData.readerIndex(), array, 0, length);
    			offset = 0;
    		}
    		
    		RequestProtoMessage reqMsg = new RequestProtoMessage(msgCode, array, offset, length);
    		return reqMsg;
    	}
    	return null;
    }
    
    private static int readRawVarint32(ByteBuf buffer) {
        if (!buffer.isReadable()) {
            return 0;
        }
        buffer.markReaderIndex();
        byte tmp = buffer.readByte();
        if (tmp >= 0) {
            return tmp;
        } else {
            int result = tmp & 127;
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return 0;
            }
            if ((tmp = buffer.readByte()) >= 0) {
                result |= tmp << 7;
            } else {
                result |= (tmp & 127) << 7;
                if (!buffer.isReadable()) {
                    buffer.resetReaderIndex();
                    return 0;
                }
                if ((tmp = buffer.readByte()) >= 0) {
                    result |= tmp << 14;
                } else {
                    result |= (tmp & 127) << 14;
                    if (!buffer.isReadable()) {
                        buffer.resetReaderIndex();
                        return 0;
                    }
                    if ((tmp = buffer.readByte()) >= 0) {
                        result |= tmp << 21;
                    } else {
                        result |= (tmp & 127) << 21;
                        if (!buffer.isReadable()) {
                            buffer.resetReaderIndex();
                            return 0;
                        }
                        result |= (tmp = buffer.readByte()) << 28;
                        if (tmp < 0) {
                            throw new CorruptedFrameException("malformed varint.");
                        }
                    }
                }
            }
            return result;
        }
    }

	@Override
	public void handle(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
		RequestProtoMessage queryMsg = getContent(httpRequest);
		if (queryMsg == null) {
			FullHttpResponse response = this.createFullHttpResponseSimple(HttpResponseStatus.BAD_REQUEST);
			ctx.writeAndFlush(response);
			return;
		}
		BaseProtoIoExecutor.executeHttpRequest(ctx,httpRequest, queryMsg);
	}
}