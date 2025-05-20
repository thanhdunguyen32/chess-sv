package game.module.activity.logic;

import game.GameServer;
import game.common.DateCommonUtils;
import game.entity.PlayingRole;
import game.module.activity.bean.*;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.ActivityCache;
import game.module.activity.dao.ActivityPlayerCache;
import game.module.activity.dao.ActivityPlayerDao;
import game.module.activity.dao.TestActivitiesNewTemplateCache;
import game.module.mail.constants.MailConstants;
import game.module.mail.logic.MailManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lion.session.GlobalTimer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

/**
 * 玩家个人活动 Manager
 * 
 * @author zhangning
 * 
 * @Date 2015年7月23日 下午4:24:50
 */
public class ActivityPlayerManager {

	private static Logger logger = LoggerFactory.getLogger(ActivityPlayerManager.class);

	ActivityPlayerDao activityPlayerDao = ActivityPlayerDao.getInstance();

	static class SingletonHolder {
		static ActivityPlayerManager instance = new ActivityPlayerManager();
	}

	public static ActivityPlayerManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 登录奖励定时器
	 * 
	 * @param playingRole
	 */
	public void loginTimeEndTimer(final PlayingRole playingRole) {
		final ActivityBean activity = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_LOGIN);
		if (activity == null || activity.getActivityLogin() == null) {
			return;
		}
		DBActivityLogin dbActivityLogin = activity.getActivityLogin();
		final List<ActivityConfigOne> activityConfigOnes = dbActivityLogin.getItemsList();
		if (activityConfigOnes == null || activityConfigOnes.isEmpty()) {
			return;
		}

		Date startTime = activity.getStartTime();
		// int lastDays = activity.getActivityLogin().getDayCount();
		// Date endTime1 = DateUtils.addDays(startTime, lastDays - 1);
		// Calendar endTimeCal = Calendar.getInstance();
		// endTimeCal.setTime(endTime1);
		// endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
		// endTimeCal.set(Calendar.MINUTE, 59);
		// endTimeCal.set(Calendar.SECOND, 59);
		// endTimeCal.set(Calendar.MILLISECOND, 59);
		// Date endTime = endTimeCal.getTime();
		Date endTime = activity.getEndTime();

