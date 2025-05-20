/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.entity.Mail;
import cc.mrbird.febs.system.entity.MailItem;

import java.util.Date;

/**
 * Thư
 * 
 * @author zhangning
 * 
 * @Date 2015年1月8日 上午10:21:48
 */
public class MailData {

	/**
	 * Tạo thư mới
	 */
	public static Mail createMail(int zoneId, String loginName, int templateId) {
		Mail mail = new Mail();
		mail.setZoneId(zoneId);
		mail.setLoginName(loginName);
		mail.setTemplateId(templateId);
		mail.setSendTime(new Date());
		return mail;
	}

	/**
	 * Tạo nội dung thư
	 * @param addressee Loại người nhận (1: người chơi cụ thể, 2: tất cả người chơi)
	 * @param receiveId ID người nhận (nhiều ID cách nhau bằng dấu |)
	 * @param sender Tên người gửi
	 * @param title Tiêu đề thư
	 * @param content Nội dung thư
	 * @param attach Vật phẩm đính kèm (định dạng: ID|Số lượng)
	 * @param validity Thời gian hết hạn (tính bằng ngày)
	 */
	public static MailItem createMailItem(byte addressee, String receiveId, String sender, String title,
											String content, String attach, int validity) {
		MailItem mailItem = new MailItem();
		mailItem.setAddressee(addressee);
		// Xử lý ID người nhận - chuẩn hóa dấu | và loại bỏ khoảng trắng
		if (receiveId != null) {
			receiveId = receiveId.trim()
				.replaceAll("[\\s\\n]+", "|")  // Thay thế khoảng trắng và xuống dòng bằng dấu |
				.replaceAll(",", "|")          // Thay thế dấu phẩy bằng dấu |
				.replaceAll("\\|+", "|")       // Gộp nhiều dấu | thành một
				.replaceAll("^\\||\\|$", "");  // Xóa dấu | ở đầu và cuối
		}
		mailItem.setReceiveId(receiveId);
		mailItem.setSender(sender);
		mailItem.setMailTitle(title);
		mailItem.setMailContent(content);
		mailItem.setMailAttach(attach);
		mailItem.setValidityTime(validity);
		return mailItem;
	}
}
		