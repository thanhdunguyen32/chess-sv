package game.module.activity.logic;

import game.GameServer;
import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.entity.PlayingRole;
import game.entity.TemplateList;
import game.module.activity.bean.*;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.*;
import game.module.activity.processor.ActivityListProcessor;
import game.module.award.logic.AwardUtils;
import game.module.db.bean.DbChongZhiBangPlayer;
import game.module.log.constants.LogConstants;
import game.module.mail.constants.MailConstants;
import game.module.mail.logic.MailManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.offline.logic.ServerCache;
import game.module.pay.logic.PaymentManager;
import game.module.template.ActivityTemplate;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerInfoManager;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lion.session.GlobalTimer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase.ActivitiesItem;
import ws.WsMessageActivity.PushActivityProgressUpdate;
import ws.WsMessageActivity.S2CActivitiesGetAward;

import java.util.*;
import java.util.Map.Entry;

public class ActivityManager {

	private static Logger logger = LoggerFactory.getLogger(ActivityManager.class);

	static class SingletonHolder {
		static ActivityManager instance = new ActivityManager();
	}

	public static ActivityManager getInstance() {
		return SingletonHolder.instance;
	}

	public List<Activity4Gm> getActivityAllBase() {
		return ActivityDao.getInstance().getAllActivityBase();
	}

	public Activity4Gm getActivityById(int activityId) {
		return ActivityDao.getInstance().getActivity4Gm(activityId);
	}

	public void updateActivity(Activity4Gm activity4Gm) {
		boolean isExist = ActivityDao.getInstance().checkActivityExist(activity4Gm.getType());
		if (isExist) {
			ActivityDao.getInstance().updateActivityBean(activity4Gm);
		} else {
			ActivityDao.getInstance().insertActivityBean(activity4Gm);
		}
		ActivityBean oldActivity = ActivityCache.getInstance().getOneActivity(activity4Gm.getType());
		Date now = new Date();
		boolean isOld = false;
		if (oldActivity == null || (oldActivity.getEndTime() != null && oldActivity.getEndTime().before(now))) {
			isOld = true;
		}
		ActivityCache.getInstance().updateActivity(activity4Gm);
		// 通知客户端
		Integer activityId = activity4Gm.getType();
		ActivityTemplate activityTemp = ActivityTemplateCache.getInstance().getActivityTemplateById(activityId);
		int activytType = activityTemp == null ? ActivityConstants.ACTIVITY_TIME_INDISCIPLINE : activityTemp.getType();

		Collection<PlayingRole> playerAll = SessionManager.getInstance().getAllPlayers();
		if (activytType == ActivityConstants.ACTIVITY_TIME_INDISCIPLINE) {
			int validDayCount = getValidDay(activity4Gm.getType());

			for (PlayingRole aPlayer : playerAll) {
				Date registTime = aPlayer.getPlayerBean().getCreateTime();
				Date endTime1 = DateUtils.addDays(registTime, validDayCount - 1);
				Calendar endTimeCal = Calendar.getInstance();
				endTimeCal.setTime(endTime1);
				endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
				endTimeCal.set(Calendar.MINUTE, 59);
				endTimeCal.set(Calendar.SECOND, 59);
				endTimeCal.set(Calendar.MILLISECOND, 59);
				Date endTime = endTimeCal.getTime();

				if (endTime.after(new Date())) {
					// aPlayer.getGamePlayer().writeAndFlush(16018,
					// PushActivityUpdate.newBuilder().setModuleType(activityId).build());
				}
			}
		} else if (activytType == ActivityConstants.ACTIVITY_TIME_RANGE && activity4Gm.getEndTime().after(new Date())) {
			for (PlayingRole aPlayer : playerAll) {
				// aPlayer.getGamePlayer().writeAndFlush(16018,
				// PushActivityUpdate.newBuilder().setModuleType(activityId).build());
			}
		}
		// 更新缓存
		ActivityBean newActivity = ActivityCache.getInstance().getOneActivity(activity4Gm.getType());
		if (isOld && newActivity.getEndTime() != null && newActivity.getEndTime().after(now)) {
			ServerCache.getInstance().setActivityLastUpdateTime(activity4Gm.getType(), now);
		}
		// 更新结束定时器
		if (activity4Gm.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI) {
			kaiFuJinSaiTimer();
		} else if (activity4Gm.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG) {
			chongZhiBangTimer();
		} else if (activity4Gm.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG) {
			xiaoFeiBangTimer();
		} else if (activity4Gm.getType() == ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK) {
			chongJiBangTimer();
		}
	}

