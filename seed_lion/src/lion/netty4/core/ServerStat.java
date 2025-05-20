package lion.netty4.core;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerStat {

	public static long startUpTime;

	public static AtomicInteger queryCount = new AtomicInteger();

	public static volatile int maxPackageSize = 0;
	
	public static long lastVisitTime;
	
	public static int lastQueryCount;

}
