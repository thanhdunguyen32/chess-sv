package login.bean;

import lion.netty4.message.RequestByteMessage;

public interface IResponseLanMessage {

	void parse(RequestByteMessage msg);
	
}
