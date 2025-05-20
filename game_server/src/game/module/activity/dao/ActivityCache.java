package game.module.activity.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameServer;
import game.module.activity.bean.Activity4Gm;
import game.module.activity.bean.ActivityBean;
import game.module.activity.bean.DBActivity3DayFirstRecharge;
import game.module.activity.bean.DBActivityAnswerAward;
import game.module.activity.bean.DBActivityAwardDouble;
import game.module.activity.bean.DBActivityChengZhangJiJin;
import game.module.activity.bean.DBActivityChongZhiBang;
import game.module.activity.bean.DBActivityCommon;
import game.module.activity.bean.DBActivityConsume;
import game.module.activity.bean.DBActivityCrossBoss;
import game.module.activity.bean.DBActivityDailyActive;
import game.module.activity.bean.DBActivityFirstRechgeDouble;
import game.module.activity.bean.DBActivityHeroLibao;
import game.module.activity.bean.DBActivityKaiFu;
import game.module.activity.bean.DBActivityLevelAward;
import game.module.activity.bean.DBActivityLianXuChongZhi;
import game.module.activity.bean.DBActivityLogin;
import game.module.activity.bean.DBActivityLotteryWheel;
import game.module.activity.bean.DBActivityMeiRiShouChong;
import game.module.activity.bean.DBActivityMineAward;
import game.module.activity.bean.DBActivityQCJJ;
import game.module.activity.bean.DBActivityQiZhenYiBao;
import game.module.activity.bean.DBActivityQuanMinFuLi;
import game.module.activity.bean.DBActivitySale;
import game.module.activity.bean.DBActivityShiLianJiangLi;
import game.module.activity.bean.DBActivityTTHL;
import game.module.activity.bean.DBActivityVipLiBao;
import game.module.activity.bean.DBActivityXiaoFeiBang;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import io.netty.util.Timeout;
import io.protostuff.ProtostuffIOUtil;
import lion.common.BeanTool;

/**
 * 活动缓存
 * 
 * @author zhangning
 * 
 * @Date 2015年7月21日 下午2:10:15
 */
public class ActivityCache {

	private static Logger logger = LoggerFactory.getLogger(ActivityCache.class);

	static class SingletonHolder {
		static ActivityCache instance = new ActivityCache();
	}

	public static ActivityCache getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 活动缓存<br/>
	 * Key：活动类型 (参考ActivityConstants)
	 */
	private Map<Integer, ActivityBean> activityCacheAll = new ConcurrentHashMap<Integer, ActivityBean>();

