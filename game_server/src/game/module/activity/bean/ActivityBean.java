package game.module.activity.bean;

import java.util.Date;

public class ActivityBean {

	private Integer type;

	private Date startTime;

	private Date endTime;

	private String title;

	private String description;

	private Integer isOpen;

	private DBActivityCommon activityCommon;
	/**
	 * 充值奖励
	 */
	private DBActivityCommon rechargeAward;

	private DBActivityConsume activityConsume;

	private DBActivityLogin activityLogin;

	/**
	 * 冲级奖励
	 */
	private DBActivityLevelAward dbActivityLevelAward;

	/**
	 * 钻石矿井
	 */
	private DBActivityMineAward dbActivityMineAward;

	/**
	 * 奖励加倍
	 */
	private DBActivityAwardDouble dbActivityAwardDouble;

	/**
	 * 答题
	 */
	private DBActivityAnswerAward dbActivityAnswerAward;

	/**
	 * 折扣季
	 */
	private DBActivitySale dbActivitySale;
	private DBActivityKaiFu dbActivityKaiFu;
	private DBActivityChengZhangJiJin dbActivityChengZhangJiJin;
	private DBActivityQuanMinFuLi activityQuanMinFuLi;
	private DBActivityVipLiBao activityVipLiBao;
	private DBActivityDailyActive activityDailyActive;
	private DBActivity3DayFirstRecharge activity3DayFirstRecharge;
	private DBActivityHeroLibao activityHeroLibao;
	private DBActivityLotteryWheel activityLotteryWheel;
	private DBActivityFirstRechgeDouble activityFirstRechargeDouble;
	private DBActivityQiZhenYiBao activityQiZhenYiBao;
	
	private DBActivityMeiRiShouChong activityMeiRiShouChong;
	private DBActivityDanBiChongZhi activityDanBiChongZhi;
	private DBActivityLianXuChongZhi activityLianXuChongZhi;
	private DBActivityShiLianJiangLi activityShiLianJiangLi;
	private DBActivityChongZhiBang activityChongZhiBang;
	private DBActivityXiaoFeiBang activityXiaoFeiBang;
	private DBActivityCrossBoss activityCrossBoss;
	private DBActivityTTHL activityTTHL;
	private DBActivityQCJJ activityQCJJ;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public DBActivityCommon getRechargeAward() {
		return rechargeAward;
	}

	public void setRechargeAward(DBActivityCommon rechargeAward) {
		this.rechargeAward = rechargeAward;
	}

	public DBActivityLevelAward getDbActivityLevelAward() {
		return dbActivityLevelAward;
	}

	public void setDbActivityLevelAward(DBActivityLevelAward dbActivityLevelAward) {
		this.dbActivityLevelAward = dbActivityLevelAward;
	}

	public DBActivityMineAward getDbActivityMineAward() {
		return dbActivityMineAward;
	}

	public void setDbActivityMineAward(DBActivityMineAward dbActivityMineAward) {
		this.dbActivityMineAward = dbActivityMineAward;
	}

	public DBActivityAwardDouble getDbActivityAwardDouble() {
		return dbActivityAwardDouble;
	}

	public void setDbActivityAwardDouble(DBActivityAwardDouble dbActivityAwardDouble) {
		this.dbActivityAwardDouble = dbActivityAwardDouble;
	}

	public DBActivityConsume getActivityConsume() {
		return activityConsume;
	}

	public void setActivityConsume(DBActivityConsume activityConsume) {
		this.activityConsume = activityConsume;
	}

	public DBActivityLogin getActivityLogin() {
		return activityLogin;
	}

	public void setActivityLogin(DBActivityLogin activityLogin) {
		this.activityLogin = activityLogin;
	}

	public DBActivityAnswerAward getDbActivityAnswerAward() {
		return dbActivityAnswerAward;
	}

	public void setDbActivityAnswerAward(DBActivityAnswerAward dbActivityAnswerAward) {
		this.dbActivityAnswerAward = dbActivityAnswerAward;
	}

	public DBActivitySale getDbActivitySale() {
		return dbActivitySale;
	}

	public void setDbActivitySale(DBActivitySale dbActivitySale) {
		this.dbActivitySale = dbActivitySale;
	}

	public DBActivityChengZhangJiJin getDbActivityChengZhangJiJin() {
		return dbActivityChengZhangJiJin;
	}

	public void setDbActivityChengZhangJiJin(DBActivityChengZhangJiJin dbActivityChengZhangJiJin) {
		this.dbActivityChengZhangJiJin = dbActivityChengZhangJiJin;
	}

	public DBActivityQuanMinFuLi getActivityQuanMinFuLi() {
		return activityQuanMinFuLi;
	}

	public void setActivityQuanMinFuLi(DBActivityQuanMinFuLi activityQuanMinFuLi) {
		this.activityQuanMinFuLi = activityQuanMinFuLi;
	}

	public DBActivityVipLiBao getActivityVipLiBao() {
		return activityVipLiBao;
	}

