package cn.hxh.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Test {
	public static String getDomobSign(String appkey, String mac, String macmd5, String ifa, String ifamd5,
			String sign_key) throws NoSuchAlgorithmException {
		char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		String s = appkey + "," + mac + "," + macmd5 + "," + ifa + "," + ifamd5 + "," + sign_key;
		byte[] strTemp = s.getBytes();
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char[] str = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[(k++)] = HEX_DIGITS[(byte0 >>> 4 & 0xF)];
			str[(k++)] = HEX_DIGITS[(byte0 & 0xF)];
		}

		return new String(str);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(getDomobSign("531266294", "7C:AB:A3:D6:E7:81", "", "511F798756E2F5423A5BFED5E4C52CB5A6DC",
				"0fa2689dd43aa03af5fb0f13a268e803", "123456"));
	}
}