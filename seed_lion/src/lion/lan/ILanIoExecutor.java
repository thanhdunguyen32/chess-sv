package lion.lan;

import java.io.IOException;

import io.netty.channel.Channel;
import lion.netty4.message.RequestByteMessage;

public interface ILanIoExecutor {

	void execute(Channel channel, RequestByteMessage msg) throws IOException;

}
