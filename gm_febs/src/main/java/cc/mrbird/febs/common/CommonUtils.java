package cc.mrbird.febs.common;

import io.protostuff.LinkedBuffer;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 公共工具类
 * 
 * @author zhangning
 *
 */
public class CommonUtils {

	private static final ThreadLocal<LinkedBuffer> buffers = new ThreadLocal<LinkedBuffer>();

	/**
	 * 每个线程有自己的buffer，可以共享
	 * 
	 * @return
	 */
	public static LinkedBuffer getProtoBuffer() {
		LinkedBuffer buffer = buffers.get();
		if (buffer == null) {
			buffer = LinkedBuffer.allocate(8192);
			buffers.set(buffer);
		}
		buffer.clear();
		return buffer;
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Numbers.toString(hi | (val & (hi - 1)), Numbers.MAX_RADIX).substring(1);
	}

	/**
	 * 以62进制（字母加数字）生成19位UUID，最短的UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}

	/**
	 * 获取固定数量的激活码
	 * 
	 * @param cdkCnt
	 * @return
	 */
	public static Set<String> getCdkLength18(int cdkCnt) {
		Set<String> cdkSet = new HashSet<String>(cdkCnt);
		
		for (int i = 0; i < cdkCnt; i++) {
			cdkSet.add(uuid());
		}
		return cdkSet;
	}
	
	public static Set<String> getCdkLength15(int cdkCnt) {
		Set<String> cdkSet = new HashSet<String>(cdkCnt);

		for (int i = 0; i < cdkCnt; i++) {
			String acdk = RandomStringUtils.randomAlphanumeric(15);
			cdkSet.add(acdk);
		}
		return cdkSet;
	}

	public static void main(String[] args) {
		System.out.println(uuid());
		// String attach = "11040001|10-1|10000";
		// attach = "1001|10-1001|10-1001|10";
		// int[][] attachs = getArraysOfStr(attach);
		// for (int[] is : attachs) {
		// System.out.println(is[0] + "..." + is[1]);
		// }

		// System.out.println(randomCdk(16));

		// Set<String> someCdk = getSomeCdk(10, 16);
		// for (String string : someCdk) {
		// System.out.println(string);
		// }
		
		for (int i = 0; i < 5000; i++) {
			String acdk = RandomStringUtils.randomAlphanumeric(15);
			System.out.println(acdk);
		}
	}
}
