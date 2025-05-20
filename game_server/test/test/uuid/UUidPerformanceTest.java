package test.uuid;

import lion.common.IdWorker;
import lion.common.SessionIdGenerator;

public class UUidPerformanceTest {

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			testUuid();
		}
	}

	public static void testIdWorker() {
		IdWorker idWorker = new IdWorker(0, 0);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			idWorker.nextId();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("cost:" + (endTime - startTime) + "ms");
	}

	public static void testUuid() {
		SessionIdGenerator sessionIdGenerator = new SessionIdGenerator();
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			sessionIdGenerator.generateSessionId();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("cost:" + (endTime - startTime) + "ms");
	}

}
