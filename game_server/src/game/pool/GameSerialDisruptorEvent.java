package game.pool;

import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;

public class GameSerialDisruptorEvent {

	public static final byte DISRUPTOR_EVENT_TYPE_PROTO = 0;

	public static final byte DISRUPTOR_EVENT_TYPE_RUNNABLE = 1;
	
	public static final byte DISRUPTOR_EVENT_TYPE_RAW_IO = 2;
	
	public static final byte DISRUPTOR_EVENT_TYPE_WEBSOCKET_IO = 3;

	private GamePlayer playerSession;
	
	private byte eventType;
	
	private RequestProtoMessage requestProtoMessage;
	
	private RequestByteMessage rawIoMessage;
	
	private Runnable runnableJob;
	
	private MyRequestMessage wsIoMessage;
	
	public GamePlayer getPlayerSession() {
		return playerSession;
	}

	public void setPlayerSession(GamePlayer playerSession) {
		this.playerSession = playerSession;
	}

	public byte getEventType() {
		return eventType;
	}

	public void setEventType(byte eventType) {
		this.eventType = eventType;
	}

	public RequestProtoMessage getRequestProtoMessage() {
		return requestProtoMessage;
	}

	public void setRequestProtoMessage(RequestProtoMessage requestProtoMessage) {
		this.requestProtoMessage = requestProtoMessage;
	}

	public RequestByteMessage getRawIoMessage() {
		return rawIoMessage;
	}

	public void setRawIoMessage(RequestByteMessage rawIoMessage) {
		this.rawIoMessage = rawIoMessage;
	}

	public Runnable getRunnableJob() {
		return runnableJob;
	}

	public void setRunnableJob(Runnable runnableJob) {
		this.runnableJob = runnableJob;
	}

	public MyRequestMessage getWsIoMessage() {
		return wsIoMessage;
	}

	public void setWsIoMessage(MyRequestMessage wsIoMessage) {
		this.wsIoMessage = wsIoMessage;
	}

}
