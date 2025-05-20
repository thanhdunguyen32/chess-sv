package lion.common;

import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * 数据加密
 * 
 * @author serv_dev
 * 
 */
public class DataEncryption {

	protected static final Logger logger = LoggerFactory.getLogger(DataEncryption.class);

	public static final byte ENCRY_STR = (byte) 0xe5;

	/**
	 * 按位取反
	 * 
	 * @param data
	 * @return
	 */
	public static boolean BitReversion(byte[] data, int begin) {
		for (int i = begin; i < data.length; i++) {
			int temp = (int) data[i];
			temp = ~temp;
			data[i] = (byte) temp;
		}
		return true;
	}

	/**
	 * 异或加密
	 * 
	 * @param data
	 * @param begin
	 * @return
	 */
	public static boolean xorEveryByte(byte[] data, int begin) {
		for (int i = begin; i < data.length; i++) {
			data[i] ^= ENCRY_STR;
		}
		return true;
	}

	public static void xorEveryByte(ByteBuf toReadBytes) {
		byte[] bytes = toReadBytes.array();
		int startIndex = toReadBytes.readerIndex();
		int couldReadLength = toReadBytes.readableBytes();
		for (int i = startIndex; i < couldReadLength; i++) {
			bytes[i] ^= ENCRY_STR;
		}
	}

	public static byte[] encrypt(byte secretByte, byte[] sourceArray) {
		byte[] dstArray = new byte[outLength(sourceArray.length)];
		byte tmpByte = 1;

		int srcArrayIndex = sourceArray.length - 1;
		int zeroIndex = 0;
		for (int i = dstArray.length - 1; i >= 0; i--) {

			if (zeroIndex == 7) {
				dstArray[i] = (byte) (tmpByte >> (8 - zeroIndex) & getShiftByte(8 - zeroIndex));
				zeroIndex = 0;
				tmpByte = 1;
				continue;
			}

			if (srcArrayIndex < 0) {
				dstArray[i] = (byte) ((byte) (tmpByte >> (8 - zeroIndex)) & getShiftByte(8 - zeroIndex));
				break;
			}

			byte oneByte = sourceArray[srcArrayIndex--];
			int newByte = (tmpByte >> (8 - zeroIndex) & getShiftByte(8 - zeroIndex)) | (oneByte << zeroIndex);
			dstArray[i] = (byte) (newByte & 0X7F);
			tmpByte = oneByte;
			zeroIndex++;
		}
		return dstArray;
	}

	public static byte[] encrypt(byte secretByte, byte[] sourceArray, int startIndex, int length) {
		byte[] dstArray = new byte[outLength(length)];
		byte tmpByte = 1;

		int srcArrayIndex = startIndex + length - 1;
		int zeroIndex = 0;
		for (int i = dstArray.length - 1; i >= 0; i--) {

			if (zeroIndex == 7) {
				dstArray[i] = (byte) (tmpByte >> (8 - zeroIndex) & getShiftByte(8 - zeroIndex));
				zeroIndex = 0;
				tmpByte = 1;
				continue;
			}

			if (srcArrayIndex < startIndex) {
				dstArray[i] = (byte) ((byte) (tmpByte >> (8 - zeroIndex)) & getShiftByte(8 - zeroIndex));
				break;
			}

			byte oneByte = sourceArray[srcArrayIndex--];
			int newByte = (tmpByte >> (8 - zeroIndex) & getShiftByte(8 - zeroIndex)) | (oneByte << zeroIndex);
			dstArray[i] = (byte) (newByte & 0X7F);
			tmpByte = oneByte;
			zeroIndex++;
		}
		return dstArray;
	}

	public static byte getShiftByte(int shiftLength) {
		switch (shiftLength) {
		case 1:
			return 0X7F;
		case 2:
			return 0X3F;
		case 3:
			return 0X1F;
		case 4:
			return 0X0F;
		case 5:
			return 0X07;
		case 6:
			return 0X03;
		case 7:
			return 0X01;
		case 8:
			return 0X00;
		default:
			return 0X00;
		}
	}

	public static byte[] decrypt(byte[] encryptedArray) {
		byte[] dstArray = new byte[decryptLength(encryptedArray.length)];

		int dstIndex = dstArray.length - 1;
		int zeroIndex = 0;
		for (int i = encryptedArray.length - 1; i >= 0; i--) {

			if (i == 0) {
				if (dstIndex >= 0) {
					dstArray[dstIndex] = (byte) (encryptedArray[i] >> zeroIndex);
				}
				break;
			}

			byte leftByte = encryptedArray[i - 1];
			byte rightByte = encryptedArray[i];

			if (zeroIndex == 7) {
				zeroIndex = 0;
				continue;
			}

			dstArray[dstIndex--] = (byte) ((leftByte << (7 - zeroIndex)) | (rightByte >> zeroIndex));
			zeroIndex++;
		}
		if (dstArray[0] == 0) {
			dstArray = Arrays.copyOfRange(dstArray, 1, dstArray.length);
		}
		return dstArray;
	}

