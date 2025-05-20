package game.module.pay.bean;

import java.util.Date;

public class ChargeEntity {

	private Integer id;

	private Integer playerId;

	private Date firstPayTime;
	
	private Boolean isCzjj;

	private DbVipPackGet dbVipPackGet;

	private DbPaymentLevels dbPaymentLevels;

	/**
	 * 霸王特权
	 */
	private Date bwEndTime;
	/**
	 * 勤政爱民特权
	 */
	private Date qzEndTime;
	/**
	 * 招贤纳士特权
	 */
	private Boolean isZxns;
	/**
	 * 军团特权
	 */
	private Date jtEndTime;
	/**
	 * 月度特权
	 */
	private Date ydEndTime;
	/**
	 * 贵族月卡结束时间
	 */
	private Date gzYuekaEndTime;

	/**
	 * 至尊月卡结束时间
	 */
	private Date zzYuekaEndTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public DbVipPackGet getDbVipPackGet() {
		return dbVipPackGet;
	}

	public void setDbVipPackGet(DbVipPackGet dbVipPackGet) {
		this.dbVipPackGet = dbVipPackGet;
	}

	public DbPaymentLevels getDbPaymentLevels() {
		return dbPaymentLevels;
	}

	public void setDbPaymentLevels(DbPaymentLevels dbPaymentLevels) {
		this.dbPaymentLevels = dbPaymentLevels;
	}

	public Date getGzYuekaEndTime() {
		return gzYuekaEndTime;
	}

	public void setGzYuekaEndTime(Date gzYuekaEndTime) {
		this.gzYuekaEndTime = gzYuekaEndTime;
	}

	public Date getZzYuekaEndTime() {
		return zzYuekaEndTime;
	}

	public void setZzYuekaEndTime(Date zzYuekaEndTime) {
		this.zzYuekaEndTime = zzYuekaEndTime;
	}

	public Boolean getCzjj() {
		return isCzjj;
	}

	public void setCzjj(Boolean czjj) {
		isCzjj = czjj;
	}

	public Date getFirstPayTime() {
		return firstPayTime;
	}

	public void setFirstPayTime(Date firstPayTime) {
		this.firstPayTime = firstPayTime;
	}

	public Date getBwEndTime() {
		return bwEndTime;
	}

	public void setBwEndTime(Date bwEndTime) {
		this.bwEndTime = bwEndTime;
	}

	public Date getQzEndTime() {
		return qzEndTime;
	}

	public void setQzEndTime(Date qzEndTime) {
		this.qzEndTime = qzEndTime;
	}

	public Boolean getZxns() {
		return isZxns;
	}

	public void setZxns(Boolean zxns) {
		isZxns = zxns;
	}

	public Date getJtEndTime() {
		return jtEndTime;
	}

	public void setJtEndTime(Date jtEndTime) {
		this.jtEndTime = jtEndTime;
	}

	public Date getYdEndTime() {
		return ydEndTime;
	}

	public void setYdEndTime(Date ydEndTime) {
		this.ydEndTime = ydEndTime;
	}

	@Override
	public String toString() {
		return "ChargeEntity{" +
				"id=" + id +
				", playerId=" + playerId +
				", firstPayTime=" + firstPayTime +
				", isCzjj=" + isCzjj +
				", dbVipPackGet=" + dbVipPackGet +
				", dbPaymentLevels=" + dbPaymentLevels +
				", bwEndTime=" + bwEndTime +
				", qzEndTime=" + qzEndTime +
				", isZxns=" + isZxns +
				", jtEndTime=" + jtEndTime +
				", ydEndTime=" + ydEndTime +
				", gzYuekaEndTime=" + gzYuekaEndTime +
				", zzYuekaEndTime=" + zzYuekaEndTime +
				'}';
	}
}
