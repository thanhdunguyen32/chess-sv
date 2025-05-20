package game.module.activity.bean;

import java.util.Date;

/**
 * 玩家活动信息
 * 
 * @author zhangning
 * 
 * @Date 2015年7月23日 下午3:46:21
 */
public class ActivityPlayer {

	private Integer Id;
	private Integer playerId;
	private Integer progress;
	private Integer type;
	private Date resetTime;

	// 冲级奖励
	private DBActivityLevel dbActivityLevel;

	// 钻石矿井
	private DBActivityMine dbActivityMine;

	// 答题
	private DBActivityAnswer dbActivityAnswer;

	private DBActivityPlayerGet activityPlayerGet;
	
	private DBActivityPlayerHeroLibao heroLibaoBuy;
	
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}

	public DBActivityLevel getDbActivityLevel() {
		return dbActivityLevel;
	}

	public void setDbActivityLevel(DBActivityLevel dbActivityLevel) {
		this.dbActivityLevel = dbActivityLevel;
	}

	public DBActivityMine getDbActivityMine() {
		return dbActivityMine;
	}

	public void setDbActivityMine(DBActivityMine dbActivityMine) {
		this.dbActivityMine = dbActivityMine;
	}

	public DBActivityAnswer getDbActivityAnswer() {
		return dbActivityAnswer;
	}

	public void setDbActivityAnswer(DBActivityAnswer dbActivityAnswer) {
		this.dbActivityAnswer = dbActivityAnswer;
	}

	public DBActivityPlayerGet getActivityPlayerGet() {
		return activityPlayerGet;
	}

	public void setActivityPlayerGet(DBActivityPlayerGet activityPlayerGet) {
		this.activityPlayerGet = activityPlayerGet;
	}

	public DBActivityPlayerHeroLibao getHeroLibaoBuy() {
		return heroLibaoBuy;
	}

	public void setHeroLibaoBuy(DBActivityPlayerHeroLibao heroLibaoBuy) {
		this.heroLibaoBuy = heroLibaoBuy;
	}
	
}
