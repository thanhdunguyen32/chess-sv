package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 邮件：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "Mail")
public class MailTemplate {

	private Integer id;

	private String mail_title;

	private String mail_content;

	private String mail_item;

	private Integer deletetime;

	private String sender;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMail_title() {
		return mail_title;
	}

	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}

	public String getMail_content() {
		return mail_content;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}

	public String getMail_item() {
		return mail_item;
	}

	public void setMail_item(String mail_item) {
		this.mail_item = mail_item;
	}

	public Integer getDeletetime() {
		return deletetime;
	}

	public void setDeletetime(Integer deletetime) {
		this.deletetime = deletetime;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