		// 活动结束
		final ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playingRole.getId(),
				activity.getType());
		if (!DateCommonUtils.betweenDate(startTime, endTime) && activityPlayer != null) {
			loginSendMail(playingRole, activity, activityPlayer, activityConfigOnes);
			return;
		}

		int reachTimeSecs = DateCommonUtils.getSecsReachMonment(endTime);
		if (reachTimeSecs > 0) {
			Timeout activityEndTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
				@Override
				public void run(Timeout arg0) throws Exception {
					loginSendMail(playingRole, activity, activityPlayer, activityConfigOnes);
				}

			}, reachTimeSecs);
			playingRole.setLoginTimeout(activityEndTimeout);
		}
	}

	/**
	 * 未领取奖励, 发放邮件
	 * 
	 * @param playingRole
	 * @param activity
	 * @param activityConfigOnes
	 */
	private void loginSendMail(PlayingRole playingRole, ActivityBean activity, ActivityPlayer activityPlayer,
			List<ActivityConfigOne> activityConfigOnes) {
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
			MailManager.getInstance().sendSysMailToSingle(playingRole.getId(), activity.getTitle(), content, mailAtt);

			// TODO 测试---待删除
			logger.info("Email will be sent after the event, event type: {}, recipient ID: {}", activity.getType(), activityPlayer.getPlayerId());
		}
		// 删除领取记录
		ActivityPlayerCache.getInstance().removeOnePlayerActivityType(playingRole.getId(), activity.getType());
		asyncRemoveOnePlayerActivity(playingRole.getId(), activity.getType());

		// TODO 测试---待删除
		logger.info("Email will be sent after the event, event type: {}, delete the player ID participating in the event: {}", activity.getType(), playingRole.getId());
	}

	/**
	 * 冲级奖励定时器
	 * 
	 * @param playingRole
	 */
	public void levelGoTimeEndTimer(final PlayingRole playingRole) {
		final ActivityBean activity = ActivityCache.getInstance().getOneActivity(ActivityConstants.TYPE_LEVEL_GO);
		if (activity == null || activity.getDbActivityLevelAward() == null) {
			return;
		}

		DBActivityLevelAward dbActivityLevelAward = activity.getDbActivityLevelAward();
		final List<LevelAward> levelAwards = dbActivityLevelAward.getLevelAwardList();
		if (levelAwards == null || levelAwards.isEmpty()) {
			return;
		}

		Date registTime = playingRole.getPlayerBean().getCreateTime();
		int lastDays = activity.getDbActivityLevelAward().getDayCount();
		Date endTime1 = DateUtils.addDays(registTime, lastDays - 1);
		Calendar endTimeCal = Calendar.getInstance();
		endTimeCal.setTime(endTime1);
		endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
		endTimeCal.set(Calendar.MINUTE, 59);
		endTimeCal.set(Calendar.SECOND, 59);
		endTimeCal.set(Calendar.MILLISECOND, 59);
		Date endTime = endTimeCal.getTime();

		// 活动结束
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playingRole.getId(),
				ActivityConstants.TYPE_LEVEL_GO);
		if (!DateCommonUtils.betweenDate(registTime, endTime) && activityPlayer != null) {
			levelGoSendMail(playingRole, activity, levelAwards);
			return;
		}
		int reachTimeSecs = DateCommonUtils.getSecsReachMonment(endTime);
		if (reachTimeSecs >= 0) {
			Timeout activityEndTimeout = GlobalTimer.getInstance().newTimeout(new TimerTask() {
				@Override
				public void run(Timeout arg0) throws Exception {
					levelGoSendMail(playingRole, activity, levelAwards);
				}
			}, reachTimeSecs);

			playingRole.setLevelGoTimeout(activityEndTimeout);
		}
	}

	/**
	 * 冲级奖励未领取, 发放邮件
	 * 
	 * @param playingRole
	 * @param activity
	 * @param levelAwards
	 */
	private void levelGoSendMail(final PlayingRole playingRole, final ActivityBean activity,
			final List<LevelAward> levelAwards) {
		// Check是否有未领取奖励
		List<Award> getLevelAwards = new ArrayList<Award>();
		List<Integer> awardedLevels = getAwardedLevels(playingRole.getId());
		for (LevelAward levelAward : levelAwards) {
			if (playingRole.getPlayerBean().getLevel() >= levelAward.getLevel()
					&& (awardedLevels == null || !awardedLevels.contains(levelAward.getLevel()))) {
				getLevelAwards.addAll(levelAward.getAwardList());
			}
		}

		if (!getLevelAwards.isEmpty()) {
			// 有奖励未领, 通过邮件形式发放
			String sendName = "";
			String content = "";
			List<String> dbMailParam = MailManager.getInstance().getDbMailParam(MailConstants.PARAM_NO,
					StringUtils.EMPTY, MailConstants.PARAM_NO, StringUtils.EMPTY, MailConstants.PARAM_CONTENT,
					activity.getTitle());
			Map<Integer,Integer> mailAtt = getMailAtt(getLevelAwards);
			MailManager.getInstance().sendSysMailToSingle(playingRole.getId(), activity.getTitle(), content, mailAtt);

			// TODO 测试---待删除
			logger.info("Email will be sent after the event, event type: {}, recipient ID: {}", activity.getType(), playingRole.getId());
		}

		// 删除领取记录
		ActivityPlayerCache.getInstance().removeOnePlayerActivityType(playingRole.getId(), activity.getType());
		asyncRemoveOnePlayerActivity(playingRole.getId(), activity.getType());

		// TODO 测试---待删除
		logger.info("Email will be sent after the event, event type: {}, delete the player ID participating in the event: {}", activity.getType(), playingRole.getId());
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
	 * 已经领取冲级奖励
	 * 
	 * @param playerId
	 * @return
	 */
	private List<Integer> getAwardedLevels(int playerId) {
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				ActivityConstants.TYPE_LEVEL_GO);
		if (activityPlayer != null) {
			DBActivityLevel dbActivityLevel = activityPlayer.getDbActivityLevel();
			if (dbActivityLevel != null) {
				return dbActivityLevel.getAwardedLevelList();
			}
		}
		return null;
	}

	/**
	 * 异步添加个人活动信息
	 * 
	 * @param activityPlayer
	 */
	public void asyncInsertActivityPlayer(final ActivityPlayer activityPlayer) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				activityPlayerDao.addActivityPlayer(activityPlayer);
			}
		});
	}

	/**
	 * 异步更新活动进度
	 * 
	 * @param activityPlayer
	 */
	public void asyncUpdateActivityProgerss(final ActivityPlayer activityPlayer) {
		logger.info("asyncUpdateActivityProgerss:playerId={},activityType={},progress={},resetTime={}",
				activityPlayer.getPlayerId(), activityPlayer.getType(), activityPlayer.getProgress(),
				activityPlayer.getResetTime());
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				activityPlayerDao.updateActivityPlayerProgress(activityPlayer);
			}
		});
	}

	/**
	 * 异步更新活动领取奖励信息
	 * 
	 * @param activityPlayer
	 */
	public void asyncUpdateActivityAll(final ActivityPlayer activityPlayer) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				activityPlayerDao.updateActivityPlayerAll(activityPlayer);
			}
		});
	}

	/**
	 * 异步删除某一活动的所有玩家进程
	 * 
	 * @param typeAnswer
	 */
	public void asyncRemovePlayerActivityType(final int type) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				activityPlayerDao.removePlayerActivityType(type);
			}
		});
	}

	/**
	 * 异步删除某个玩家的某一活动的进程
	 * 
	 * @param typeAnswer
	 */
	public void asyncRemoveOnePlayerActivity(final int playerId, final int type) {
		GameServer.executorService.execute(new Runnable() {

			@Override
			public void run() {
				activityPlayerDao.removeOnePlayerActivity(playerId, type);
			}
		});
	}

	public boolean canGetFixedAward(int playerId, int activeIdType, PlayingRole playingRole) {
		boolean ret = true;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId, activeIdType);
		Map<String, Object> activity1Config = TestActivitiesNewTemplateCache.getInstance()
				.getFixedActivity(activeIdType);
		switch (activeIdType) {
		case ActivityConstants.TYPE_LOGIN:
			if (activityPlayer != null) {
				// 是否能领取
				Date now = new Date();
				Date resetTime = activityPlayer.getResetTime();
				List<Map<String, Object>> rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
				if (activityPlayer.getProgress() >= rewardList.size() || now.before(resetTime)) {
					ret = false;
				}
			}
			break;
		case ActivityConstants.TYPE_LEVEL_GO:
			int currentProgress = 0;
			if (activityPlayer != null) {
				currentProgress = activityPlayer.getProgress();
			}
			List<Map<String, Object>> rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
			if (currentProgress >= rewardList.size()) {
				ret = false;
			} else {
				Map<String, Object> step1Config = rewardList.get(currentProgress);
				int needLevel = (int) step1Config.get("limit");
				if (playingRole.getPlayerBean().getLevel() < needLevel) {
					ret = false;
				}
			}
			break;
		case ActivityConstants.TYPE_STAGE:
			currentProgress = 0;
//			if (activityPlayer != null) {
//				currentProgress = activityPlayer.getProgress();
//			}
//			rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
//			if (currentProgress >= rewardList.size()) {
//				ret = false;
//			} else {
//				Map<String, Object> step1Config = rewardList.get(currentProgress);
//				int needStageId = (int) step1Config.get("limit");
//				boolean isStagePassed = StageUtils.isStagePassed(playerId, needStageId);
//				if (!isStagePassed) {
//					ret = false;
//				}
//			}
			break;
		case ActivityConstants.TYPE_ELITE_STAGE:
			currentProgress = 0;
//			if (activityPlayer != null) {
//				currentProgress = activityPlayer.getProgress();
//			}
//			rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
//			if (currentProgress >= rewardList.size()) {
//				ret = false;
//			} else {
//				Map<String, Object> step1Config = rewardList.get(currentProgress);
//				int needEliteStageId = (int) step1Config.get("limit");
//				boolean isEliteStagePassed = StageUtils.isEliteStagePassed(playerId, needEliteStageId);
//				if (!isEliteStagePassed) {
//					ret = false;
//				}
//			}
			break;
		case ActivityConstants.TYPE_ARENA_WIN:
			currentProgress = 0;
//			if (activityPlayer != null) {
//				currentProgress = activityPlayer.getProgress();
//			}
//			rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
//			if (currentProgress >= rewardList.size()) {
//				ret = false;
//			} else {
//				Map<String, Object> step1Config = rewardList.get(currentProgress);
//				int needWinCount = (int) step1Config.get("limit");
//				ArenaBean arenaBean = ArenaPlayerCache.getInstance().getArenaBean(playerId);
//				if (arenaBean == null || arenaBean.getWinSum() < needWinCount) {
//					ret = false;
//				}
//			}
			break;
		case ActivityConstants.TYPE_FREE_FUND:
			// VIP4以上
			if (playingRole.getPlayerBean().getVipLevel() < 8) {
				ret = false;
			}
			currentProgress = 0;
			if (activityPlayer != null) {
				currentProgress = activityPlayer.getProgress();
			}
			rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
			if (currentProgress >= rewardList.size()) {
				ret = false;
			} else {
				Map<String, Object> step1Config = rewardList.get(currentProgress);
				int needLevel = (int) step1Config.get("limit");
				if (playingRole.getPlayerBean().getLevel() < needLevel) {
					ret = false;
				}
			}
			break;
		default:
			break;
		}
		return ret;
	}
	
	public void removeActivityByPlayer(int actType, int playerId) {
		// 移除缓存
		ActivityPlayerCache.getInstance().removePlayerActivityType(actType, playerId);
		// 移除数据库
		asyncRemoveOnePlayerActivity(playerId, actType);
	}

}
