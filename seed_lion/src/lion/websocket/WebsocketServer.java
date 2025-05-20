package lion.websocket;

import java.io.File;
import java.util.concurrent.ThreadFactory;

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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.util.AttributeKey;
import lion.common.NamedThreadFactory;
import lion.netty4.filter.MsgValidManger;
import lion.netty4.message.GamePlayer;

public class WebsocketServer {

	public static final AttributeKey<Long> KEY_SESSION_ID = AttributeKey
			.valueOf(WebsocketServer.class.getName() + ".SESSION_ID");

	public static final AttributeKey<MsgValidManger> KEY_MSG_VISIT_TIME = AttributeKey
			.valueOf(WebsocketServer.class.getName() + ".MSG_VISIT_TIME");

	public static final AttributeKey<GamePlayer> KEY_GAME_PLAYER = AttributeKey
			.valueOf(WebsocketServer.class.getName() + ".KEY_GAME_PLAYER");

	private static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

	private boolean SSL = true;

	private int port;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private Channel serverChannel;

	public WebsocketServer(int port, boolean isSsl) {
		this.port = port;
		this.SSL = isSsl;
	}

	public void run() throws Exception {
		run(Runtime.getRuntime().availableProcessors() * 3, new NamedThreadFactory("@+netty_boss"),
				new NamedThreadFactory("@+netty_I/O"), "", "");
	}
	

	public void run(int workerThreadCount, ThreadFactory bossThreadFactory, ThreadFactory workerThreadFactory)
			throws Exception {
		run(workerThreadCount, bossThreadFactory, workerThreadFactory, "", "");
	}

	public void run(String certFilePath, String keyFilePath) throws Exception {
		run(Runtime.getRuntime().availableProcessors() * 3, new NamedThreadFactory("@+netty_boss"),
				new NamedThreadFactory("@+netty_I/O"), certFilePath, keyFilePath);
	}

	public void run(int threadCount, ThreadFactory bossThreadFactory, ThreadFactory workerThreadFactory,
			String certFilePath, String keyFilePath) throws Exception {
		// Configure SSL.
		final SslContext sslCtx;
		if (this.SSL) {
			logger.info("websocket run!,pemFile={},keyFile={}", certFilePath, keyFilePath);
			File file1 = new File(certFilePath);
			File file2 = new File(keyFilePath);
			sslCtx = SslContextBuilder.forServer(file1, file2).sslProvider(SslProvider.OPENSSL).build();
		} else {
			sslCtx = null;
		}
		if (!SystemUtils.IS_OS_LINUX) {
			bossGroup = new NioEventLoopGroup(0x1, bossThreadFactory); // (1)
			// io线程会去处理逻辑，因此数量要多一些，默认是2*coreCount
			workerGroup = new NioEventLoopGroup(threadCount, workerThreadFactory);
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(new WebSocketServerInitializer(sslCtx));
			// Bind and start to accept incoming connections.
			serverChannel = b.bind(port).sync().channel();
			logger.warn("websocket server listen on {}:{} successfully!", (this.SSL ? "https" : "http"), port);
		} else {
			bossGroup = new EpollEventLoopGroup(0x1, bossThreadFactory);
			workerGroup = new EpollEventLoopGroup(threadCount, workerThreadFactory);
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(new WebSocketServerInitializer(sslCtx));
			// Bind and start to accept incoming connections.
			serverChannel = b.bind(port).sync().channel();
			logger.warn("websocket server listen on {}:{} successfully!", (this.SSL ? "https" : "http"), port);
		}

	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8653;
		}
		new WebsocketServer(port, true).run();
	}

	public void shutdown() {
		try {
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			serverChannel.close().sync();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

}
