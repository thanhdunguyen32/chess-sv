package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lion.netty4.message.ClientOutputProtoMessage;

public class GameClientSession {

	private static Logger logger = LoggerFactory.getLogger(GameClientSession.class);

	private Netty4Client netty4Client;

	private Channel channel;

	public GameClientSession(Netty4Client netty4Client) {
		this.netty4Client = netty4Client;
	}

	public void connect(String host, int port) {
		channel = netty4Client.connect(host, port);
	}

	public Channel getChannel() {
		return channel;
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

	public void write(int msgCode, GeneratedMessage respMsg) {
		channel.write(new ClientOutputProtoMessage(msgCode, respMsg));
	}

	public void writeAndFlush(int msgCode, GeneratedMessage respMsg) {
		channel.writeAndFlush(new ClientOutputProtoMessage(msgCode, respMsg));
	}

}
