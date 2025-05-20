/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package lion.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lion.netty4.core.MyIoExecutor;
import lion.netty4.message.MyRequestMessage;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MyIoExecutor.initGamePlayer(ctx.channel());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		MyIoExecutor.removeGamePlayer(ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		// ping and pong frames already handled

		if (frame instanceof TextWebSocketFrame) {
			// Send the uppercase string back.
			// String request = ((TextWebSocketFrame) frame).text();
			// logger.info("{} received {}", ctx.channel(), request);
			// ctx.channel().writeAndFlush(new
			// TextWebSocketFrame(request.toUpperCase()));
			String request = ((TextWebSocketFrame) frame).text();
			logger.info("{} received {}", ctx.channel(), request);
			ctx.fireChannelReadComplete();

		} else if (frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
			ByteBuf reqBuf = binaryFrame.content();
			MyRequestMessage reqMsg = new MyRequestMessage(reqBuf);
			int msgCode = reqMsg.getMsgCode();
			logger.info("receive msg,code={}", msgCode);
			ctx.fireChannelRead(reqMsg);
//			MyIoExecutor.executeIoRequest(ctx.channel(), reqMsg);
			
			// CodedInputStream codedInputStream =
			// CodedInputStream.newInstance(new ByteBufInputStream(reqBuf));
			// String str1 = codedInputStream.readString();
			// int roomId = codedInputStream.readInt32();
			// logger.info("st1={},roomid={}", str1, roomId);
			//
			// ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(32);
			// CodedOutputStream headerOut = CodedOutputStream.newInstance(new
			// ByteBufOutputStream(bb), 32);
			// headerOut.writeInt32NoTag(10086);
			// headerOut.writeStringNoTag("灰机哥sldl!@)@ddf7\":.,大蓝");
			// headerOut.flush();
			// BinaryWebSocketFrame retFrame = new BinaryWebSocketFrame(bb);
			// ctx.channel().writeAndFlush(retFrame);
		} else {
			String message = "unsupported frame type: " + frame.getClass().getName();
			throw new UnsupportedOperationException(message);
		}
	}
}
