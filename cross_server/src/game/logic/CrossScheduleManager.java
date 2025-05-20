package game.logic;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.boss.logic.BossResetPlayerDataJob;
import cross.boss.logic.BossTimeoutFinishJob;

public class CrossScheduleManager {

	private static Logger logger = LoggerFactory.getLogger(CrossScheduleManager.class);

	static class SingletonHolder {
		static CrossScheduleManager instance = new CrossScheduleManager();
	}

	public static CrossScheduleManager getInstance() {
		return SingletonHolder.instance;
	}

	private Scheduler sched;

	private CrossScheduleManager() {
		try {
			// create the thread pool
			SimpleThreadPool threadPool = new SimpleThreadPool(2, Thread.NORM_PRIORITY);
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
		// add job here
		bossTimeoutFinish();
		bossPlayerDataReset();
	}

	private void bossPlayerDataReset() {
		JobDetail jobDetail = JobBuilder.newJob(BossResetPlayerDataJob.class)
				.withIdentity(BossResetPlayerDataJob.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(BossResetPlayerDataJob.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 55 4 ? * *")).build();
		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void bossTimeoutFinish() {
		JobDetail jobDetail = JobBuilder.newJob(BossTimeoutFinishJob.class)
				.withIdentity(BossTimeoutFinishJob.class.getSimpleName(), "group2").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(BossTimeoutFinishJob.class.getSimpleName() + "_trigger", "group2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 30 20 ? * *")).build();
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
