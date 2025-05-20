package game.module.vip.logic;

import game.entity.IntPair;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.mail.constants.MailConstants;
import game.module.mail.logic.MailManager;
import game.module.pay.logic.ChargeTemplateCache;
import game.module.template.VipTemplate;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerInfoManager;
import game.module.vip.bean.VipCount;
import game.module.vip.dao.VipTemplateCache;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VIP逻辑处理
 * 
 * @author zhangning
 * 
 * @Date 2015年3月3日 下午5:12:10
 */
public class VipManager implements Job {

	private static Logger logger = LoggerFactory.getLogger(VipManager.class);

	// StoreDao storeDao = StoreDao.getInstance();

	static class SingletonHolder {
		static VipManager instance = new VipManager();
	}

	public static VipManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 充值, 升级VIP
	 * 
	 * @param playingRole
	 * @param diamond
	 */
	public void rechargeVipLev(PlayingRole playingRole, int diamond) {
		logger.info("add vip,player={},val={}",playingRole.getId(), diamond);
		if (diamond <= 0) {
			logger.error("玩家:{}充值, 钻石值小于等于0", playingRole.getId());
			return;
		}
		// 更新VIP
		int nowVipExp = playingRole.getPlayerBean().getVipExp();
		nowVipExp += diamond;
		int oldVipLevel = playingRole.getPlayerBean().getVipLevel();
		int vipLev = addVipExp(nowVipExp);
		if(vipLev > oldVipLevel){
			WsMessageHall.PushPropChange retMsg = new WsMessageHall.PushPropChange(GameConfig.PLAYER.VIP, vipLev);
			playingRole.getGamePlayer().write(retMsg.build(playingRole.alloc()));
		}
		PlayerInfoManager.getInstance().updateVipLevAndExp(playingRole, vipLev, nowVipExp);
		logger.info("玩家提升vip经验，add_exp={},sum_exp={},vip_level={}", diamond, nowVipExp, vipLev);
	}

	public int addVipExp(int newVipExp) {
		int vipLev = 0;
		List<VipTemplate> vipTemplates = VipTemplateCache.getInstance().getTemplateList();
		for (int i = 0; i < vipTemplates.size(); i++) {
			if (newVipExp >= vipTemplates.get(i).getEXP()) {
				vipLev = i;
			}
		}
		return vipLev;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		logger.info("vip award job execute!");
//		long startTime = System.currentTimeMillis();
//		List<Map<String, Object>> rewardsConfig = ChargeTemplateCache.getInstance().getVipAwards();
//		// 所有玩家
//		List<IntPair> playerAllVip = PlayerDao.getInstance().getPlayerAllVip();
//		for (IntPair intPair : playerAllVip) {
//			int playerId = intPair.getA();
//			int vipLevel = intPair.getB();
//			List<List<Integer>> awards = (List<List<Integer>>)rewardsConfig.get(vipLevel).get("award");
//			// 邮件奖励
//			// 附件
//			Map<Integer, Integer> dbMailAtt = getMailAtt(awards);
//			MailManager.getInstance().sendSysMailToSingle(playerId, "VIP奖励标题","VIP奖励内容", dbMailAtt);
//			logger.info("send vip award,playerId={},vipLevel={}",playerId,vipLevel);
//		}
//		logger.info("vip award job success!cost={}ms", System.currentTimeMillis() - startTime);
	}

	private Map<Integer, Integer> getMailAtt(List<List<Integer>> paramList) {
		Map<Integer,Integer> dbMailAtt = new HashMap<>();
		for (List<Integer> aParam : paramList) {
			dbMailAtt.put(aParam.get(0), aParam.get(1));
		}
		return dbMailAtt;
	}

	public VipCount createVipCount(int playerId) {
		VipCount vipCount = new VipCount();
		vipCount.setPlayerId(playerId);
		vipCount.setActCount(1);
		vipCount.setLastActTime(new Date());
		return vipCount;
	}
}
