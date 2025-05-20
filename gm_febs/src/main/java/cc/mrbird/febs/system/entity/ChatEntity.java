package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ChatEntity {

	private int playerId;
	
	private String playerName;
	
	private String content;
	
	private Date chatTime;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getChatTime() {
		return chatTime;
	}

	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}
	
}