	public static byte[] decrypt(byte[] encryptedArray, int startIndex, int length) {
		byte[] dstArray = new byte[decryptLength(length)];

		int dstIndex = dstArray.length - 1;
		int zeroIndex = 0;
		for (int i = startIndex + length - 1; i >= startIndex; i--) {

			if (i == 0) {
				if (dstIndex >= 0) {
					dstArray[dstIndex] = (byte) (encryptedArray[i] >> zeroIndex);
				}
				break;
			}

			byte leftByte = encryptedArray[i - 1];
			byte rightByte = encryptedArray[i];

			if (zeroIndex == 7) {
				zeroIndex = 0;
				continue;
			}

			dstArray[dstIndex--] = (byte) ((leftByte << (7 - zeroIndex)) | (rightByte >> zeroIndex));
			zeroIndex++;
		}
		if (dstArray[0] == 0) {
			dstArray = Arrays.copyOfRange(dstArray, 1, dstArray.length);
		}
		return dstArray;
	}

	public static int outLength(int initLength) {
		int newLength = initLength * 8 / 7;
		return initLength % 7 == 0 ? newLength : newLength + 1;
	}

	public static int decryptLength(int initLength) {
		int newLength = initLength * 7 / 8;
		return initLength % 8 == 0 ? newLength : newLength + 1;
	}

	public static void main(String[] args) {
		// byte[] sourceArray = new byte[] { 65, -55, 'b', -78, 'x', '&', 'p', '/', 6, 90, -120, 125, 87, 12, 56, -59,
		// 90,
		// 81, -53 };
		// // byte[] sourceArray = new byte[] { -4, 4, -86, '#' };
		// logger.info("before,byte={}", sourceArray);
		// StringBuilder sb = new StringBuilder();
		// for (byte b : sourceArray) {
		// byte[] retBytes = getBooleanArray(b);
		// sb.append(Arrays.toString(retBytes));
		// }
		// logger.info("ret={}", sb.toString());
		// byte[] afterBytes = DataEncryption.encrypt((byte) 10, sourceArray);
		// logger.info("after,byte={}", afterBytes);
		// StringBuilder sb2 = new StringBuilder();
		// for (byte b : afterBytes) {
		// byte[] retBytes = getBooleanArray(b);
		// sb2.append(Arrays.toString(retBytes));
		// }
		// logger.info("ret2={}", sb2.toString());
		//
		// byte[] sourceBytes = DataEncryption.decrypt(afterBytes);
		// logger.info("decrypt,byte={}", sourceBytes);
		cmp();
	}

	public static void cmp() {
		for (int i = 0; i <= 10000; i++) {
			byte[] randBytes = RandomUtils.nextBytes(RandomUtils.nextInt(1, 4000));
			if (randBytes[0] == 0) {
				continue;
			}
			// byte[] sourceArray = new byte[] { (byte) i, -55, 'b', -78, 'x', '&', 'p', '/', 109 };
			byte[] afterBytes = DataEncryption.encrypt((byte) 10, randBytes, 0, randBytes.length);
			byte[] decryptBytes = DataEncryption.decrypt(afterBytes, 0, afterBytes.length);
			boolean isEqual = byteEquals(randBytes, decryptBytes);
			if (!isEqual) {
				logger.info("source,byte={}", randBytes);
				printBytes(randBytes);
				logger.info("afterBytes,byte={}", afterBytes);
				printBytes(afterBytes);
				logger.info("decryptBytes,byte={}", decryptBytes);
				printBytes(decryptBytes);
				logger.error("fail!");
			}
		}
	}

	public static void printBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			byte[] retBytes = getBooleanArray(b);
			sb.append(Arrays.toString(retBytes));
		}
		logger.info("ret={}", sb.toString());
	}

	/**
	 * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
	 */
	public static byte[] getBooleanArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 7; i >= 0; i--) {
			array[i] = (byte) (b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}

	public static boolean byteEquals(byte[] srcByte, byte[] endByte) {
		boolean ret = false;
		if (srcByte.length != endByte.length) {
			return ret;
		}
		for (int i = 0; i < srcByte.length; i++) {
			byte b1 = srcByte[i];
			byte b2 = endByte[i];
			if (b1 != b2) {
				return ret;
			}
		}
		return true;
	}

	/**
	 * 把byte转为字符串的bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

}
