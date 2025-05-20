package game.module.mail.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MailZhdTemplate;
import lion.common.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.mail.constants.MailConstants;
import lion.common.Reloadable;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件：配置文件缓存
 * 
 * @author zhangning
 *
 */
public class MailTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(MailTemplateCache.class);

	static class SingletonHolder {
		static MailTemplateCache instance = new MailTemplateCache();
	}

	public static MailTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private List<MailZhdTemplate> templateList;

	private List<MailZhdTemplate> initTemplateList;

	@Override
	public void reload() {
		try {
			String fileName = MailZhdTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			List<MailZhdTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MailZhdTemplate>>() {
			});
			logger.info("zhd mails size={}", templateWrapperMap.size());
			templateList = templateWrapperMap;
			fileName = "mailInit.json";
			jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			List<MailZhdTemplate> templateWrapperMapInit = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<MailZhdTemplate>>() {
			});
			logger.info("init mails size={}", templateWrapperMapInit.size());
			initTemplateList = templateWrapperMapInit;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 邮件附件
	 * 
	 * @param id
	 * @return
	 */
	public String getAttachs(int id) {
		return StringUtils.EMPTY;
	}

	/**
	 * 邮件有效时间
	 * 
	 * @param id
	 * @return
	 */
	public int getDelTime(int id) {
		return MailConstants.SYS_MAIL_MAX_DELTIME;
	}

	public MailZhdTemplate getMailZhdTemplate(int mailIndex){
		return templateList.get(mailIndex);
	}

	public List<MailZhdTemplate> getInitTemplateList(){
		return initTemplateList;
	}

	public static void main(String[] args) {
		new MailTemplateCache().reload();
	}

}
