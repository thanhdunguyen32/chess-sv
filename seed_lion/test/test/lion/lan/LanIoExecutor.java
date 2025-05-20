package test.lion.lan;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import lion.lan.ILanIoExecutor;
import lion.netty4.message.RequestByteMessage;

public class LanIoExecutor implements ILanIoExecutor {

	private static Logger logger = LoggerFactory.getLogger(LanIoExecutor.class);

	public void execute(Channel channel, RequestByteMessage msg) throws IOException {
		int msgCode = msg.getMsgCode();
		switch (msgCode) {
		case 10085:
			long uid = msg.readLong();
			String helloMsg = msg.readString();
			logger.info("receive say hello,uid={},msg={}", uid, helloMsg);
//			SayHelloReturnMessage returnMsg = new SayHelloReturnMessage(channel.alloc(), 10086);
//			returnMsg.setMessage("hxh", "服务器engineer", 25);
//			channel.writeAndFlush(returnMsg);
			break;
		case 10086:
			String name = msg.readString();
			String position = msg.readString();
			int age = msg.readInt();
			logger.info("say hello returns:name={},position={},age={}", name, position, age);
			break;
		default:
			break;
		}
	}

}
