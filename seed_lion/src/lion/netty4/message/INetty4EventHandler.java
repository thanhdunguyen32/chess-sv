package lion.netty4.message;


public interface INetty4EventHandler {

	public void channelInactive(GamePlayer gamePlayer);

	public void readTimeout(GamePlayer gamePlayer);

}
