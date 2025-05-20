package lion.http;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
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

public class HttpProtoResponseHelper {
	
	private static Logger logger = LoggerFactory.getLogger(HttpProtoResponseHelper.class);
	
	public static void sendResponse(ChannelHandlerContext ctx,FullHttpRequest request, int msgCode,int errorNum) {
		sendResponse(ctx, request, msgCode, errorNum, null);
	}
	
	public static void sendResponse(ChannelHandlerContext ctx,FullHttpRequest request, int msgCode,GeneratedMessageV3 respMsg) {
		sendResponse(ctx, request, msgCode, 0, respMsg);
	}

	public static void sendResponse(ChannelHandlerContext ctx,FullHttpRequest request, int msgCode,int errorNum, GeneratedMessageV3 respMsg) {
		int msgBytesLength = 0;
		if (respMsg != null) {
			msgBytesLength = respMsg.getSerializedSize();
		}
		int bodyLen = CodedOutputStream.computeUInt32SizeNoTag(msgCode) +CodedOutputStream.computeUInt32SizeNoTag(errorNum)+ msgBytesLength;
		int headLen = CodedOutputStream.computeUInt32SizeNoTag(bodyLen);
		int totalLen = bodyLen + headLen;
		
		ByteBuf out = Unpooled.buffer(totalLen);
		CodedOutputStream outputStream = CodedOutputStream.newInstance(new ByteBufOutputStream(out), headLen + bodyLen);
		try {
			outputStream.writeInt32NoTag(bodyLen);
			outputStream.writeInt32NoTag(msgCode);
			outputStream.writeInt32NoTag(errorNum);
			if (respMsg != null) {
				respMsg.writeTo(outputStream);
			}
			outputStream.flush();
		} catch (IOException e) {
			logger.error("",e);
		}
		//
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, out);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if(HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
	}
	
}
