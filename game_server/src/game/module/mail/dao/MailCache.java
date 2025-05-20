package game.module.mail.dao;

import game.module.mail.bean.MailBean;
import game.module.mail.constants.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件缓存数据
 *
 * @author zhangning
 * @Date 2014年12月29日 下午2:52:06
 *
 */
public class MailCache {

	private static Logger logger = LoggerFactory.getLogger(MailCache.class);

	static class SingletonHolder {
		static MailCache instance = new MailCache();
	}

	public static MailCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 邮件缓存<br/>
	 * Key：玩家唯一ID
	 */
	private Map<Integer, List<MailBean>> mailCacheAll = new ConcurrentHashMap<>();

	/**
	 * 初始化数据到缓存中
	 *
	 * @param playerId
	 */
	public void loadFromDb(int playerId) {
		if (mailCacheAll.containsKey(playerId)) {
			return;
		}
		// Load玩家邮件
		List<MailBean> mailBeanList = MailDao.getInstance().getPlayerMailAll(playerId);
		mailCacheAll.put(playerId, mailBeanList);
	}

	/**
	 * 下线删除缓存
	 *
	 * @param playerId
	 */
	public void remove(int playerId) {
		mailCacheAll.remove(playerId);
	}

	// --------------------------------------玩家邮件--------------------------------------------//

	/**
	 * 获取玩家所有邮件
	 *
	 * @param playerId
	 * @return
	 */
	public List<MailBean> getMailCache(int playerId) {
		return mailCacheAll.get(playerId);
	}

	public Collection<List<MailBean>> getMailCacheAll(){
		return mailCacheAll.values();
	}

	/**
	 * 玩家的邮件数量
	 *
	 * @param playerId
	 * @return
	 */
	public int getMailCnt(int playerId) {
		List<MailBean> mails = getMailCache(playerId);
		if (mails != null) {
			return mails.size();
		}
		return 0;
	}

	/**
	 * 获取某个邮件
	 *
	 * @param playerId
	 * @param mailId
	 *            ：邮件模版ID
	 * @return
	 */
	public MailBean getOneMail(int playerId, int mailId) {
		List<MailBean> mailMap = getMailCache(playerId);
		int mailIndex = mailId-1;
		if (mailMap != null && mailIndex < mailMap.size()) {
			return mailMap.get(mailIndex);
		}
		return null;
	}

	public void sortMails(int playerId) {
		List<MailBean> mailMap = getMailCache(playerId);
		if (mailMap == null) {
			return;
		}
		mailMap.sort((m1, m2) -> {
			if (m1.getState() != m2.getState()) {
				return m2.getState() - m1.getState();
			} else {
				return (int) (m1.getCreateTime().getTime() - m2.getCreateTime().getTime());
			}
		});
	}

	/**
	 * Add mail cache for a player
	 * @param playerId Player ID
	 * @param mailCache List of mail beans
	 */
	public void addMailCache(int playerId, List<MailBean> mailCache) {
		mailCacheAll.put(playerId, mailCache);
	}

}
