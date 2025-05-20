// file cronjob đặt schedule cho các event sự kiện 
// nhưng mà nó đang nằm trong folder game_server mà folder này nó chạy db của gg04 không phải db của gmtool
// ko này là nó đang chạy mặc định mà 
// e mở sv kia cũng có file này á
package game.module.quartz;

import game.module.bigbattle.logic.DayResetManager;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.kingpvp.logic.KpMissionResetManager;
import game.module.mail.logic.MailDailyZhdManager;
import game.module.mail.logic.MailManager;
import game.module.pvp.logic.PvpManager;
import game.module.pvp.logic.PvpWeekRewardManager;
import game.module.rank.logic.RankManager;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzSchedulerManager {

	private static Logger logger = LoggerFactory.getLogger(QuartzSchedulerManager.class);

	static class SingletonHolder {
		static QuartzSchedulerManager instance = new QuartzSchedulerManager();
	}

	public static QuartzSchedulerManager getInstance() {
		return SingletonHolder.instance;
	}

	private Scheduler sched;

	private QuartzSchedulerManager() {
		try {
			// create the thread pool
			SimpleThreadPool threadPool = new SimpleThreadPool(1, Thread.NORM_PRIORITY);
			threadPool.setThreadNamePrefix("Quartz_Scheduler");
			threadPool.initialize();
			// create the job store
			JobStore jobStore = new RAMJobStore();
			DirectSchedulerFactory.getInstance().createScheduler("Quartz_Scheduler", "Quartz_Scheduler_Instance",
					threadPool, jobStore);
			sched = DirectSchedulerFactory.getInstance().getScheduler("Quartz_Scheduler");

			initJobs();

			sched.start();
		} catch (SchedulerException e) {
			logger.error("", e);
		}
	}

	private void initJobs() {
		// 检查过期邮件
		checkPassDueMailJob();
		rankJob();
		pvpDayReward();
		pvpWeekRewardReset();
		guozhanReward();
		dayResetTask();
		dailyZhdMail();
		kingPvpMissionReset();
	}

	private void guozhanReward() {
		JobDetail jobDetail = JobBuilder.newJob(GuoZhanManager.class)
				.withIdentity(GuoZhanManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(GuoZhanManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 5 22 * * ?")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void kingPvpMissionReset() {
		JobDetail jobDetail = JobBuilder.newJob(KpMissionResetManager.class)
				.withIdentity(KpMissionResetManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(KpMissionResetManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("13 59 23 ? * FRI")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 周活动每日奖励邮件
	 */
	private void dailyZhdMail() {
		JobDetail jobDetail = JobBuilder.newJob(MailDailyZhdManager.class)
				.withIdentity(MailDailyZhdManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(MailDailyZhdManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("43 1 0 * * ?")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void dayResetTask() {
		JobDetail jobDetail = JobBuilder.newJob(DayResetManager.class)
				.withIdentity(DayResetManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(DayResetManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("5 0 0 * * ?")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 每日凌晨5时：检查过期邮件
	 */
	private void checkPassDueMailJob() {
		JobDetail jobDetail = JobBuilder.newJob(MailManager.class)
				.withIdentity(MailManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(MailManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 5 * * ?")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 排行榜
	 */
	private void rankJob() {
		JobDetail jobDetail = JobBuilder.newJob(RankManager.class)
				.withIdentity(RankManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(RankManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 30 5,17 * * ?")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 竞技演武场pvp每日奖励
	 */
	private void pvpDayReward() {
		JobDetail jobDetail = JobBuilder.newJob(PvpManager.class)
				.withIdentity(PvpManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(PvpManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("1 0 23 ? * 1,2,3,4,6,7")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 竞技演武场pvp每周奖励
	 */
	private void pvpWeekRewardReset() {
		JobDetail jobDetail = JobBuilder.newJob(PvpWeekRewardManager.class)
				.withIdentity(PvpWeekRewardManager.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(PvpWeekRewardManager.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("11 0 23 ? * THU")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void shutdown() {
		try {
			sched.shutdown(true);
		} catch (SchedulerException e) {
			logger.error("", e);
		}
	}

}