	private void xiaoFeiBangTimer() {
		ActivityBean activity = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG);
		if (activity == null || activity.getActivityChongZhiBang() == null
				|| activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date endTime = activity.getEndTime();
		if (System.currentTimeMillis() > endTime.getTime()) {
			logger.info("消费榜已经过期！,endTime={}", endTime);
			return;
		}
		// 移除老的时间
		Timeout oldTimeout = GameServer.getInstance().getTimeouts(activity.getType());
		if (oldTimeout != null) {
			oldTimeout.cancel();
		}
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);
		Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
			@Override
			public void run(Timeout arg0) throws Exception {
				xiaoFeiBangSendAward();
			}
		}, reachTime);
		GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
	}

	public void xiaoFeiBangSendAward() {
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				logger.info("消费榜发送奖励！");
				ActivityBean activity = ActivityCache.getInstance()
						.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG);
				if (activity == null || activity.getActivityXiaoFeiBang() == null
						|| activity.getIsOpen() == ActivityConstants.NO) {
					return;
				}
				DBActivityXiaoFeiBang activityKaiFu = activity.getActivityXiaoFeiBang();
				List<ActivityConfigOne1> activityConfigOne1s = activityKaiFu.getItemsList();
				// 排行
				List<DbChongZhiBangPlayer> realRankList = ActivityXiaoFeiBangBangCache.getInstance().getRankList();
				int rankIndex = 0;
				for (ActivityConfigOne1 activityConfigOne1 : activityConfigOne1s) {
					if (rankIndex >= realRankList.size()) {
						break;
					}
					DbChongZhiBangPlayer aPlayerInfo = realRankList.get(rankIndex);
					int playerId = aPlayerInfo.getPlayerId();
					int val = aPlayerInfo.getVal();
					// 奖励内容
					List<Award> awards = new ArrayList<>();
					awards.addAll(activityConfigOne1.getAwardList());
					if (activityConfigOne1.getExtraLimit() > 0 && activityConfigOne1.getExtraLimit() <= val) {
						awards.addAll(activityConfigOne1.getExtraLimitAwardList());
					}
					// 邮件奖励
					Map<Integer,Integer> dbMailAtt = getMailAtt(awards);
					List<String> dbMailParam = new ArrayList<>();
					dbMailParam.add(String.valueOf(val));
					dbMailParam.add(String.valueOf(rankIndex + 1));
					MailManager.getInstance().sendSysMailToSingle(playerId, "消费榜标题","消费榜内容", dbMailAtt);
					logger.info("消费榜发放奖励,playerId={},rank={},awards={}", playerId, rankIndex + 1, awards);
					rankIndex++;
				}
			}
		});
		// remove
		GameServer.getInstance().removeTimeout(ActivityConstants.TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG);
	}

	public void chongJiBangTimer() {
		ActivityBean activity = ActivityCache.getInstance().getOneActivity(ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK);
		if (activity == null || activity.getActivityCommon() == null || activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date endTime = activity.getEndTime();
		if (System.currentTimeMillis() > endTime.getTime()) {
			logger.info("冲级榜已经过期！,endTime={}", endTime);
			return;
		}
		// 移除老的时间
		Timeout oldTimeout = GameServer.getInstance().getTimeouts(activity.getType());
		if (oldTimeout != null) {
			oldTimeout.cancel();
		}
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);
		Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
			@Override
			public void run(Timeout arg0) throws Exception {
				chongJiBangSendAward();
			}
		}, reachTime);
		GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
	}

	public void chongJiBangSendAward() {
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				logger.info("冲级榜发送奖励start！");
				ActivityBean activity = ActivityCache.getInstance()
						.getOneActivity(ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK);
				if (activity == null || activity.getActivityCommon() == null
						|| activity.getIsOpen() == ActivityConstants.NO) {
					return;
				}
				DBActivityCommon activityKaiFu = activity.getActivityCommon();
				List<ActivityConfigOne> activityConfigOne1s = activityKaiFu.getItemsList();
				// 排行
				List<PlayerBean> playerRankList = PlayerDao.getInstance().rankPlayerLevel();
				int currentRank = 1;
				for (ActivityConfigOne activityConfigOne : activityConfigOne1s) {
					int targetRank = activityConfigOne.getLevel();
					while (currentRank <= targetRank && currentRank <= playerRankList.size()) {
						PlayerBean aPlayerInfo = playerRankList.get(currentRank - 1);
						int playerId = aPlayerInfo.getId();
						int val = aPlayerInfo.getLevel();
						// 奖励内容
						List<Award> awards = new ArrayList<>();
						awards.addAll(activityConfigOne.getAwardList());
						// 邮件奖励
						Map<Integer,Integer> dbMailAtt = getMailAtt(awards);
						List<String> dbMailParam = new ArrayList<>();
						dbMailParam.add(String.valueOf(val));
						dbMailParam.add(String.valueOf(currentRank));
						MailManager.getInstance().sendSysMailToSingle(playerId,"冲级榜标题","冲级榜内容", dbMailAtt);
						logger.info("冲级榜发放奖励,playerId={},rank={},awards={}", playerId, currentRank, awards);
						currentRank++;
					}
				}
				logger.info("冲级榜发送奖励end！");
			}
		});
		// remove
		GameServer.getInstance().removeTimeout(ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK);
	}

	public void chongZhiBangTimer() {
		ActivityBean activity = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG);
		if (activity == null || activity.getActivityChongZhiBang() == null
				|| activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date endTime = activity.getEndTime();
		if (System.currentTimeMillis() > endTime.getTime()) {
			logger.info("充值榜已经过期！,endTime={}", endTime);
			return;
		}
		// 移除老的时间
		Timeout oldTimeout = GameServer.getInstance().getTimeouts(activity.getType());
		if (oldTimeout != null) {
			oldTimeout.cancel();
		}
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);
		Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
			@Override
			public void run(Timeout arg0) throws Exception {
				chongZhiBangSendAward();
			}
		}, reachTime);
		GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
	}

	public void chongZhiBangSendAward() {
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				logger.info("充值榜发送奖励！");
				ActivityBean activity = ActivityCache.getInstance()
						.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG);
				if (activity == null || activity.getActivityChongZhiBang() == null
						|| activity.getIsOpen() == ActivityConstants.NO) {
					return;
				}
				DBActivityChongZhiBang activityKaiFu = activity.getActivityChongZhiBang();
				List<ActivityConfigOne1> activityConfigOne1s = activityKaiFu.getItemsList();
				// 排行
				List<DbChongZhiBangPlayer> realRankList = ActivityChongZhiBangCache.getInstance().getRankList();
				int rankIndex = 0;
				for (ActivityConfigOne1 activityConfigOne1 : activityConfigOne1s) {
					if (rankIndex >= realRankList.size()) {
						break;
					}
					DbChongZhiBangPlayer aPlayerInfo = realRankList.get(rankIndex);
					int playerId = aPlayerInfo.getPlayerId();
					int val = aPlayerInfo.getVal();
					// 奖励内容
					List<Award> awards = new ArrayList<>();
					awards.addAll(activityConfigOne1.getAwardList());
					if (activityConfigOne1.getExtraLimit() > 0 && activityConfigOne1.getExtraLimit() <= val) {
						awards.addAll(activityConfigOne1.getExtraLimitAwardList());
					}
					// 邮件奖励
					Map<Integer,Integer> dbMailAtt = getMailAtt(awards);
					List<String> dbMailParam = new ArrayList<>();
					dbMailParam.add(String.valueOf(val));
					dbMailParam.add(String.valueOf(rankIndex + 1));
					MailManager.getInstance().sendSysMailToSingle(playerId, "充值榜标题","充值榜内容", dbMailAtt);
					logger.info("充值榜发放奖励,playerId={},rank={},awards={}", playerId, rankIndex + 1, awards);
					rankIndex++;
				}
			}
		});
		// remove
		GameServer.getInstance().removeTimeout(ActivityConstants.TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG);
	}

	/**
	 * 活动有效期
	 * 
	 * @param type
	 * @return
	 */
	private int getValidDay(Integer type) {
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(type);
		if (type == ActivityConstants.TYPE_LEVEL_GO) {
			DBActivityLevelAward dbActivityLevelAward = activityBean.getDbActivityLevelAward();
			if (dbActivityLevelAward != null) {
				return dbActivityLevelAward.getDayCount();
			}
		} else if (type == ActivityConstants.TYPE_MINE) {
			DBActivityMineAward dbActivityMineAward = activityBean.getDbActivityMineAward();
			if (dbActivityMineAward != null) {
				return dbActivityMineAward.getDayCount();
			}
		}

		return 0;
	}

	/**
	 * 充值奖励定时器
	 */
	public void rechargeTimeEndTimer() {
		final ActivityBean activity = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.DYNAMIC_TYPE_RECHARGE);
		if (activity == null || activity.getRechargeAward() == null || activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}

		// if (!DateCommonUtils.betweenDate(activity.getStartTime(),
		// activity.getEndTime())) {
		// return;
		// }

		final List<ActivityConfigOne> activityConfigOnes = activity.getRechargeAward().getItemsList();
		if (activityConfigOnes == null) {
			return;
		}

		Date endTime = activity.getEndTime();
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);

		if (reachTime >= 0) {
			Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
				@Override
				public void run(Timeout arg0) throws Exception {
					// 获取参与冲级奖励的所有玩家
					GameServer.executorService.execute(new Runnable() {
						public void run() {
							awardSendMail(activity, activityConfigOnes);
						}
					});
				}
			}, reachTime);
			GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
		}
	}

	/**
	 * 奖励未领, 发放邮件
	 * 
	 * @param activity
	 */
	private void awardSendMail(ActivityBean activity, List<ActivityConfigOne> activityConfigOnes) {
		List<ActivityPlayer> activitiesPlayer = ActivityPlayerDao.getInstance().getActivitiesType(activity.getType());
		if (activitiesPlayer != null && !activitiesPlayer.isEmpty()) {
			for (ActivityPlayer activityPlayer : activitiesPlayer) {
				List<Integer> gettedLevel = new ArrayList<Integer>();
				DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (dbActivityPlayerGet != null && dbActivityPlayerGet.getLevelList() != null) {
					gettedLevel = dbActivityPlayerGet.getLevelList();
				}

				List<Award> awards = new ArrayList<Award>();
				for (ActivityConfigOne activityConfigOne : activityConfigOnes) {
					if (activityPlayer.getProgress() >= activityConfigOne.getLevel()
							&& !gettedLevel.contains(activityConfigOne.getLevel())) {
						awards.addAll(activityConfigOne.getAwardList());
					}
				}

				if (!awards.isEmpty()) {
					// 有奖励未领, 通过邮件形式发放
					String sendName = "";
					String content = "";
					List<String> dbMailParam = MailManager.getInstance().getDbMailParam(MailConstants.PARAM_NO,
							StringUtils.EMPTY, MailConstants.PARAM_NO, StringUtils.EMPTY, MailConstants.PARAM_CONTENT,
							activity.getTitle());
					Map<Integer,Integer> mailAtt = getMailAtt(awards);
					MailManager.getInstance().sendSysMailToSingle(activityPlayer.getPlayerId(), activity.getTitle(), content, mailAtt);

					// TODO 测试---待删除
					logger.info("活动结束发放邮件, 活动类型:{}, 收件人ID:{}", activity.getType(), activityPlayer.getPlayerId());
				}
			}

			// 删除记录
			ActivityPlayerCache.getInstance().removePlayerActivityType(activity.getType());
			ActivityPlayerDao.getInstance().removePlayerActivityType(activity.getType());

			// TODO 测试---待删除
			logger.info("活动结束发放邮件! 活动类型:{}, 删除参与该活动的玩家个数:{}!", activity.getType(), activitiesPlayer.size());
		} else {
			// TODO 测试---待删除
			logger.info("活动结束发放邮件! 活动类型:{}, 没有查到参与该活动的玩家!", activity.getType());
		}
		// 移除缓存
		Collection<Map<Integer, ActivityPlayer>> cacheAll = ActivityPlayerCache.getInstance().getAll();
		for (Map<Integer, ActivityPlayer> map : cacheAll) {
			map.remove(activity.getType());
		}

	}

	/**
	 * 累计消费定时器
	 */
	public void consumptionTimeEndTimer() {
		final ActivityBean activity = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_CONSUMPTION);
		if (activity == null || activity.getActivityConsume() == null || activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}

		// if (!DateCommonUtils.betweenDate(activity.getStartTime(),
		// activity.getEndTime())) {
		// return;
		// }

		final List<ActivityConfigOne> activityConfigOnes = activity.getActivityConsume().getItemsList();
		if (activityConfigOnes == null) {
			return;
		}

		Date endTime = activity.getEndTime();
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);

		if (reachTime >= 0) {
			Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
				@Override
				public void run(Timeout arg0) throws Exception {
					// 获取参与冲级奖励的所有玩家
					GameServer.executorService.execute(new Runnable() {
						public void run() {
							awardSendMail(activity, activityConfigOnes);
						}
					});
				}
			}, reachTime);

			GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
		}
	}

	/**
	 * 邮件附件
	 * 
	 * @param awardStr
	 * @return
	 */
	public Map<Integer,Integer> getMailAtt(final List<Award> awards) {
		Map<Integer, Integer> itemPairMap = new HashMap<Integer, Integer>();
		for (Award award : awards) {
			int itemCnt = award.getItemCount();
			if (itemPairMap.containsKey(award.getItemId())) {
				itemCnt += itemPairMap.get(award.getItemId());
			}

			itemPairMap.put(award.getItemId(), itemCnt);
		}

		Map<Integer,Integer> dbMailAtt = new HashMap<>();
		for (Entry<Integer, Integer> entry : itemPairMap.entrySet()) {
			dbMailAtt.put(entry.getKey(), entry.getValue());
		}
		return dbMailAtt;
	}

	/**
	 * 已经领取的等级奖励
	 * 
	 * @param id
	 * @return
	 */
	private List<Integer> getAwardedLev(ActivityPlayer activityPlayer) {
		if (activityPlayer != null) {
			DBActivityLevel dbActivityLevel = activityPlayer.getDbActivityLevel();
			if (dbActivityLevel != null) {
				return dbActivityLevel.getAwardedLevelList();
			}
		}
		return null;
	}

	/**
	 * 累计充值活动
	 * 
	 * @param diamond
	 */
	public void recharge(PlayingRole playingRole, int diamond) {
		logger.info("recharge activity,playerId={},diamond={}", playingRole.getId(), diamond);
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(ActivityConstants.DYNAMIC_TYPE_RECHARGE);
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
				ActivityConstants.DYNAMIC_TYPE_RECHARGE);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(diamond);
			activityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_RECHARGE);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(activityPlayer.getProgress() + diamond);
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// TODO 是否可领取check

		// push
		// playingRole.getGamePlayer().writeAndFlush(16012,
		// PushActivityProgressUpdate.newBuilder()
		// .setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_RECHARGE).setProgress(activityPlayer.getProgress()).build());
	}

	/**
	 * 每日充值固定金额以上
	 * 
	 * @param playingRole
	 * @param diamond
	 */
	public void dailyRecharge(PlayingRole playingRole, int diamond) {
		logger.info("daily pay activity,playerId={},diamond={}", playingRole.getId(), diamond);
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.DYNAMIC_TYPE_DAILY_PAY);
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
				ActivityConstants.DYNAMIC_TYPE_DAILY_PAY);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(diamond);
			activityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_DAILY_PAY);
			activityPlayer.setResetTime(new Date());
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			// 今天冲过
			Date oldResetTime = activityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, ActivityConstants.DAILY_RESET_TIME)) {
				DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (dbActivityPlayerGet != null && dbActivityPlayerGet.getLevelList() != null) {
					dbActivityPlayerGet.getLevelList().clear();
				}
				activityPlayer.setProgress(diamond);
			} else {
				activityPlayer.setProgress(activityPlayer.getProgress() + diamond);
			}
			activityPlayer.setResetTime(new Date());
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// TODO 是否可领取check

		// push
		PushActivityProgressUpdate pushMsg = new PushActivityProgressUpdate(activityPlayer.getProgress(),
				ActivityConstants.DYNAMIC_TYPE_DAILY_PAY);
		playingRole.writeAndFlush(pushMsg.build(playingRole.alloc()));
	}

	public void tianTianHaoLi(PlayingRole playingRole, int diamond) {
		logger.info("tian tian hao li activity,playerId={},diamond={}", playingRole.getId(), diamond);
		int playerId = playingRole.getId();
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
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI);
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(diamond);
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI);
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			Date oldResetTime = activityPlayer.getResetTime();
			if (oldResetTime != null && DateUtils.isSameDay(now, oldResetTime)) {
				activityPlayer.setProgress(activityPlayer.getProgress() + diamond);
			} else {
				activityPlayer.setProgress(diamond);
				DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (activityPlayerGet != null) {
					List<Integer> levelList = activityPlayerGet.getLevelList();
					if (levelList != null) {
						levelList.clear();
					}
				}
			}
			activityPlayer.setResetTime(now);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// push
		// playingRole.getGamePlayer().writeAndFlush(16012,
		// PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_TIAN_TIAN_HAO_LI)
		// .setProgress(activityPlayer.getProgress()).build());
	}

	/**
	 * 消费
	 * 
	 * @param playingRole
	 * @param diamond
	 */
	public void consome(PlayingRole playingRole, int diamond) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_CONSUMPTION);
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
				ActivityConstants.TYPE_CONSUMPTION);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(diamond);
			activityPlayer.setType(ActivityConstants.TYPE_CONSUMPTION);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(activityPlayer.getProgress() + diamond);
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// TODO 是否可领取check

		// push
		// playingRole.getGamePlayer().writeAndFlush(16012,
		// PushActivityProgressUpdate.newBuilder()
		// .setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_CONSUME).setProgress(activityPlayer.getProgress()).build());
	}

	public void login(PlayingRole playingRole) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_LOGIN);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now = new Date();
		// 活动开启，是否在有效时间内
		Date startTime = activityBean.getStartTime();
		// int lastDays = activityBean.getActivityLogin().getDayCount();
		// Date endTime1 = DateUtils.addDays(startTime, lastDays - 1);
		// Calendar endTimeCal = Calendar.getInstance();
		// endTimeCal.setTime(endTime1);
		// endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
		// endTimeCal.set(Calendar.MINUTE, 59);
		// endTimeCal.set(Calendar.SECOND, 59);
		// endTimeCal.set(Calendar.MILLISECOND, 59);
		// Date endTime = endTimeCal.getTime();
		Date endTime = activityBean.getEndTime();
		if (!DateCommonUtils.betweenDate(startTime, endTime)) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_LOGIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setType(ActivityConstants.TYPE_LOGIN);
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLastLoginTime(now.getTime());
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (activityPlayerGet != null) {
				if (activityPlayerGet.getLastLoginTime() != null) {
					long lastLoginTimeMil = activityPlayerGet.getLastLoginTime();
					Date lastLoginTime = new Date(lastLoginTimeMil);
					if (!DateUtils.isSameDay(lastLoginTime, now)) {
						activityPlayer.setProgress(activityPlayer.getProgress() + 1);
					}
				} else {
					activityPlayer.setProgress(1);
				}
				activityPlayerGet.setLastLoginTime(now.getTime());
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// TODO 是否可领取check

		// push
		// playingRole.getGamePlayer().write(16012,
		// PushActivityProgressUpdate.newBuilder()
		// .setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_LOGIN).setProgress(activityPlayer.getProgress()).build());
	}

	public void loginDynamic(PlayingRole playingRole) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(ActivityConstants.DYNAMIC_TYPE_LOGIN);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now = new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.DYNAMIC_TYPE_LOGIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_LOGIN);
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLastLoginTime(now.getTime());
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (activityPlayerGet != null) {
				if (activityPlayerGet.getLastLoginTime() != null) {
					long lastLoginTimeMil = activityPlayerGet.getLastLoginTime();
					Date lastLoginTime = new Date(lastLoginTimeMil);
					if (!DateUtils.isSameDay(lastLoginTime, now)) {
						activityPlayer.setProgress(1);
						if (activityPlayer.getActivityPlayerGet().getLevelList() != null) {
							activityPlayer.getActivityPlayerGet().getLevelList().clear();
						}
					}
				} else {
					activityPlayer.setProgress(1);
				}
				activityPlayerGet.setLastLoginTime(now.getTime());
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
	}

	public void buyDuKangDynamic(PlayingRole playingRole, int addVal) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now = new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(addVal);
			activityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG);
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(activityPlayer.getProgress() + addVal);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
	}

	public void buyDuKang10Dynamic(PlayingRole playingRole) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now = new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(1);
			activityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_BUY_DUKANG10);
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(activityPlayer.getProgress() + 1);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
	}

	/**
	 * 奖励翻倍活动: 每个模块奖励倍数
	 * 
	 * @param moudleType
	 *            (模块类型 参考ActivityConstants)
	 * @return
	 */
	public int getMoudelMultiple(int moudleType) {
		// 没有该活动
		ActivityBean activity = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_AWARD_DOUBLE);
		if (activity == null || activity.getDbActivityAwardDouble() == null
				|| activity.getIsOpen() == ActivityConstants.NO) {
			return 1;
		}

		// 活动已结束
		if (!DateCommonUtils.betweenDate(activity.getStartTime(), activity.getEndTime())) {
			return 1;
		}

		DBActivityAwardDouble dbActivityAwardDouble = activity.getDbActivityAwardDouble();
		if (dbActivityAwardDouble != null) {
			List<AwardDouble> awardDoubles = dbActivityAwardDouble.getAwardDoubleList();
			if (awardDoubles != null && !awardDoubles.isEmpty()) {
				long passDays = DateUtils.getFragmentInDays(new Date(), Calendar.YEAR);
				long idx = (passDays%awardDoubles.size())/2;
				AwardDouble validModule0 = awardDoubles.get((int)idx*2);
				AwardDouble validModule1 = awardDoubles.get((int)idx*2+1);
				if(validModule0.getMoudelType() == moudleType) {
					return validModule0.getMultipleCnt() <= 0 ? 1 : validModule0.getMultipleCnt();
				}else if(validModule1.getMoudelType() == moudleType) {
					return validModule1.getMultipleCnt() <= 0 ? 1 : validModule1.getMultipleCnt();
				}
			}
		}

		return 1;
	}

	/**
	 * 折扣季
	 * 
	 * @param storeType
	 * @return
	 */
	public float getSaleResult(int storeType) {
		// // 没有该活动
		// ActivityBean activity =
		// ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_SALE);
		// if (activity == null || activity.getDbActivitySale() == null ||
		// activity.getIsOpen() == ActivityConstants.NO) {
		// return StoreConstants.SALE_PERCENTAGE / StoreConstants.SALE_PERCENTAGE;
		// }
		//
		// // 活动已结束
		// if (!DateCommonUtils.betweenDate(activity.getStartTime(),
		// activity.getEndTime())) {
		// return StoreConstants.SALE_PERCENTAGE / StoreConstants.SALE_PERCENTAGE;
		// }
		//
		// DBActivitySale dbActivitySale = activity.getDbActivitySale();
		// if (dbActivitySale != null) {
		// List<Sale> sales = dbActivitySale.getSaleList();
		// if (sales != null && !sales.isEmpty()) {
		// for (Sale sale : sales) {
		// if (storeType == sale.getStoreType()) {
		// return sale.getSaleResult() <= 0
		// ? StoreConstants.SALE_PERCENTAGE / StoreConstants.SALE_PERCENTAGE
		// : sale.getSaleResult() / StoreConstants.SALE_PERCENTAGE;
		// }
		// }
		// }
		// }
		//
		// return StoreConstants.SALE_PERCENTAGE / StoreConstants.SALE_PERCENTAGE;
		return 0;
	}

	public boolean checkActivityTip(PlayingRole playingRole) {
		Collection<ActivityBean> activityAll = ActivityCache.getInstance().getAllActivity();
		PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playingRole.getId());
		for (ActivityBean activityBean : activityAll) {
			int activityId = activityBean.getType();
			Date lastUpdateTime = ServerCache.getInstance().getActivityLastUpdateTime(activityId);
			Date myViewDate = poc.getActivityTipModules().get(activityId);
			// 活动是否有效
			Date endTime = null;
			if (activityId == ActivityConstants.ACTIVITY_TIME_RANGE) {
				endTime = activityBean.getEndTime();
			} else {
				Date registTime = playingRole.getPlayerBean().getCreateTime();
				int validDays = 0;
				if (activityId == ActivityConstants.TYPE_LEVEL_GO) {
					DBActivityLevelAward dbActivityLevelAward = activityBean.getDbActivityLevelAward();
					if (dbActivityLevelAward != null) {
						validDays = dbActivityLevelAward.getDayCount();
					}
				}
				Date endTime1 = DateUtils.addDays(registTime, validDays - 1);
				Calendar endTimeCal = Calendar.getInstance();
				endTimeCal.setTime(endTime1);
				endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
				endTimeCal.set(Calendar.MINUTE, 59);
				endTimeCal.set(Calendar.SECOND, 59);
				endTime = endTimeCal.getTime();
			}
			Date now = new Date();
			if (activityBean.getIsOpen() == ActivityConstants.YES && endTime != null && endTime.after(now)) {
				if (lastUpdateTime != null && (myViewDate == null || myViewDate.before(lastUpdateTime))) {
					return true;
				}
				if (myViewDate == null) {
					return true;
				}
			}
		}
		return false;
	}

	public void kaiFuJinSaiTimer() {
		ActivityBean activity = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI);
		if (activity == null || activity.getDbActivityKaiFu() == null || activity.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date endTime = activity.getEndTime();
		if (System.currentTimeMillis() > endTime.getTime()) {
			logger.info("开服竞赛已经过期！,endTime={}", endTime);
			return;
		}
		// 移除老的时间
		Timeout oldTimeout = GameServer.getInstance().getTimeouts(activity.getType());
		if (oldTimeout != null) {
			oldTimeout.cancel();
		}
		int reachTime = DateCommonUtils.getSecsReachMonment(endTime);
		Timeout activityTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
			@Override
			public void run(Timeout arg0) throws Exception {
				kaiFuJinSaiSendAward();
			}
		}, reachTime);
		GameServer.getInstance().setTimeouts(activity.getType(), activityTimeout);
	}

	public void kaiFuJinSaiSendAward() {
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				logger.info("开服竞赛发送奖励！");
				ActivityBean activity = ActivityCache.getInstance()
						.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI);
				if (activity == null || activity.getDbActivityKaiFu() == null
						|| activity.getIsOpen() == ActivityConstants.NO) {
					return;
				}
				DBActivityKaiFu activityKaiFu = activity.getDbActivityKaiFu();
				List<ActivityConfigKaifuJinSai> activityConfigKaifuJinSais = activityKaiFu.getItemsList();
				List<ActivityConfigKaifuJinSai> jinJiChangLists = new ArrayList<>();
				List<ActivityConfigKaifuJinSai> zhuShenLists = new ArrayList<>();
				for (ActivityConfigKaifuJinSai aItem : activityConfigKaifuJinSais) {
					if (aItem.getType() == 0) {
						jinJiChangLists.add(aItem);
					} else {
						zhuShenLists.add(aItem);
					}
				}
			}
		});
		// remove
		GameServer.getInstance().removeTimeout(ActivityConstants.TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI);
	}

	/**
	 * 添加成长基金活动记录
	 * 
	 */
	public void chengZhangJiJin(PlayingRole playingRole) {
		logger.info("chengZhangJiJin,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setActivityPlayerGet(null);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// push
		PushActivityProgressUpdate pushMsg = new PushActivityProgressUpdate(activityPlayer.getProgress(),
				ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
		playingRole.getGamePlayer().writeAndFlush(pushMsg.build(playingRole.alloc()));
	}

	public void chengZhangJiJinOffline(Integer playerId) {
		logger.info("chengZhangJiJin offline,playerId={}", playerId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
	}

	/**
	 * 增加每日活跃度
	 * 
	 * @param playingRole
	 * @param addVal
	 */
	public void addDailyActive(PlayingRole playingRole, int addVal) {
		// int playerId = playingRole.getId();
		// ActivityBean activityBean = ActivityCache.getInstance()
		// .getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE);
		// if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO)
		// {
		// return;
		// }
		// Date now = new Date();
		// // 添加记录
		// ActivityPlayer activityPlayer =
		// ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
		// ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE);
		// if (activityPlayer == null) {
		// activityPlayer = new ActivityPlayer();
		// activityPlayer.setPlayerId(playerId);
		// activityPlayer.setProgress(addVal);
		// activityPlayer.setResetTime(DateUtils.addDays(now, 1));
		// activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_DAILY_ACTIVE);
		// DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
		// activityPlayerGet.setLevelList(new ArrayList<Integer>());
		// activityPlayer.setActivityPlayerGet(activityPlayerGet);
		// ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
		// ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		// } else {
		// // 是否需要重置
		// Date resetTime = activityPlayer.getResetTime();
		// if (DateCommonUtils.dayDiff(resetTime, now) >= 0) {
		// activityPlayer.setProgress(addVal);
		// activityPlayer.setResetTime(DateUtils.addDays(now, 1));
		// if (activityPlayer.getActivityPlayerGet() != null
		// && activityPlayer.getActivityPlayerGet().getLevelList() != null) {
		// activityPlayer.getActivityPlayerGet().getLevelList().clear();
		// }
		// } else {
		// activityPlayer.setProgress(activityPlayer.getProgress() + addVal);
		// }
		// ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		// }
		// // push
		// playingRole.getGamePlayer().writeAndFlush(16012,
		// PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_SALE_DAILY_ACTIVE)
		// .setProgress(activityPlayer.getProgress()).build());
	}

	public boolean is3DayFirstRecharge() {
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return false;
		}
		return true;
	}

	public void add3DayFirstRecharge(PlayingRole playingRole) {
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
		if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		Date now = new Date();
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
		if (activityPlayer != null) {
			return;
		}

		activityPlayer = new ActivityPlayer();
		activityPlayer.setPlayerId(playerId);
		activityPlayer.setProgress(0);
		activityPlayer.setResetTime(now);
		activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
		DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
		activityPlayerGet.setLevelList(new ArrayList<Integer>());
		activityPlayer.setActivityPlayerGet(activityPlayerGet);
		ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
		ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		// push
		// playingRole.getGamePlayer().writeAndFlush(16012,
		// PushActivityProgressUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_3DAY_FIRST_RECHARGE)
		// .setProgress(activityPlayer.getProgress()).build());
	}

	public void heroLibao(PlayingRole playingRole, int rechargeId, int addDiamond2) {
		logger.info("hero libao,playerId={}", playingRole.getId());
		int playerId = playingRole.getId();
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
		if (activityBean == null) {
			PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond2, LogConstants.MODULE_PAYMENT);
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond2, LogConstants.MODULE_PAYMENT);
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
			// 购买信息
			DBActivityPlayerHeroLibao heroLibaoBuy = new DBActivityPlayerHeroLibao();
			List<Integer> buyCountList = new ArrayList<>();
			buyCountList.add(1);
			heroLibaoBuy.setBuyCountList(buyCountList);
			List<Integer> rechargeIdList = new ArrayList<>();
			rechargeIdList.add(rechargeId);
			heroLibaoBuy.setRechargeIdList(rechargeIdList);
			activityPlayer.setHeroLibaoBuy(heroLibaoBuy);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerHeroLibao heroLibaoBuy = activityPlayer.getHeroLibaoBuy();
			List<Integer> rechargeIdList = heroLibaoBuy.getRechargeIdList();
			if (rechargeIdList.contains(rechargeId)) {
				int aIndex = rechargeIdList.indexOf(rechargeId);
				int currentBuyCount = heroLibaoBuy.getBuyCountList().get(aIndex);
				// 判断次数是否超过上限
				DBActivityHeroLibao heroLibaoConfig = activityBean.getActivityHeroLibao();
				List<ActivityConfigHeroLiBao> rewardList = heroLibaoConfig.getItemsList();
				for (ActivityConfigHeroLiBao activityConfigHeroLiBao : rewardList) {
					int configRechargeId = activityConfigHeroLiBao.getRechargeId();
					if (rechargeId == configRechargeId) {
						int maxBuyCount = activityConfigHeroLiBao.getBuyCount();
						if (currentBuyCount >= maxBuyCount) {
							int addDiamond = activityConfigHeroLiBao.getPrice() * 10;
							PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond,
									LogConstants.MODULE_PAYMENT);
							return;
						}
					}
				}
				heroLibaoBuy.getBuyCountList().set(aIndex, ++currentBuyCount);
			} else {
				heroLibaoBuy.getRechargeIdList().add(rechargeId);
				heroLibaoBuy.getBuyCountList().add(1);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 发送奖励
		List<ActivitiesItem> rewardItems = new ArrayList<>();
		DBActivityHeroLibao heroLibaoConfig = activityBean.getActivityHeroLibao();
		List<ActivityConfigHeroLiBao> rewardList = heroLibaoConfig.getItemsList();
		int levelIndex = 0;
		for (ActivityConfigHeroLiBao activityConfigHeroLiBao : rewardList) {
			int configRechargeId = activityConfigHeroLiBao.getRechargeId();
			if (rechargeId == configRechargeId) {
				for (Award award : activityConfigHeroLiBao.getAwardList()) {
					AwardUtils.changeRes(playingRole, award.getItemId(), award.getItemCount(), 100 + ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
					rewardItems.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
				}
				break;
			}
			levelIndex++;
		}
		// push
		S2CActivitiesGetAward pushMsg = new S2CActivitiesGetAward();
		pushMsg.activeIdType = ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO;
		pushMsg.item_index = levelIndex;
		pushMsg.gain = new ArrayList<>(rewardItems);
		playingRole.writeAndFlush(pushMsg.build(playingRole.alloc()));
		// push
		// playingRole.getGamePlayer().writeAndFlush(16018,
		// PushActivityUpdate.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_HERO_LIBAO_VALUE));
	}

	public void heroLibaoOffline(Integer playerId, int rechargeId, String userId, int serverid) {
		logger.info("hero libao offline,playerId={}", playerId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
		if (activityBean == null) {
			return;
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			return;
		}
		// 添加记录
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO);
			// 购买信息
			DBActivityPlayerHeroLibao heroLibaoBuy = new DBActivityPlayerHeroLibao();
			List<Integer> buyCountList = new ArrayList<>();
			buyCountList.add(1);
			heroLibaoBuy.setBuyCountList(buyCountList);
			List<Integer> rechargeIdList = new ArrayList<>();
			rechargeIdList.add(rechargeId);
			heroLibaoBuy.setRechargeIdList(rechargeIdList);
			activityPlayer.setHeroLibaoBuy(heroLibaoBuy);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerHeroLibao heroLibaoBuy = activityPlayer.getHeroLibaoBuy();
			List<Integer> rechargeIdList = heroLibaoBuy.getRechargeIdList();
			if (rechargeIdList.contains(rechargeId)) {
				int aIndex = rechargeIdList.indexOf(rechargeId);
				int currentBuyCount = heroLibaoBuy.getBuyCountList().get(aIndex);
				// 判断次数是否超过上限
				DBActivityHeroLibao heroLibaoConfig = activityBean.getActivityHeroLibao();
				List<ActivityConfigHeroLiBao> rewardList = heroLibaoConfig.getItemsList();
				for (ActivityConfigHeroLiBao activityConfigHeroLiBao : rewardList) {
					int configRechargeId = activityConfigHeroLiBao.getRechargeId();
					if (rechargeId == configRechargeId) {
						int maxBuyCount = activityConfigHeroLiBao.getBuyCount();
						if (currentBuyCount >= maxBuyCount) {
							int addDiamond = activityConfigHeroLiBao.getPrice() * 10;
							PlayerDao.getInstance().addMoney(addDiamond, userId);
							PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(userId, serverid);
							if (pb != null) {
								pb.setMoney(pb.getMoney() + addDiamond);
							}
							return;
						}
					}
				}
				heroLibaoBuy.getBuyCountList().set(aIndex, ++currentBuyCount);
			} else {
				heroLibaoBuy.getRechargeIdList().add(rechargeId);
				heroLibaoBuy.getBuyCountList().add(1);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 离线奖励发到邮箱
		Map<Integer, Integer> rewardItems = new HashMap<>();
		List<ActivityConfigHeroLiBao> configList = activityBean.getActivityHeroLibao().getItemsList();
		for (ActivityConfigHeroLiBao activityConfigHeroLiBao : configList) {
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
			List<String> dbMailParam = null;
			MailManager.getInstance().sendSysMailToSingle(playerId, "英雄礼包标题","英雄礼包内容", dbMailAtt);
		}
	}

	public BeanFirstRechargeTriple isFirstRechargeTriple(int playerId, int rechargeId) {
		logger.info("check is first recharge triple,playerId={},rechargeId={}", playerId, rechargeId);
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE);
		if (activityBean == null) {
			return new BeanFirstRechargeTriple(false, 0);
		}
		// 活动开启，是否在有效时间内
		if (activityBean.getIsOpen() == ActivityConstants.NO) {
			return new BeanFirstRechargeTriple(false, 0);
		}
		Date now = new Date();
		if (now.before(activityBean.getStartTime()) || now.after(activityBean.getEndTime())) {
			return new BeanFirstRechargeTriple(false, 0);
		}
		// 没有这项活动
		DBActivityFirstRechgeDouble activityFirstRechgeDouble = activityBean.getActivityFirstRechargeDouble();
		if (activityFirstRechgeDouble == null) {
			return new BeanFirstRechargeTriple(false, 0);
		}
		boolean findRechargeId = false;
		int favorRate = 0;
		List<ActivityFirstRechgeDouble> configList = activityFirstRechgeDouble.getItemsList();
		for (ActivityFirstRechgeDouble activityFirstRechgeDouble2 : configList) {
			if (activityFirstRechgeDouble2.getRechargeId() == rechargeId) {
				findRechargeId = true;
				favorRate = activityFirstRechgeDouble2.getFavorRate();
				break;
			}
		}
		if (!findRechargeId) {
			return new BeanFirstRechargeTriple(false, 0);
		}
		// 是否已经完成
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE);
		if (activityPlayer != null) {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (activityPlayerGet != null) {
				List<Integer> getList = activityPlayerGet.getLevelList();
				if (getList != null && getList.contains(rechargeId)) {
					return new BeanFirstRechargeTriple(false, 0);
				}
			}
		}
		return new BeanFirstRechargeTriple(true, favorRate);
	}

	public static final class BeanFirstRechargeTriple {
		public boolean ret;
		public int favorRate;

		public BeanFirstRechargeTriple(boolean ret, int favorRate) {
			super();
			this.ret = ret;
			this.favorRate = favorRate;
		}
	}

	public void doFirstRechargeTriple(int playerId, Integer rechargeId) {
		logger.info("first recharge triple finish!playerId={},rechargeId={}", playerId, rechargeId);
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE);
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setProgress(0);
			activityPlayer.setResetTime(new Date());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE);
			// 领取信息
			DBActivityPlayerGet heroLibaoBuy = new DBActivityPlayerGet();
			List<Integer> buyCountList = new ArrayList<>();
			buyCountList.add(rechargeId);
			heroLibaoBuy.setLevelList(buyCountList);
			activityPlayer.setActivityPlayerGet(heroLibaoBuy);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet playerGetInfo = activityPlayer.getActivityPlayerGet();
			if (playerGetInfo == null) {
				playerGetInfo = new DBActivityPlayerGet();
				List<Integer> buyCountList = new ArrayList<>();
				buyCountList.add(rechargeId);
				playerGetInfo.setLevelList(buyCountList);
				activityPlayer.setActivityPlayerGet(playerGetInfo);
			} else {
				playerGetInfo.getLevelList().add(rechargeId);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 通知消息
		PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
		if (pr != null && pr.isChannelActive()) {
			ActivityBean activityBean = ActivityCache.getInstance()
					.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE);
			DBActivityFirstRechgeDouble activityFirstRechgeDouble = activityBean.getActivityFirstRechargeDouble();
			int levelIndex = 0;
			List<ActivityFirstRechgeDouble> configList = activityFirstRechgeDouble.getItemsList();
			for (ActivityFirstRechgeDouble activityFirstRechgeDouble2 : configList) {
				if (activityFirstRechgeDouble2.getRechargeId() == rechargeId) {
					break;
				}
				levelIndex++;
			}
			// pr.getGamePlayer().writeAndFlush(16014,
			// S2CActivityGetAward.newBuilder().setModuleType(ACTIVITY_TYPE.ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE)
			// .setLevelIndex(levelIndex).setVipLevel(0));
		}
	}

	/**
	 * 红点提示
	 * 
	 * @param playerId
	 * @return
	 */
	public List<Integer> checkRedPoints(int playerId, PlayingRole playingRole) {
		List<Integer> retList = new ArrayList<>();
		TemplateList fixedActivitiesList = TestActivitiesNewTemplateCache.getInstance().getFixedActivitiesAll();
		for (int i = 0; i < fixedActivitiesList.size(); i++) {
			int activityId = i + 1;
			// 激活码兑换,基金活动 不开放
			if (activityId == 3) {
				continue;
			}
			boolean canFixedAwardGet = ActivityPlayerManager.getInstance().canGetFixedAward(playerId, activityId,
					playingRole);
			if (canFixedAwardGet) {
				retList.add(activityId + 1000);
			}
		}
		// 动态活动
		Collection<ActivityBean> dynamicActivities = ActivityCache.getInstance().getAllActivity();
		for (ActivityBean activityBean : dynamicActivities) {
			// 未开放
			if (activityBean.getIsOpen() == 0) {
				continue;
			}
			// 活动过期
			Date now = new Date();
			int templateId = activityBean.getType();
			ActivityTemplate activityTemplate = ActivityTemplateCache.getInstance().getActivityTemplateById(templateId);
			Date endTime = ActivityListProcessor.getActivityEndTime(playingRole, activityBean);
			if (activityTemplate.getType() != ActivityConstants.ACTIVITY_TIME_FOREVER && endTime != null
					&& endTime.before(now)) {
				continue;
			}
			int curProgress = 0;
			ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
					activityBean.getType());
			if (activityPlayer != null) {
				curProgress = activityPlayer.getProgress();
			}
			switch (activityBean.getType()) {
			case ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL:
				DBActivityLotteryWheel dbActivityLotteryWheel = activityBean.getActivityLotteryWheel();
				curProgress = 0;
				if (activityPlayer != null && activityPlayer.getResetTime() != null
						&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
					curProgress = activityPlayer.getProgress();
				}
				int payCount = 0;
				if (activityPlayer != null
						&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)
						&& activityPlayer.getActivityPlayerGet() != null
						&& activityPlayer.getActivityPlayerGet().getLevelList() != null) {
					payCount = activityPlayer.getActivityPlayerGet().getLevelList().get(0);
				}
				if (curProgress < dbActivityLotteryWheel.getDailyFreeCount()
						|| payCount < dbActivityLotteryWheel.getDailyPayCount()) {
					// 满足条件
					retList.add(activityBean.getType() + 1000);
				}
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN:
				DBActivityChengZhangJiJin dbActivityChengZhangJiJin = activityBean.getDbActivityChengZhangJiJin();
				List<Integer> daimondDailyList = dbActivityChengZhangJiJin.getDiamondDailyList();
				curProgress = -1;
				if (activityPlayer != null) {
					Date startDay = activityPlayer.getResetTime();
					int progress = DateCommonUtils.dayDiff(startDay, new Date());
					if (progress < daimondDailyList.size() && progress >= 0) {
						// 已经领取
						boolean isGet = false;
						DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
						if (dbActivityPlayerGet != null) {
							List<Integer> dayList = dbActivityPlayerGet.getLevelList();
							if (dayList != null && dayList.contains(progress)) {
								isGet = true;
							}
						}
						if (!isGet) {
							// 满足条件
							retList.add(activityBean.getType() + 1000);
						}
					}
				}
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI:
				DBActivityQuanMinFuLi activityQuanMinFuLi = activityBean.getActivityQuanMinFuLi();
				List<ActivityConfigOneFuLi> configList = activityQuanMinFuLi.getItemsList();
				curProgress = PaymentManager.getInstance().getPayPlayers().size();
				for (ActivityConfigOneFuLi activityConfigOneFuLi : configList) {
					if (curProgress < activityConfigOneFuLi.getLevel()) {
						continue;
					}
					// 是否已经领取
					boolean isGet = false;
					if (activityPlayer != null) {
						DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
						if (activityPlayerGet != null) {
							List<Integer> levelList = activityPlayerGet.getLevelList();
							List<Integer> vipLevelList = activityPlayerGet.getVipLevelList();
							int configLevel = activityConfigOneFuLi.getLevel();
							int configVipLevel = activityConfigOneFuLi.getVipLevel();
							for (int aIndex = 0; aIndex < levelList.size(); aIndex++) {
								if (configLevel == levelList.get(aIndex)
										&& configVipLevel == vipLevelList.get(aIndex)) {
									isGet = true;
									break;
								}
							}
						}
					}
					if (!isGet) {
						// 满足条件
						retList.add(activityBean.getType() + 1000);
					}
				}
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI:
				if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
						&& activityPlayer.getActivityPlayerGet().getVipLevelList() != null) {
					List<Integer> canGetCountList = activityPlayer.getActivityPlayerGet().getVipLevelList();
					for (Integer canGetCount : canGetCountList) {
						if (canGetCount > 0) {
							// 满足条件
							retList.add(activityBean.getType() + 1000);
							break;
						}
					}
				}
				break;
			default:
				if (activityBean.getActivityCommon() != null) {
					List<ActivityConfigOne> subItemList = activityBean.getActivityCommon().getItemsList();
					for (ActivityConfigOne activityConfigOne : subItemList) {
						int needLevel = activityConfigOne.getLevel();
						// 还没达到进度
						if (curProgress < needLevel) {
							continue;
						}
						// 已经领取
						if (activityPlayer.getActivityPlayerGet() != null
								&& activityPlayer.getActivityPlayerGet().getLevelList() != null
								&& activityPlayer.getActivityPlayerGet().getLevelList().contains(needLevel)) {
							continue;
						}
						// 满足条件
						retList.add(activityBean.getType() + 1000);
						break;
					}
				}
				break;
			}
		}
		return retList;
	}

}
