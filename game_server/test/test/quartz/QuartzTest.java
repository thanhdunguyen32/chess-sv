package test.quartz;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzTest {

	public static void main(String[] args) throws Exception {
		QuartzTest quartzTest = new QuartzTest();
		quartzTest.run();
	}

	public void run() throws Exception {
		Logger log = LoggerFactory.getLogger(QuartzTest.class);

		log.info("------- Initializing ----------------------");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		log.info("------- Initialization Complete -----------");

		Date runTime = DateBuilder.evenMinuteDate(new Date());

		log.info("------- Scheduling Job  -------------------");

		JobDetail job = JobBuilder.newJob(HelloJob.class)
				.withIdentity("job1", "group1").build();

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "group1").startAt(runTime).build();

		sched.scheduleJob(job, trigger);
		log.info(job.getKey() + " will run at: " + runTime);

		sched.start();

		log.info("------- Started Scheduler -----------------");

		log.info("------- Waiting 65 seconds... -------------");
		try {
			Thread.sleep(65000L);
		} catch (Exception e) {
		}

		log.info("------- Shutting Down ---------------------");
		sched.shutdown(true);
		log.info("------- Shutdown Complete -----------------");
	}

}