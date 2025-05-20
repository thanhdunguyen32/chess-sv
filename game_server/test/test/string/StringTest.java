package test.string;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTest {

	private static Logger logger = LoggerFactory.getLogger(StringTest.class);

	public static void main(String[] args) {
		new StringTest().test4();
	}

	public void test1() {
		String itemCmd = "		item 			12112 	   5	";
		String[] cmdList = StringUtils.split(itemCmd);
		logger.info("{}", cmdList);

		String a1 = "abc";
		String a2 = "abc";
		System.out.println(a1 == a2);
	}

	public void test2() {
		Double val1 = Double.valueOf("2.0100011E7");
		System.out.println(val1.toString());
	}

	public void test3() {
		String x1 = "我是%1$s,送了%2$s一朵花";
		String ret = String.format(x1, "灰机", "a妹子");
		System.out.println(ret);
	}

	public void test4() {
		String[] strs = new String[] { "aa", "bb" };
		logger.info("before,strs={}", Arrays.asList(strs));
		String T = strs[0];
		strs[0] = strs[1];
		strs[1] = T;
		logger.info("after,strs={}", Arrays.asList(strs));
	}

}
