package lion.lan;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

public interface IHttpIoExecutor {

	void execute(Channel channel, HttpResponse msg);

	void execute(Channel channel, HttpContent content);

}
