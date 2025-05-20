package cn.hxh.netty.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.hxh.netty.stream.UnixTime;

public class TimeClientHandler extends ChannelHandlerAdapter {
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		UnixTime m = (UnixTime) msg;
		System.out.println(m);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
