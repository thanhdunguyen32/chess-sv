package lion.netty4.message;

import lion.netty4.processor.HttpProcessor;
import lion.netty4.processor.MsgProcessor;

public interface IGameServer {

	boolean checkIP(String address);

	MsgProcessor getMsgProcessor(int msgCode);
	
	HttpProcessor getHttpProcessor(int msgCode);
	
	void syncExecuteIoRequest(GamePlayer player, RequestByteMessage request);

	void syncExecuteIoRequest(GamePlayer player, RequestProtoMessage request);

	void syncExecuteIoRequest(GamePlayer player, MyRequestMessage requestMsg);

}
