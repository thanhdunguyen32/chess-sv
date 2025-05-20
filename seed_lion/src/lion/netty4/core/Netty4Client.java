package lion.netty4.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lion.netty4.codec.ProtobufClientDecoder;
import lion.netty4.codec.ProtobufClientEncoder;

public class Netty4Client {

	private static Logger logger = LoggerFactory.getLogger(Netty4Client.class);

	private Bootstrap bootstrap;

	private EventLoopGroup workerGroup;

	public Netty4Client(final IExecutorPool executorPool) {
		workerGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap(); // (1)
		bootstrap.group(workerGroup); // (2)
		bootstrap.channel(NioSocketChannel.class); // (3)
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		bootstrap.option(ChannelOption.SO_LINGER, 0);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				// ch.pipeline().addLast(new ValueOnlyDecoder(), new ValueOnlyEncoder(), new
				// Netty4ClientHandler());
				ch.pipeline().addLast(new ProtobufVarint32FrameDecoder(), new ProtobufClientDecoder(),
						new ProtobufClientEncoder(), new Netty4ClientProtoHandler(executorPool));
			}
		});
	}

	public Channel connect(String host, int port) {
		try {
			ChannelFuture future = bootstrap.connect(host, port).sync();
			if (future.isCancelled()) {
				logger.error("cancelled");
				return null;
			} else if (!future.isSuccess()) {
				logger.error("failed");
				return null;
			}
			Channel channel = future.channel();
			return channel;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() {
		workerGroup.shutdownGracefully();
	}

}
