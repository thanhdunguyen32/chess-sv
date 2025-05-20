package test.uuid;

import java.util.concurrent.atomic.AtomicLong;

import lion.common.IdWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ConcurrentHashMultiset;

public class IdWorkerTest {

	public static Logger log = LoggerFactory.getLogger(IdWorkerTest.class);

	static class IdWorkThread implements Runnable {

		private ConcurrentHashMultiset<Long> set;

		private IdWorker idWorker;

		public static AtomicLong totalCount = new AtomicLong();

		public IdWorkThread(ConcurrentHashMultiset<Long> set, IdWorker idWorker) {

			this.set = set;

			this.idWorker = idWorker;

		}

		@Override
		public void run() {

			while (true) {

				long id = idWorker.nextId();
				totalCount.incrementAndGet();
				// log.info("" + totalCount.longValue());
				// if (!set.add(id)) {

				// System.out.println("duplicate:" + id);

				// }

			}

		}

	}

	public void test() {
		ConcurrentHashMultiset<Long> set = ConcurrentHashMultiset.create();

		final IdWorker idWorker = new IdWorker(0, 0);

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

	public static void main(String[] args) {

		ConcurrentHashMultiset<Long> set = ConcurrentHashMultiset.create();

		final IdWorker idWorker = new IdWorker(0, 0);

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
