//package test.json;
//
//import java.io.IOException;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class JsonTest {
//
//	/**
//	 * @param args
//	 * @throws IOException
//	 * @throws JsonMappingException
//	 * @throws JsonParseException
//	 */
//	public static void main(String[] args) throws JsonParseException, JsonMappingException,
//			IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		String reqMsg = "{\"correct\":\"\u76d8\u7ed5\u5728\u5c71\u95e8\u91cc\u64ce\u5929\u534e\u8868\u67f1\u4e0a\",\"error\":[\"\u56de\u5f52\u897f\u6d77\u9f99\u5bab\uff0c\u7ee7\u7eed\u5f53\u592a\u5b50\",\"\u7ee7\u7eed\u4ee5\u9a6c\u7684\u5f62\u8c61\u966a\u4f34\u5510\u50e7\",\"\u5230\u5929\u5ead\u5fa1\u9a6c\u76d1\u6210\u4e3a\u4f17\u9a6c\u4e4b\u738b\"]}";
//		reqMsg = unicodeDecode(reqMsg);
//		QuizAnswer quizAnswer = mapper.readValue(reqMsg, QuizAnswer.class);
//		System.out.println(quizAnswer.getCorrect());
//		System.out.println(quizAnswer.getError()[2]);
//	}
//
//	public static String unicodeEncode(String strText) {
//		char c;
//		String strRet = "";
//		int intAsc;
//		String strHex;
//		for (int i = 0; i < strText.length(); i++) {
//			c = strText.charAt(i);
//			intAsc = c;
//			if (intAsc > 128) {
//				strHex = Integer.toHexString(intAsc);
//				strRet += "\\u" + strHex;
//			} else {
//				strRet = strRet + c;
//			}
//		}
//		return strRet;
//	}
//
//	public static String unicodeDecode(String strText) {
//		StringBuilder sb = new StringBuilder();
//		int i = 0;
//		char c;
//		while (i < strText.length()) {
//			c = strText.charAt(i);
//			if (c == '\\' && (i + 1) != strText.length() && strText.charAt(i + 1) == 'u') {
//				sb.append((char) Integer.parseInt(strText.substring(i + 2, i + 6), 16));
//				i += 6;
//			} else {
//				sb.append(c);
//				i++;
//			}
//		}
//		return sb.toString();
//	}
//
//}
