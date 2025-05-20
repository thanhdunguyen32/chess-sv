package test.uuid;

import java.util.concurrent.atomic.AtomicLong;

import lion.common.SessionIdGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ConcurrentHashMultiset;

public class UuidConcurrentTest {

	public static Logger log = LoggerFactory.getLogger(UuidConcurrentTest.class);

	static class IdWorkThread implements Runnable {

		private ConcurrentHashMultiset<String> set;

		private SessionIdGenerator idWorker;

		public static AtomicLong totalCount = new AtomicLong();

		public IdWorkThread(ConcurrentHashMultiset<String> set, SessionIdGenerator idWorker) {

			this.set = set;

			this.idWorker = idWorker;

		}

		@Override
		public void run() {

			while (true) {

				String id = idWorker.generateSessionId();
				totalCount.incrementAndGet();
				// log.info("" + totalCount.longValue());
				// if (!set.add(id)) {

				// System.out.println("duplicate:" + id);

				// }

			}

		}

	}

	public static void main(String[] args) {
		if (args[0].equals("apache")) {
			new UuidConcurrentTest().test();
		} else if (args[0].equals("idworker")) {
			new IdWorkerTest().test();
		}
	}

	public void test() {

		ConcurrentHashMultiset<String> set = ConcurrentHashMultiset.create();

		final SessionIdGenerator idWorker = new SessionIdGenerator();

		Thread t1 = new Thread(new IdWorkThread(set, idWorker));

		Thread t2 = new Thread(new IdWorkThread(set, idWorker));
		Thread t3 = new Thread(new IdWorkThread(set, idWorker));
		Thread t4 = new Thread(new IdWorkThread(set, idWorker));

		t1.setDaemon(true);

		t2.setDaemon(true);
		t3.setDaemon(true);
		t4.setDaemon(true);

		t1.start();

		t2.start();
		t3.start();
		t4.start();

		try {

			Thread.sleep(60000);
			log.info("total count:{}", IdWorkThread.totalCount.longValue());
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}
}
