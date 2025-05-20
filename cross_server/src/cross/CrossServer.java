package cross;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.io.InputStream;
import java.util.Properties;

public class CrossServer {
    private static final Logger logger = LoggerFactory.getLogger(CrossServer.class);
    private static Properties properties = new Properties();
    private static int socketPort;
    private static int lanPort;
    private static int httpPort;
    
    static {
        try {
            InputStream input = CrossServer.class.getClassLoader().getResourceAsStream("cross.properties");
            properties.load(input);
            socketPort = Integer.parseInt(properties.getProperty("socket_port", "8670"));
            lanPort = Integer.parseInt(properties.getProperty("lan_port", "8671"));
            httpPort = Integer.parseInt(properties.getProperty("http_port", "8072"));
        } catch (Exception e) {
            logger.error("Failed to load properties", e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        logger.info("Starting Cross Server...");
        logger.info("Socket Port: " + socketPort);
        logger.info("LAN Port: " + lanPort);
        logger.info("HTTP Port: " + httpPort);
        
        // Start socket server
        startSocketServer();
        
        // Start LAN server
        startLanServer();
        
        // Start HTTP server
        startHttpServer();
    }
    
    private static void startSocketServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     // TODO: Add socket channel handlers
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            // Bind and start to accept incoming connections
            ChannelFuture f = b.bind(socketPort).sync();
            logger.info("Socket Server started on port: " + socketPort);
            
            // Wait until the server socket is closed
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("Socket Server failed to start", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    private static void startLanServer() {
        // TODO: Implement LAN server
        logger.info("LAN Server started on port: " + lanPort);
    }
    
    private static void startHttpServer() {
        // TODO: Implement HTTP server
        logger.info("HTTP Server started on port: " + httpPort);
    }
} 