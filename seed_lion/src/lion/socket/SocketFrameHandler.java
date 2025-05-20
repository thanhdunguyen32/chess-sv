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
package lion.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lion.netty4.core.MyIoExecutor;
import lion.netty4.message.MyRequestMessage;

/**
 * Echoes uppercase content of text frames.
 */
public class SocketFrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private static final Logger logger = LoggerFactory.getLogger(SocketFrameHandler.class);

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
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf reqBuf) throws Exception {
		MyRequestMessage reqMsg = new MyRequestMessage(reqBuf);
		int msgCode = reqMsg.getMsgCode();
		logger.info("receive msg,code={}", msgCode);
		ctx.fireChannelRead(reqMsg);
	}
}
