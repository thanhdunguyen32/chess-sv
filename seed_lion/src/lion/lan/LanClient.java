package lion.lan;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lion.common.NamedThreadFactory;
import lion.netty4.codec.ValueOnlyDecoder;
import lion.netty4.codec.ValueOnlyEncoder;

public class LanClient {

	private static Logger logger = LoggerFactory.getLogger(LanClient.class);

	private Bootstrap bootstrap;

	private EventLoopGroup workerGroup;

	public LanClient(final ILanIoExecutor lanIoExecutor) {
		if (!SystemUtils.IS_OS_LINUX) {
			workerGroup = new NioEventLoopGroup(2, new NamedThreadFactory("@+netty_LAN_CLIENT"));
			bootstrap = new Bootstrap(); // (1)
			bootstrap.group(workerGroup); // (2)
			bootstrap.channel(NioSocketChannel.class); // (3)
		} else {
			workerGroup = new EpollEventLoopGroup(2, new NamedThreadFactory("@+netty_LAN_CLIENT"));
			bootstrap = new Bootstrap(); // (1)
			bootstrap.group(workerGroup); // (2)
			bootstrap.channel(EpollSocketChannel.class); // (3)
		}
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
		bootstrap.option(ChannelOption.SO_LINGER, 0);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ValueOnlyDecoder(), new ValueOnlyEncoder(),
						new Netty4LanHandler(lanIoExecutor));
			}
		});
	}

	public Channel connect(String host, int port) {
		try {
			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.awaitUninterruptibly();

			assert future.isDone();

			if (future.isCancelled()) {
				// Connection attempt cancelled by user
				logger.error("cancelled");
				return null;
			} else if (!future.isSuccess()) {
				logger.error("failed", future.cause());
				return null;
			}

			Channel channel = future.channel();
			logger.info("successfully connect to server!host={},port={}", host, port);
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
