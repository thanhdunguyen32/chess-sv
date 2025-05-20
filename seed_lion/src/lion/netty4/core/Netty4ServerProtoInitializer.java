package lion.netty4.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lion.netty4.codec.ProtobufMsgCodeDecoder;
import lion.netty4.codec.ProtobufMsgErrorCodeEncoder;
import lion.netty4.filter.MessageFireWallProtoFilter;

public class Netty4ServerProtoInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
//		p.addLast("readTimeoutHandler", new ReadTimeoutHandler(900));//15分钟没收到断socket
		p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		// p.addLast("frameDecoder", new ProtobufVarint32EncryptDecoder());
		p.addLast("protobufDecoder", new ProtobufMsgCodeDecoder());
		// p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrependerEncrypt());
		p.addLast("protobufEncoder", new ProtobufMsgErrorCodeEncoder());
		p.addLast("fireWallFilter", new MessageFireWallProtoFilter());
		p.addLast("handler", new Netty4ServerProtoHanlder());
	}

}
