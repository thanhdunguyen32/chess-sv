package game.entity;

import com.google.protobuf.GeneratedMessageV3;

import game.module.user.bean.PlayerBean;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.Timeout;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MySendToMessage;

public class PlayingRole {

	private GamePlayer gamePlayer;

	private PlayerBean playerBean;

	private PlayerCacheStatus playerCacheStatus;

	private Timeout levelGoTimeout;

	private Timeout loginTimeout;

	private Long lastAddStrengthTime;

	private Long sessionId;

	public PlayingRole(GamePlayer gamePlayer, PlayerBean playerBean) {
		this.gamePlayer = gamePlayer;
		this.playerBean = playerBean;
		playerCacheStatus = new PlayerCacheStatus();
	}

	public PlayerBean getPlayerBean() {
		return playerBean;
	}

	public void setPlayerBean(PlayerBean playerBean) {
		this.playerBean = playerBean;
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}
	
	public boolean isChannelActive(){
		return gamePlayer != null && gamePlayer.isChannelActive();
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public int getId() {
		return playerBean.getId();
	}

	public PlayerCacheStatus getPlayerCacheStatus() {
		return playerCacheStatus;
	}

	public void setPlayerCacheStatus(PlayerCacheStatus playerCacheStatus) {
		this.playerCacheStatus = playerCacheStatus;
	}

	public Timeout getLevelGoTimeout() {
		return levelGoTimeout;
	}

	public void setLevelGoTimeout(Timeout levelGoTimeout) {
		this.levelGoTimeout = levelGoTimeout;
	}

	public Timeout getLoginTimeout() {
		return loginTimeout;
	}

	public void setLoginTimeout(Timeout loginTimeout) {
		this.loginTimeout = loginTimeout;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getLastAddStrengthTime() {
		return lastAddStrengthTime;
	}

	public void setLastAddStrengthTime(Long lastAddStrengthTime) {
		this.lastAddStrengthTime = lastAddStrengthTime;
	}
	
	public ByteBufAllocator alloc() {
		return gamePlayer.alloc();
	}
	
	public void writeAndFlush(int msgCode, GeneratedMessageV3 respMsg) {
		gamePlayer.writeAndFlush(msgCode, respMsg);
	}
	
	public void writeAndFlush(int msgCode, int errorCode) {
		gamePlayer.writeAndFlush(msgCode, errorCode);
	}
	
	public void writeAndFlush(MySendToMessage respMsg) {
		gamePlayer.writeAndFlush(respMsg);
	}
	
	public void write(MySendToMessage respMsg) {
		gamePlayer.write(respMsg);
	}
	
	public void write(int msgCode, GeneratedMessageV3 respMsg) {
		gamePlayer.write(msgCode, respMsg);
	}

	public void write(int msgCode, int errorCode) {
		gamePlayer.write(msgCode, errorCode);
	}
	
	public void flush() {
		gamePlayer.flush();
	}

}
