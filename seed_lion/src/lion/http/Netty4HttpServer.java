package lion.http;

import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lion.common.NamedThreadFactory;

public class Netty4HttpServer {

	private static Logger logger = LoggerFactory.getLogger(Netty4HttpServer.class);

	private int port;

	private int workerTheads;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private Channel serverChannel;

	public Netty4HttpServer(int p_port) {
		this(p_port, 2);
	}

	public Netty4HttpServer(int p_port, int pWorkThreads) {
		this.port = p_port;
		this.workerTheads = pWorkThreads;
	}

	public void start(Executor threadExecutor, final ChannelReadHttpHandler channelReadHandler) throws Exception {
		if (!SystemUtils.IS_OS_LINUX) {
			if (threadExecutor != null) {
				bossGroup = new NioEventLoopGroup(0x1, threadExecutor); // (1)
				workerGroup = new NioEventLoopGroup(this.workerTheads, threadExecutor);
			} else {
				bossGroup = new NioEventLoopGroup(0x1,new NamedThreadFactory("@+netty_boss")); // (1)
				workerGroup = new NioEventLoopGroup(this.workerTheads,new NamedThreadFactory("@+netty_I/O"));
			}
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
							ch.pipeline().addLast(new HttpResponseEncoder());
							// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpObjectAggregator(512 * 1024));
							// ch.pipeline().addLast(new ChunkedWriteHandler());
							ch.pipeline().addLast(new HttpServerInboundHandler(channelReadHandler));
						}
					});
			serverChannel = b.bind(port).sync().channel();
			logger.warn("HttpServer listening on port:{} successfully!", port);
		} else {
			if (threadExecutor != null) {
				bossGroup = new EpollEventLoopGroup(0x1, threadExecutor); // (1)
				workerGroup = new EpollEventLoopGroup(this.workerTheads, threadExecutor);
			} else {
				bossGroup = new EpollEventLoopGroup(0x1); // (1)
				workerGroup = new EpollEventLoopGroup(this.workerTheads);
			}
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class) // (3)
					.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.SO_REUSEADDR, true)// 重用地址
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
							ch.pipeline().addLast(new HttpResponseEncoder());
							// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
							ch.pipeline().addLast(new HttpRequestDecoder());
							ch.pipeline().addLast(new HttpObjectAggregator(512 * 1024));
							// ch.pipeline().addLast(new ChunkedWriteHandler());
							ch.pipeline().addLast(new HttpServerInboundHandler(channelReadHandler));
						}
					});
			serverChannel = b.bind(port).sync().channel();
			logger.warn("HttpServer listening on port:{} successfully!", port);
		}
	}

	public static void main(String[] args) throws Exception {
		Netty4HttpServer server = new Netty4HttpServer(8000);
		server.start(null, new HttpGetHandler("gm") {
			@Override
			public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request,
					Map<String, String> queryMap) throws Exception {
				logger.info("queryMap={}", queryMap);
				return "success";
			}
		});
//		server.start(null, new HttpPostStrHandler("gm") {
//			@Override
//			public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content)
//					throws Exception {
//				String request_uri = request.uri();
//				logger.info("http request,uri={}", request_uri);
//				if (request_uri.startsWith("/gm/query1")) {
//					String myName = "huijige灰机哥";
//					int xx = 0;
//					int h = 50 / xx;
//					return myName;
//				} else {
//					String myName = "上海观趣guanqu";
//					return myName;
//				}
//			}
//		});
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
