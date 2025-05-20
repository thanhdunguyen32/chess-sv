/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package cc.mrbird.febs.system.entity;

import java.io.Serializable;
import java.util.Date;

//JPA标识
public class Cdk implements Serializable {

	private int platform;
	private int area;
	private String cdk;
	private String cdkName;
	private int awardId;
	private int reuse;
	private Date startTime;
	private Date endTime;
	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public String getCdk() {
		return cdk;
	}

	public void setCdk(String cdk) {
		this.cdk = cdk;
	}

	public String getCdkName() {
		return cdkName;
	}

	public void setCdkName(String cdkName) {
		this.cdkName = cdkName;
	}

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
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

	public int getReuse() {
		return reuse;
	}

	public void setReuse(int reuse) {
		this.reuse = reuse;
	}

}