	/**
	 * 初始化数据到缓存中
	 * 
	 * @param playerId
	 */
	public void loadFromDb() {
		try {
			List<ActivityBean> activityBeans = ActivityDao.getInstance().getAllActivityBean();
			BeanTool.addOrUpdate(activityCacheAll, activityBeans, "type");
			logger.info("ActivityCache loadFromDb success!");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 所有活动
	 * 
	 * @return
	 */
	public Collection<ActivityBean> getAllActivity() {
		return activityCacheAll.values();
	}

	/**
	 * 获取某一个活动
	 * 
	 * @param type
	 * @return
	 */
	public ActivityBean getOneActivity(int type) {
		return activityCacheAll.get(type);
	}

	/**
	 * 添加活动
	 * 
	 * @param activityBean
	 */
	public void addOneActivity(ActivityBean activityBean) {
		if (activityBean != null) {
			activityCacheAll.put(activityBean.getType(), activityBean);
		}
	}

	public void updateActivity(Activity4Gm activity4Gm) {
		Integer type = activity4Gm.getType();
		ActivityBean activityBean = new ActivityBean();
		activityBean.setType(type);
		activityBean.setTitle(activity4Gm.getTitle());
		activityBean.setDescription(activity4Gm.getDescription());
		activityBean.setIsOpen(activity4Gm.getIsOpen());
		activityBean.setStartTime(activity4Gm.getStartTime());
		activityBean.setEndTime(activity4Gm.getEndTime());
		if (type == ActivityConstants.DYNAMIC_TYPE_RECHARGE || type == ActivityConstants.DYNAMIC_TYPE_DAILY_PAY
				|| type == ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK || type == ActivityConstants.DYNAMIC_TYPE_LOGIN
				|| type == ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG || type == ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI) {
			DBActivityCommon commonAward = new DBActivityCommon();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), commonAward, DBActivityCommon.getSchema());
			activityBean.setActivityCommon(commonAward);
		} else if (type == ActivityConstants.TYPE_CONSUMPTION) {
			DBActivityConsume activityConsume = new DBActivityConsume();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), activityConsume, DBActivityConsume.getSchema());
			activityBean.setActivityConsume(activityConsume);
		} else if (type == ActivityConstants.TYPE_LOGIN) {
			DBActivityLogin activityLogin = new DBActivityLogin();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), activityLogin, DBActivityLogin.getSchema());
			activityBean.setActivityLogin(activityLogin);
		} else if (type == ActivityConstants.TYPE_LEVEL_GO) {
			DBActivityLevelAward dbActivityLevelAward = new DBActivityLevelAward();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivityLevelAward, DBActivityLevelAward.getSchema());
			activityBean.setDbActivityLevelAward(dbActivityLevelAward);
		} else if (type == ActivityConstants.TYPE_MINE) {
			DBActivityMineAward dBActivityMineAward = new DBActivityMineAward();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dBActivityMineAward, DBActivityMineAward.getSchema());
			activityBean.setDbActivityMineAward(dBActivityMineAward);
		} else if (type == ActivityConstants.TYPE_AWARD_DOUBLE) {
			DBActivityAwardDouble dBActivityAwardDouble = new DBActivityAwardDouble();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dBActivityAwardDouble,
					DBActivityAwardDouble.getSchema());
			activityBean.setDbActivityAwardDouble(dBActivityAwardDouble);
		} else if (type == ActivityConstants.TYPE_ANSWER) {
			DBActivityAnswerAward dbActivityAnswerAward = new DBActivityAnswerAward();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivityAnswerAward,
					DBActivityAnswerAward.getSchema());
			activityBean.setDbActivityAnswerAward(dbActivityAnswerAward);
		} else if (type == ActivityConstants.TYPE_SALE) {
			DBActivitySale dbActivitySale = new DBActivitySale();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivitySale.getSchema());
			activityBean.setDbActivitySale(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI) {
			DBActivityKaiFu dbActivitySale = new DBActivityKaiFu();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityKaiFu.getSchema());
			activityBean.setDbActivityKaiFu(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN) {
			DBActivityChengZhangJiJin dbActivitySale = new DBActivityChengZhangJiJin();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityChengZhangJiJin.getSchema());
			activityBean.setDbActivityChengZhangJiJin(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI) {
			DBActivityQuanMinFuLi dbActivitySale = new DBActivityQuanMinFuLi();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityQuanMinFuLi.getSchema());
			activityBean.setActivityQuanMinFuLi(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_VIP_LIBAO) {
			DBActivityVipLiBao dbActivitySale = new DBActivityVipLiBao();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityVipLiBao.getSchema());
			activityBean.setActivityVipLiBao(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE) {
			DBActivityDailyActive dbActivitySale = new DBActivityDailyActive();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityDailyActive.getSchema());
			activityBean.setActivityDailyActive(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE) {
			DBActivity3DayFirstRecharge dbActivitySale = new DBActivity3DayFirstRecharge();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale,
					DBActivity3DayFirstRecharge.getSchema());
			activityBean.setActivity3DayFirstRecharge(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO) {
			DBActivityHeroLibao dbActivitySale = new DBActivityHeroLibao();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityHeroLibao.getSchema());
			activityBean.setActivityHeroLibao(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL) {
			DBActivityLotteryWheel dbActivitySale = new DBActivityLotteryWheel();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityLotteryWheel.getSchema());
			activityBean.setActivityLotteryWheel(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE) {
			DBActivityFirstRechgeDouble dbActivitySale = new DBActivityFirstRechgeDouble();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale,
					DBActivityFirstRechgeDouble.getSchema());
			activityBean.setActivityFirstRechargeDouble(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO) {
			DBActivityQiZhenYiBao dbActivitySale = new DBActivityQiZhenYiBao();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityQiZhenYiBao.getSchema());
			activityBean.setActivityQiZhenYiBao(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG) {
			DBActivityMeiRiShouChong dbActivitySale = new DBActivityMeiRiShouChong();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityMeiRiShouChong.getSchema());
			activityBean.setActivityMeiRiShouChong(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI) {
			DBActivityLianXuChongZhi dbActivitySale = new DBActivityLianXuChongZhi();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityLianXuChongZhi.getSchema());
			activityBean.setActivityLianXuChongZhi(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI) {
			DBActivityShiLianJiangLi dbActivitySale = new DBActivityShiLianJiangLi();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityShiLianJiangLi.getSchema());
			activityBean.setActivityShiLianJiangLi(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG) {
			DBActivityChongZhiBang dbActivitySale = new DBActivityChongZhiBang();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityChongZhiBang.getSchema());
			activityBean.setActivityChongZhiBang(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG) {
			DBActivityXiaoFeiBang dbActivitySale = new DBActivityXiaoFeiBang();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityXiaoFeiBang.getSchema());
			activityBean.setActivityXiaoFeiBang(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_CROSS_BOSS) {
			DBActivityCrossBoss dbActivitySale = new DBActivityCrossBoss();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityCrossBoss.getSchema());
			activityBean.setActivityCrossBoss(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI) {
			DBActivityTTHL dbActivitySale = new DBActivityTTHL();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityTTHL.getSchema());
			activityBean.setActivityTTHL(dbActivitySale);
		} else if (type == ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN) {
			DBActivityQCJJ dbActivitySale = new DBActivityQCJJ();
			ProtostuffIOUtil.mergeFrom(activity4Gm.getParams(), dbActivitySale, DBActivityQCJJ.getSchema());
			activityBean.setActivityQCJJ(dbActivitySale);
		}
		activityCacheAll.put(type, activityBean);

		// 重启(开启)定时器
		resetTimer(type);
	}

	/**
	 * 如果有定时器在跑, 先停止
	 * 
	 * @param type
	 */
	private void resetTimer(int type) {
		Timeout timeout = GameServer.getInstance().getTimeouts(type);
		if (timeout != null) {
			timeout.cancel();
		}

		if (type == ActivityConstants.DYNAMIC_TYPE_RECHARGE) {
			ActivityManager.getInstance().rechargeTimeEndTimer();
		} else if (type == ActivityConstants.TYPE_CONSUMPTION) {
			ActivityManager.getInstance().consumptionTimeEndTimer();
		}
	}

}
