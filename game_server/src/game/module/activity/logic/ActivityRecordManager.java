package game.module.activity.logic;

import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.entity.PlayingRole;
import game.module.activity.bean.*;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.*;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mail.constants.MailConstants;
import game.module.mail.logic.MailManager;
import game.module.pay.logic.PaymentManager;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerInfoManager;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase.ActivitiesItem;
import ws.WsMessageActivity.PushActivityProgressUpdate;
import ws.WsMessageActivity.S2CActivitiesGetAward;

import java.util.*;

public class ActivityRecordManager {

	private static Logger logger = LoggerFactory.getLogger(ActivityRecordManager.class);

	static class SingletonHolder {
		static ActivityRecordManager instance = new ActivityRecordManager();
	}

	public static ActivityRecordManager getInstance() {
		return SingletonHolder.instance;
	}

	public void meiRiShouChong(PlayingRole playingRole) {
		logger.info("每日首充活动,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setResetTime(now);
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			// 今天冲过
			activityPlayer.setProgress(1);
			Date oldResetTime = activityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, ActivityConstants.DAILY_RESET_TIME)) {
				DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (dbActivityPlayerGet != null && dbActivityPlayerGet.getLevelList() != null) {
					dbActivityPlayerGet.getLevelList().clear();
				}
			}
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// push
//		playingRole.getGamePlayer().writeAndFlush(16012,
//				PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_MEI_RI_SHOU_CHONG)
//						.setProgress(activityPlayer.getProgress()).build());
	}

	public void lianXuChongZhi(PlayingRole playingRole) {
		logger.info("连续充值活动,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setResetTime(now);
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			// 前几天冲过
			Date oldResetTime = activityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, ActivityConstants.DAILY_RESET_TIME)) {
				activityPlayer.setProgress(activityPlayer.getProgress() + 1);
			}
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// push
//		playingRole.getGamePlayer().writeAndFlush(16012,
//				PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI)
//						.setProgress(activityPlayer.getProgress()).build());
	}

	public void meiRiShouChongOffline(int playerId) {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
		if (targetActivityPlayer == null) {
			List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
			for (ActivityPlayer activityPlayer : activitysPlayer) {
				if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG) {
					targetActivityPlayer = activityPlayer;
				}
			}
		}
		Date now = new Date();
		if (targetActivityPlayer == null) {
			targetActivityPlayer = new ActivityPlayer();
			targetActivityPlayer.setPlayerId(playerId);
			targetActivityPlayer.setProgress(1);
			targetActivityPlayer.setResetTime(now);
			targetActivityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG);
			ActivityPlayerDao.getInstance().addActivityPlayer(targetActivityPlayer);
		} else {
			// 今天冲过
			targetActivityPlayer.setProgress(1);
			Date oldResetTime = targetActivityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, ActivityConstants.DAILY_RESET_TIME)) {
				DBActivityPlayerGet dbActivityPlayerGet = targetActivityPlayer.getActivityPlayerGet();
				if (dbActivityPlayerGet != null && dbActivityPlayerGet.getLevelList() != null) {
					dbActivityPlayerGet.getLevelList().clear();
				}
			}
			targetActivityPlayer.setResetTime(now);
			ActivityPlayerDao.getInstance().updateActivityPlayerProgress(targetActivityPlayer);
		}
	}

	public void lianXuChongZhiOffline(int playerId) {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
		if (targetActivityPlayer == null) {
			List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
			for (ActivityPlayer activityPlayer : activitysPlayer) {
				if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI) {
					targetActivityPlayer = activityPlayer;
				}
			}
		}
		Date now = new Date();
		if (targetActivityPlayer == null) {
			targetActivityPlayer = new ActivityPlayer();
			targetActivityPlayer.setPlayerId(playerId);
			targetActivityPlayer.setProgress(0);
			targetActivityPlayer.setResetTime(now);
			targetActivityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI);
			ActivityPlayerDao.getInstance().addActivityPlayer(targetActivityPlayer);
		} else {
			// 前几天冲过
			Date oldResetTime = targetActivityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, ActivityConstants.DAILY_RESET_TIME)) {
				targetActivityPlayer.setProgress(targetActivityPlayer.getProgress() + 1);
			}
			targetActivityPlayer.setResetTime(now);
			ActivityPlayerDao.getInstance().updateActivityPlayerProgress(targetActivityPlayer);
		}
	}

	public void shiLianJIangLi(PlayingRole playingRole) {
		logger.info("钻石十连抽奖励活动,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI);
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setResetTime(now);
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			// 前几天冲过
			// Date oldResetTime = activityPlayer.getResetTime();
			// if (oldResetTime != null &&
			// DateCommonUtils.isSameDay(oldResetTime,
			// ActivityConstants.DAILY_RESET_TIME)) {
			activityPlayer.setProgress(activityPlayer.getProgress() + 1);
			// } else {
			// activityPlayer.setProgress(1);
			// // 清空领取信息
			// DBActivityPlayerGet dbActivityPlayerGet =
			// activityPlayer.getActivityPlayerGet();
			// if (dbActivityPlayerGet != null &&
			// dbActivityPlayerGet.getLevelList() != null) {
			// dbActivityPlayerGet.getLevelList().clear();
			// }
			// }
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// push
//		playingRole.getGamePlayer().writeAndFlush(16012,
//				PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_SHI_LIAN_JIANG_LI)
//						.setProgress(activityPlayer.getProgress()).build());
	}

	public void danBiChongZhi(PlayingRole playingRole, int addYuan) {
		logger.info("单笔充值活动,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		// 是否在级别内
		int targetLevel = 0;
		boolean findTargetLevel = false;
		DBActivityCommon danBiChongZhiConfig = activityBean.getActivityCommon();
		List<ActivityConfigOne> danBiChongZhiConfigList = danBiChongZhiConfig.getItemsList();
		for (int i = danBiChongZhiConfigList.size() - 1; i >= 0; i--) {
			ActivityConfigOne configOne = danBiChongZhiConfigList.get(i);
			if (addYuan >= configOne.getLevel()) {
				targetLevel = configOne.getLevel();
				findTargetLevel = true;
				break;
			}
		}
		if (!findTargetLevel) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setResetTime(now);
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
			//
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			List<Integer> levelList = new ArrayList<>();
			levelList.add(targetLevel);
			activityPlayerGet.setLevelList(levelList);
			List<Integer> getCountList = new ArrayList<>();
			getCountList.add(1);
			activityPlayerGet.setVipLevelList(getCountList);
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setResetTime(now);
			// 可领取次数增加
			List<Integer> levelList = activityPlayer.getActivityPlayerGet().getLevelList();
			if (levelList.contains(targetLevel)) {
				int myIndex = levelList.indexOf(targetLevel);
				int targetCount = activityPlayer.getActivityPlayerGet().getVipLevelList().get(myIndex);
				if (targetCount <= 0) {
					targetCount = 1;
				} else {
					targetCount += 1;
				}
				activityPlayer.getActivityPlayerGet().getVipLevelList().set(myIndex, targetCount);
			} else {
				levelList.add(targetLevel);
				activityPlayer.getActivityPlayerGet().getVipLevelList().add(1);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// push
		PushActivityProgressUpdate pushMsg = new PushActivityProgressUpdate(targetLevel,
				ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
		playingRole.getGamePlayer().writeAndFlush(pushMsg.build(playingRole.alloc()));
	}

	public void danBiChongZhiOffline(int playerId, int addYuan) {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		// 是否在级别内
		int targetLevel = 0;
		boolean findTargetLevel = false;
		DBActivityCommon danBiChongZhiConfig = activityBean.getActivityCommon();
		List<ActivityConfigOne> danBiChongZhiConfigList = danBiChongZhiConfig.getItemsList();
		for (int i = danBiChongZhiConfigList.size() - 1; i >= 0; i--) {
			ActivityConfigOne configOne = danBiChongZhiConfigList.get(i);
			if (addYuan >= configOne.getLevel()) {
				targetLevel = configOne.getLevel();
				findTargetLevel = true;
				break;
			}
		}
		if (!findTargetLevel) {
			return;
		}
		ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
		if (targetActivityPlayer == null) {
			List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
			for (ActivityPlayer activityPlayer : activitysPlayer) {
				if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI) {
					targetActivityPlayer = activityPlayer;
				}
			}
		}
		Date now = new Date();
		if (targetActivityPlayer == null) {
			targetActivityPlayer = new ActivityPlayer();
			targetActivityPlayer.setPlayerId(playerId);
			targetActivityPlayer.setProgress(1);
			targetActivityPlayer.setResetTime(now);
			targetActivityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI);
			//
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			List<Integer> levelList = new ArrayList<>();
			levelList.add(targetLevel);
			activityPlayerGet.setLevelList(levelList);
			List<Integer> getCountList = new ArrayList<>();
			getCountList.add(1);
			activityPlayerGet.setVipLevelList(getCountList);
			targetActivityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(targetActivityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(targetActivityPlayer);
		} else {
			targetActivityPlayer.setResetTime(now);
			// 可领取次数增加
			List<Integer> levelList = targetActivityPlayer.getActivityPlayerGet().getLevelList();
			if (levelList.contains(targetLevel)) {
				int myIndex = levelList.indexOf(targetLevel);
				int targetCount = targetActivityPlayer.getActivityPlayerGet().getVipLevelList().get(myIndex);
				if (targetCount <= 0) {
					targetCount = 1;
				} else {
					targetCount += 1;
				}
				targetActivityPlayer.getActivityPlayerGet().getVipLevelList().set(myIndex, targetCount);
			} else {
				levelList.add(targetLevel);
				targetActivityPlayer.getActivityPlayerGet().getVipLevelList().add(1);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(targetActivityPlayer);
		}
	}

	public void chongZhiBang(int playerId, int addYuan) {
		logger.info("充值榜活动,playerId={}", playerId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		int addDiamond = addYuan * 10;
		ActivityChongZhiBangCache.getInstance().doChongZhi(playerId, addDiamond);
		// push
		PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
		if (playingRole != null) {
			int myVal = ActivityChongZhiBangCache.getInstance().getMyVal(playerId);
//			playingRole.getGamePlayer().writeAndFlush(16012, PushActivityProgressUpdate.newBuilder()
//					.setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_CHONG_ZHI_BAND).setProgress(myVal));
		}
	}

	public void xiaoFeiBang(int playerId, int costDiamond) {
		logger.info("消费榜活动,playerId={}", playerId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		ActivityXiaoFeiBangBangCache.getInstance().doXiaoFei(playerId, costDiamond);
		// push
		PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
		if (playingRole != null) {
			int myVal = ActivityXiaoFeiBangBangCache.getInstance().getMyVal(playerId);
//			playingRole.getGamePlayer().writeAndFlush(16012, PushActivityProgressUpdate.newBuilder()
//					.setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_XIAO_FEI_BANG).setProgress(myVal));
		}
	}
	
	public void tianTianHaoLiOffline(Integer playerId, int addDiamond) {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return;
		}
		ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI);
		if (targetActivityPlayer == null) {
			List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
			for (ActivityPlayer activityPlayer : activitysPlayer) {
				if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI) {
					targetActivityPlayer = activityPlayer;
				}
			}
		}
		Date now = new Date();
		if (targetActivityPlayer == null) {
			targetActivityPlayer = new ActivityPlayer();
			targetActivityPlayer.setPlayerId(playerId);
			targetActivityPlayer.setProgress(addDiamond);
			targetActivityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI);
			targetActivityPlayer.setResetTime(now);
			ActivityPlayerDao.getInstance().addActivityPlayer(targetActivityPlayer);
		} else {
			Date oldResetTime = targetActivityPlayer.getResetTime();
			if (oldResetTime != null && DateUtils.isSameDay(now, oldResetTime)) {
				targetActivityPlayer.setProgress(targetActivityPlayer.getProgress() + addDiamond);
			} else {
				targetActivityPlayer.setProgress(addDiamond);
				DBActivityPlayerGet activityPlayerGet = targetActivityPlayer.getActivityPlayerGet();
				if (activityPlayerGet != null) {
					List<Integer> levelList = activityPlayerGet.getLevelList();
					if (levelList != null) {
						levelList.clear();
					}
				}
			}
			targetActivityPlayer.setResetTime(now);
			ActivityPlayerDao.getInstance().updateActivityPlayerProgress(targetActivityPlayer);
		}
	}
	
	public void qingChunJiJin(PlayingRole playingRole, int rechargeId, int addDiamond2) {
		logger.info("qing chun ji jin,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
		if (activityBean == null) {
			PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond2, LogConstants.MODULE_PAYMENT);
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond2, LogConstants.MODULE_PAYMENT);
			return;
		}
		Date now = new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
			// 购买信息
			DBActivityPlayerGet heroLibaoBuy = new DBActivityPlayerGet();
			List<Integer> rechargeIdList = new ArrayList<>();
			rechargeIdList.add(rechargeId);
			heroLibaoBuy.setLevelList(rechargeIdList);
			activityPlayer.setActivityPlayerGet(heroLibaoBuy);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet heroLibaoBuy = activityPlayer.getActivityPlayerGet();
			Date oldResetTime = activityPlayer.getResetTime();
			if(oldResetTime == null || !DateUtils.isSameDay(now, oldResetTime)){
				if(heroLibaoBuy != null && heroLibaoBuy.getLevelList() != null){
					heroLibaoBuy.getLevelList().clear();
				}
			}
			List<Integer> rechargeIdList = heroLibaoBuy.getLevelList();
			if (rechargeIdList.contains(rechargeId)) {
				// 判断次数是否超过上限
				DBActivityQCJJ heroLibaoConfig = activityBean.getActivityQCJJ();
				List<ActivityConfigQCJJ> rewardList = heroLibaoConfig.getItemsList();
				for (ActivityConfigQCJJ activityConfigHeroLiBao : rewardList) {
					int configRechargeId = activityConfigHeroLiBao.getRechargeId();
					if (rechargeId == configRechargeId) {
						int maxBuyCount = activityConfigHeroLiBao.getBuyCount();
						if (1 >= maxBuyCount) {
							int addDiamond = activityConfigHeroLiBao.getPrice() * 10;
							PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond,
									LogConstants.MODULE_PAYMENT);
							return;
						}
					}
				}
			} else {
				heroLibaoBuy.getLevelList().add(rechargeId);
			}
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 发送奖励
		List<ActivitiesItem> rewardItems = new ArrayList<>();
		DBActivityQCJJ heroLibaoConfig = activityBean.getActivityQCJJ();
		List<ActivityConfigQCJJ> rewardList = heroLibaoConfig.getItemsList();
		int levelIndex = 0;
		for (ActivityConfigQCJJ activityConfigHeroLiBao : rewardList) {
			int configRechargeId = activityConfigHeroLiBao.getRechargeId();
			if (rechargeId == configRechargeId) {
				for (Award award : activityConfigHeroLiBao.getAwardList()) {
					AwardUtils.changeRes(playingRole, award.getItemId(), award.getItemCount(), 100 + ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
					rewardItems.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
				}
				// 额外钻石
				Integer extraDiamondCount = activityConfigHeroLiBao.getExtraDiamond();
				if (extraDiamondCount != null && extraDiamondCount > 0) {
					AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, extraDiamondCount,
							100 + ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
					rewardItems.add(new ActivitiesItem(GameConfig.PLAYER.YB, extraDiamondCount));
				}
				break;
			}
			levelIndex++;
		}
		S2CActivitiesGetAward pushMsg = new S2CActivitiesGetAward();
		pushMsg.activeIdType = ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN;
		pushMsg.item_index = levelIndex;
		pushMsg.gain = new ArrayList<>(rewardItems);
		playingRole.writeAndFlush(pushMsg.build(playingRole.alloc()));
	}
	
	public void qingChunJiJinOffline(Integer playerId, int rechargeId, String userId,int serverid) {
		logger.info("qing chun ji jin offline,playerId={}", playerId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now =new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
			// 购买信息
			DBActivityPlayerGet heroLibaoBuy = new DBActivityPlayerGet();
			List<Integer> rechargeIdList = new ArrayList<>();
			rechargeIdList.add(rechargeId);
			heroLibaoBuy.setLevelList(rechargeIdList);
			activityPlayer.setActivityPlayerGet(heroLibaoBuy);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet heroLibaoBuy = activityPlayer.getActivityPlayerGet();
			Date oldResetTime = activityPlayer.getResetTime();
			if(oldResetTime == null || !DateUtils.isSameDay(now, oldResetTime)){
				if(heroLibaoBuy != null && heroLibaoBuy.getLevelList() != null){
					heroLibaoBuy.getLevelList().clear();
				}
			}
			List<Integer> rechargeIdList = heroLibaoBuy.getLevelList();
			if (rechargeIdList.contains(rechargeId)) {
				// 判断次数是否超过上限
				DBActivityQCJJ heroLibaoConfig = activityBean.getActivityQCJJ();
				List<ActivityConfigQCJJ> rewardList = heroLibaoConfig.getItemsList();
				for (ActivityConfigQCJJ activityConfigHeroLiBao : rewardList) {
					int configRechargeId = activityConfigHeroLiBao.getRechargeId();
					if (rechargeId == configRechargeId) {
						int maxBuyCount = activityConfigHeroLiBao.getBuyCount();
						if (1 >= maxBuyCount) {
							int addDiamond = activityConfigHeroLiBao.getPrice() * 10;
							PlayerDao.getInstance().addMoney(addDiamond, userId);
							PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(userId,serverid);
							if (pb != null) {
								pb.setMoney(pb.getMoney() + addDiamond);
							}
							return;
						}
					}
				}
			} else {
				heroLibaoBuy.getLevelList().add(rechargeId);
			}
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 离线奖励发到邮箱
		Map<Integer, Integer> rewardItems = new HashMap<>();
		List<ActivityConfigQCJJ> configList = activityBean.getActivityQCJJ().getItemsList();
		for (ActivityConfigQCJJ activityConfigHeroLiBao : configList) {
			if (rechargeId == activityConfigHeroLiBao.getRechargeId()) {
				for (Award award : activityConfigHeroLiBao.getAwardList()) {
					rewardItems.put(award.getItemId(), award.getItemCount());
				}
				break;
			}
		}
		if (rewardItems.size() > 0) {
			// 放入附件
			Map<Integer,Integer> dbMailAtt = PaymentManager.getMailAtt(rewardItems);
			// 邮件奖励
			MailManager.getInstance().sendSysMailToSingle(playerId, "青春基金标题","青春基金内容", dbMailAtt);
		}
	}

	public boolean checkHasQcjjActivity() {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN);
		if (activityBean == null) {
			return false;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return false;
		}
		return true;
	}

	public void addLotteryWheelCount(int playerId) {
		// 活动是否有效
		int activeIdType = ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL;
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(activeIdType);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		// 活动是否在时间内
		if (activityBean.getType() == ActivityConstants.ACTIVITY_TIME_RANGE) {
			Date startTime = activityBean.getStartTime();
			Date endTime = activityBean.getEndTime();
			if (!DateCommonUtils.betweenDate(startTime, endTime)) {
				return;
			}
		}
		//do
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activeIdType);
		if(activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(-1);
			activityPlayer.setType(activeIdType);
			activityPlayer.setResetTime(new Date());
		} else {
			int curProgress = 0;
			if (activityPlayer.getResetTime() != null
					&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
				curProgress = activityPlayer.getProgress();
			}
			activityPlayer.setProgress(curProgress - 1);
			// 清空付费购买记录
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (!DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
				if (activityPlayerGet != null && activityPlayerGet.getLevelList() != null) {
					activityPlayerGet.getLevelList().set(0, 0);
				}
			}
			activityPlayer.setResetTime(new Date());
		}
		// 保存记录
		if (activityPlayer.getId() == null) {
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
	}

	public boolean checkHasActivity(int actId) {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(actId);
		if (activityBean == null) {
			return false;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO
				|| !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
			return false;
		}
		return true;
	}
}
