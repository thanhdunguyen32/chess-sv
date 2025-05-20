package cn.hxh.tool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharSource;
import com.google.common.io.Files;

import io.netty.util.CharsetUtil;

public class LuaConfig2Csv {
	
	private static Logger logger = LoggerFactory.getLogger(LuaConfig2Csv.class);

	public static void main(String[] args) throws IOException {
		String luaPath = "D:\\git_slg\\client\\src\\app\\data\\";
		File rootDir = new File(luaPath);
		File[] filenames = rootDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if(name.startsWith("s_first_guide_replay") || name.startsWith("s_gm_data")) {
					return false;
				}
				return name.endsWith(".lua");
			}
		});
		for (File luaFile : filenames) {
			String fileName = luaFile.getName();
			logger.info("fileName={}", fileName);
			fileName = StringUtils.remove(fileName, ".lua");
			CharSource cs = Files.asCharSource(luaFile, CharsetUtil.UTF_8);
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (String aLineStr : cs.readLines()) {
				aLineStr = aLineStr.trim();
				if (i == 1) {
					aLineStr = aLineStr.substring(7, aLineStr.length() - 2);
					sb.append(aLineStr).append("\n");
				} else if (i >= 3 && !StringUtils.isEmpty(aLineStr) && !aLineStr.equals("}")) {
					if (aLineStr.endsWith("},")) {
						aLineStr = aLineStr.substring(1, aLineStr.length() - 2);
					} else if (aLineStr.endsWith("}")){
						aLineStr = aLineStr.substring(1, aLineStr.length() - 1);
					}
					sb.append(aLineStr).append("\n");
				}
				i++;
			}
			String retCsv = sb.toString();
			Files.write(retCsv.substring(0, retCsv.length()-1), new File(fileName+".csv"), StandardCharsets.UTF_8);
		}
	}

}
