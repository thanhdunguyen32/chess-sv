package cn.hxh.tool;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class TwLanguageTool {

	private static Set<String> twKeySet = new HashSet<>();

	public static void main(String[] args) throws IOException {
		String[] localKeys = loadKeys("E:\\local.txt");
		String[] twKeys = loadKeys("E:\\tw.txt");
		
		for (String aKey : twKeys) {
			twKeySet.add(aKey);
		}

		for (String local1Key : localKeys) {
			if (!twKeySet.contains(local1Key)) {
				System.out.println(local1Key);
			}
		}
	}

	private static String[] loadKeys(String path) {
		try {
			String localAll = FileUtils.readFileToString(new File(path), "utf-8");
			String[] strList = StringUtils.split(localAll, "\r\n");
			return strList;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
