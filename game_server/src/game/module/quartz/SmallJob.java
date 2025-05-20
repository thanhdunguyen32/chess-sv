package game.module.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmallJob implements Job {
	private static Logger _log = LoggerFactory.getLogger(SmallJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		_log.info("small job! - " + new Date());
	}
}
