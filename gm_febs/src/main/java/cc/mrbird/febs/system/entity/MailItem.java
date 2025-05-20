/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

//JPA标识
@Data
@TableName("tmail_item")
public class MailItem implements Serializable {

	private byte addressee;

	private String receiveId;

	private String sender;

	private String mailTitle;

	private String mailContent;

	private String mailAttach;

	private int validityTime;

	public byte getAddressee() {
		return addressee;
	}

	public void setAddressee(byte addressee) {
		this.addressee = addressee;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailAttach() {
		return mailAttach;
	}

	public void setMailAttach(String mailAttach) {
		this.mailAttach = mailAttach;
	}

	public int getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(int validityTime) {
		this.validityTime = validityTime;
	}

}
