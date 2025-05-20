package cn.hxh.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Game1 {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(System.currentTimeMillis());
		String ret = URLDecoder.decode(
				"QUNBQTk0MzNCOTY0OUVBNiM3RUNEMTVFNkY0NzIzQkFEIzc1Y2FmNDMyLWJkYTgtNDBiZC04NmE4%0ALTZlYTVhMGY1MDdjOQ%3D%3D%0A",
				"UTF-8");
		System.out.println(ret);
	}

}
