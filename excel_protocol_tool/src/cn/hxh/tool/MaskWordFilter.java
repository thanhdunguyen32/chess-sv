package cn.hxh.tool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class MaskWordFilter {

	public static void main(String[] args) throws IOException {
		File inputFile = new File("c:\\input.txt");
		List<String> filterAlls = FileUtils.readLines(inputFile, Charset.forName("UTF-8"));
		System.out.println("count=" + filterAlls.size());
		Set<String> wordSet = new HashSet<>();
		for (String aStr : filterAlls) {
			wordSet.add(aStr);
		}
		StringBuilder sb = new StringBuilder();
		for (String sStr : wordSet) {
			sb.append(sStr).append("\r\n");
		}
		//System.out.println(sb);
		File file = new File("c:\\name.txt");

		FileUtils.writeStringToFile(file, sb.toString(), Charset.forName("UTF-8"));
	}

}
