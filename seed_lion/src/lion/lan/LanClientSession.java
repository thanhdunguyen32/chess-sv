package lion.lan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lion.netty4.message.SendToByteMessage;

public class LanClientSession {

	private static Logger logger = LoggerFactory.getLogger(LanClientSession.class);

	private LanClient netty4Client;

	private Channel channel;

	private boolean isConnected;

	public LanClientSession(LanClient netty4Client) {
		this.netty4Client = netty4Client;
		isConnected = false;
	}

	public LanClient getNetty4Client() {
		return netty4Client;
	}

	public void connect(String host, int port) {
		channel = netty4Client.connect(host, port);
		isConnected = true;
	}

	public void close() {
		try {
			if (channel != null) {
				channel.close().sync();
			}
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	public ByteBufAllocator alloc() {
		return channel.alloc();
	}

	public ChannelFuture write(SendToByteMessage msg) {
		return channel.write(msg);
	}
	
	public Channel flush(){
		return channel.flush();
	}

	public ChannelFuture writeAndFlush(SendToByteMessage msg) {
		return channel.writeAndFlush(msg);
	}

	public boolean isConnected() {
		return isConnected && channel != null && channel.isActive();
	}

}
