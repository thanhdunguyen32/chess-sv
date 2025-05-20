package game.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import lion.common.StringConstants;

/**
 * 公共工具类
 * 
 * @author zhangning
 *
 */
public class CommonUtils {

	/**
	 * 根据字符串解析为数组(2个长度的数组)<br/>
	 * 符合类型：1001|10
	 * 
	 * @param str
	 * @return
	 */
	public static int[] getArrayOfShu(String str) {
		return getArray(str, StringConstants.SEPARATOR_SHU);
	}

	/**
	 * 根据字符串解析为数组(2个长度的数组)<br/>
	 * 符合类型：1001-1010
	 * 
	 * @param str
	 * @return
	 */
	public static int[] getArrayOfHeng(String str) {
		return getArray(str, StringConstants.SEPARATOR_HENG);
	}

	/**
	 * 解析为数组
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	private static int[] getArray(String str, String split) {
		if (StringUtils.isNotEmpty(str)) {
			String[] params = StringUtils.split(str, split);
			int[] result = new int[params.length];
			for (int i = 0; i < params.length; i++) {
				int paramInt = Integer.valueOf(params[i]);
				result[i] = paramInt;
			}
			return result;
		}
		return null;
	}

	/**
	 * 根据字符串解析为二维数组(n个长度的数组)<br/>
	 * 符合类型：1001|10-1001|10-1001|10 <br/>
	 * 
	 * @param str
	 * @return
	 */
	public static int[][] getArraysOfStr(String str) {
		if (StringUtils.isNotEmpty(str)) {
			String[] params = StringUtils.split(str, StringConstants.SEPARATOR_HENG);
			int[][] result = new int[params.length][2];
			if (ArrayUtils.isNotEmpty(params)) {
				for (int i = 0; i < params.length; i++) {
					String paramStr = params[i];
					if (StringUtils.isNotEmpty(paramStr)) {
						String[] paramStrArrs = StringUtils.split(paramStr, StringConstants.SEPARATOR_SHU);
						for (int j = 0; j < paramStrArrs.length; j++) {
							result[i][j] = Integer.valueOf(paramStrArrs[j]);
						}
					}

				}
			}

			return result;
		}
		return null;
	}

	/**
	 * 生成激活码
	 * 
	 * @param cdk_len
	 *            生成的密码的总长度
	 * @return 激活码的字符串
	 */
	public static String randomCdk(int cdk_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'G', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer cdk = new StringBuffer("");
		Random r = new Random();
		while (count < cdk_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				cdk.append(str[i]);
				count++;
			}
		}

		return cdk.toString();
	}

	/**
	 * 字符串长度, 中文算2个长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getStrLength(String str) {
		int strLength = 0;
		if (!StringUtils.isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				char strChar = str.charAt(i);
				if (isChinese(strChar)) {
					strLength += 2;
				} else {
					strLength += 1;
				}
			}
		}
		return strLength;
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 随机数字
	 * 
	 * @param baseNum
	 * @return
	 */
	public static int getRandomNum(int baseNum) {
		Random random = new Random();
		return random.nextInt(baseNum) + 1;
	}

	public static String getHost(String socketAddr) {
		String ret = "";
		int maoIndex = socketAddr.indexOf(":");
		ret = socketAddr.substring(1, maoIndex);
		return ret;
	}
	
	public static String[] LongArray2Str(List<Long> longArray){
		String[] retStrs = new String[longArray.size()];
		for(int i=0;i<longArray.size();i++){
			retStrs[i] = String.valueOf(longArray.get(i));
		}
		return retStrs;
	}
	
	public static int[] intArray2Raw(List<Integer> longArray){
		int[] retStrs = new int[longArray.size()];
		for(int i=0;i<longArray.size();i++){
			retStrs[i] = longArray.get(i);
		}
		return retStrs;
	}

	public static long[] longArray2Raw(List<Long> longArray){
		long[] retStrs = new long[longArray.size()];
		for(int i=0;i<longArray.size();i++){
			retStrs[i] = longArray.get(i);
		}
		return retStrs;
	}
	
	public static final int RESET_HOUR = 5;
	
	public static void main(String[] args) {
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
		// String socketAddr = "/192.168.120.245:59986";
		// String ret = getHost(socketAddr);
		// System.out.println(ret);

		String aa = "我;;";
		System.out.println(getStrLength(aa));
		System.out.println(isChinese('犇'));
	}

	public static <T> List<T> fillList(int listSize, T aval) {
		List<T> alist = new ArrayList<>(listSize);
		for (int i = 0; i < listSize; i++) {
			alist.add(aval);
		}
		return alist;
	}

}