	public void setActivityVipLiBao(DBActivityVipLiBao activityVipLiBao) {
		this.activityVipLiBao = activityVipLiBao;
	}

	public DBActivityDailyActive getActivityDailyActive() {
		return activityDailyActive;
	}

	public void setActivityDailyActive(DBActivityDailyActive activityDailyActive) {
		this.activityDailyActive = activityDailyActive;
	}

	public DBActivityKaiFu getDbActivityKaiFu() {
		return dbActivityKaiFu;
	}

	public void setDbActivityKaiFu(DBActivityKaiFu dbActivityKaiFu) {
		this.dbActivityKaiFu = dbActivityKaiFu;
	}

	public DBActivity3DayFirstRecharge getActivity3DayFirstRecharge() {
		return activity3DayFirstRecharge;
	}

	public void setActivity3DayFirstRecharge(DBActivity3DayFirstRecharge activity3DayFirstRecharge) {
		this.activity3DayFirstRecharge = activity3DayFirstRecharge;
	}

	public DBActivityHeroLibao getActivityHeroLibao() {
		return activityHeroLibao;
	}

	public void setActivityHeroLibao(DBActivityHeroLibao activityHeroLibao) {
		this.activityHeroLibao = activityHeroLibao;
	}

	public DBActivityLotteryWheel getActivityLotteryWheel() {
		return activityLotteryWheel;
	}

	public void setActivityLotteryWheel(DBActivityLotteryWheel activityLotteryWheel) {
		this.activityLotteryWheel = activityLotteryWheel;
	}

	public DBActivityFirstRechgeDouble getActivityFirstRechargeDouble() {
		return activityFirstRechargeDouble;
	}

	public void setActivityFirstRechargeDouble(DBActivityFirstRechgeDouble activityFirstRechargeDouble) {
		this.activityFirstRechargeDouble = activityFirstRechargeDouble;
	}

	public DBActivityQiZhenYiBao getActivityQiZhenYiBao() {
		return activityQiZhenYiBao;
	}

	public void setActivityQiZhenYiBao(DBActivityQiZhenYiBao activityQiZhenYiBao) {
		this.activityQiZhenYiBao = activityQiZhenYiBao;
	}

	public DBActivityMeiRiShouChong getActivityMeiRiShouChong() {
		return activityMeiRiShouChong;
	}

	public void setActivityMeiRiShouChong(DBActivityMeiRiShouChong activityMeiRiShouChong) {
		this.activityMeiRiShouChong = activityMeiRiShouChong;
	}

	public DBActivityLianXuChongZhi getActivityLianXuChongZhi() {
		return activityLianXuChongZhi;
	}

	public void setActivityLianXuChongZhi(DBActivityLianXuChongZhi activityLianXuChongZhi) {
		this.activityLianXuChongZhi = activityLianXuChongZhi;
	}

	public DBActivityShiLianJiangLi getActivityShiLianJiangLi() {
		return activityShiLianJiangLi;
	}

	public void setActivityShiLianJiangLi(DBActivityShiLianJiangLi activityShiLianJiangLi) {
		this.activityShiLianJiangLi = activityShiLianJiangLi;
	}

	public DBActivityChongZhiBang getActivityChongZhiBang() {
		return activityChongZhiBang;
	}

	public void setActivityChongZhiBang(DBActivityChongZhiBang activityChongZhiBang) {
		this.activityChongZhiBang = activityChongZhiBang;
	}

	public DBActivityXiaoFeiBang getActivityXiaoFeiBang() {
		return activityXiaoFeiBang;
	}

	public void setActivityXiaoFeiBang(DBActivityXiaoFeiBang activityXiaoFeiBang) {
		this.activityXiaoFeiBang = activityXiaoFeiBang;
	}

	public DBActivityDanBiChongZhi getActivityDanBiChongZhi() {
		return activityDanBiChongZhi;
	}

	public void setActivityDanBiChongZhi(DBActivityDanBiChongZhi activityDanBiChongZhi) {
		this.activityDanBiChongZhi = activityDanBiChongZhi;
	}

	public DBActivityCrossBoss getActivityCrossBoss() {
		return activityCrossBoss;
	}

	public void setActivityCrossBoss(DBActivityCrossBoss activityCrossBoss) {
		this.activityCrossBoss = activityCrossBoss;
	}

	public DBActivityTTHL getActivityTTHL() {
		return activityTTHL;
	}

	public void setActivityTTHL(DBActivityTTHL activityTTHL) {
		this.activityTTHL = activityTTHL;
	}

	public DBActivityQCJJ getActivityQCJJ() {
		return activityQCJJ;
	}

	public void setActivityQCJJ(DBActivityQCJJ activityQCJJ) {
		this.activityQCJJ = activityQCJJ;
	}

	public DBActivityCommon getActivityCommon() {
		return activityCommon;
	}

	public void setActivityCommon(DBActivityCommon activityCommon) {
		this.activityCommon = activityCommon;
	}

}
