package lion.socket;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import lion.common.NamedThreadFactory;
import lion.netty4.filter.MsgValidManger;
import lion.netty4.message.GamePlayer;

public class SocketServer {

	public static final AttributeKey<Long> KEY_SESSION_ID = AttributeKey
			.valueOf(SocketServer.class.getName() + ".SESSION_ID");
	
	public static final AttributeKey<MsgValidManger> KEY_MSG_VISIT_TIME = AttributeKey
			.valueOf(SocketServer.class.getName() + ".MSG_VISIT_TIME");

	public static final AttributeKey<GamePlayer> KEY_GAME_PLAYER = AttributeKey
			.valueOf(SocketServer.class.getName() + ".KEY_GAME_PLAYER");

	private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

	private int port;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private Channel serverChannel;

	public SocketServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		if (!SystemUtils.IS_OS_LINUX) {
			bossGroup = new NioEventLoopGroup(0x1, new NamedThreadFactory("@+netty_boss")); // (1)
			// io线程会去处理逻辑，因此数量要多一些，默认是2*coreCount
			workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 3,
					new NamedThreadFactory("@+netty_I/O"));
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new SocketServerProtoInitializer())
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true);
			// Bind and start to accept incoming connections.
			serverChannel = b.bind(port).sync().channel();
			logger.warn("socket server listen on port:{} successfully!", port);
		} else {
			bossGroup = new EpollEventLoopGroup(0x1, new NamedThreadFactory("@+netty_boss"));
			workerGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 3,
					new NamedThreadFactory("@+netty_I/O"));
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
					.childHandler(new SocketServerProtoInitializer())
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true);
			// Bind and start to accept incoming connections.
			serverChannel = b.bind(port).sync().channel();
			logger.warn("socket server listen on port:{} successfully!", port);
		}

	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8653;
		}
		new SocketServer(port).run();
	}

	public void shutdown() {
		try {
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			serverChannel.close().sync();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

}
