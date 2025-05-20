package lion.netty4.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lion.netty4.codec.ValueOnlyDecoder;
import lion.netty4.codec.ValueOnlyEncoder;
import lion.netty4.filter.MessageFireWallFilter;

public class Netty4ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new ValueOnlyDecoder());
		p.addLast("frameEncoder", new ValueOnlyEncoder());
		p.addLast("fireWallFilter", new MessageFireWallFilter());
		p.addLast("handler", new Netty4ServerHanlder());
	}

}
