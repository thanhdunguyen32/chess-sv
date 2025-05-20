package game.common;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import io.netty.util.CharsetUtil;
import lion.common.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelTemplateParse {

	private static Logger logger = LoggerFactory.getLogger(ExcelTemplateParse.class);

	public static <T> List<T> parse(Class<T> templateClass) {
		Method[] methods = templateClass.getDeclaredMethods();
		Map<String, Method> setMethods = new HashMap<>();
		for (Method aMethod : methods) {
			String setMethodName = aMethod.getName();
			if (setMethodName.startsWith("set")) {
				String fieldName = extractFiledName(setMethodName);
				// put
				setMethods.put(fieldName, aMethod);
			}
		}
		List<T> tempList = new ArrayList<>();
		List<Method> methodList = new ArrayList<>();
		ExcelTemplateAnn ann = templateClass.getAnnotation(ExcelTemplateAnn.class);
		String fileName = ann.file();
		CharSource cs = Files.asCharSource(new File("config/" + fileName + ".csv"), CharsetUtil.UTF_8);
		try {
			int i = 0;
			for (String aLineStr : cs.readLines()) {
				if (i == 0) {
					String[] fieldStrs = StringUtils.split(aLineStr, ",");
					for (String aFiledStr : fieldStrs) {
						String aFieldName = aFiledStr.substring(1, aFiledStr.length() - 1);
						aFieldName = lowerCaseFirstLetter(aFieldName);
						methodList.add(setMethods.get(aFieldName));
//						logger.info("{}",aFieldName);
					}
					i++;
				} else {
					T classInstance = templateClass.getDeclaredConstructor().newInstance();
					int j = 0;
//					logger.info("line:{}",aLineStr);
					List<String> dataStrs = getDataListFromLine(aLineStr);
					for (String aDataStr : dataStrs) {
						Method aMethod = methodList.get(j);
//						logger.info("idx={},method={}",j,aMethod);
						String fieldName = extractFiledName(aMethod.getName());
						Object methodParameter = null;
						if (!aDataStr.equals("nil")) {
							Field thisField = templateClass.getDeclaredField(fieldName);
							Class<?> fieldType = thisField.getType();
//							logger.info("aLineStr={},aDataStr={},fieldName={},fieldType={}",aLineStr,aDataStr,fieldName,fieldType);
							if (fieldType.isAssignableFrom(Integer.class)) {
								methodParameter = Integer.parseInt(aDataStr);
							} else if (fieldType.isAssignableFrom(Long.class)) {
								methodParameter = Long.parseLong(aDataStr);
							} else if (fieldType.isAssignableFrom(Boolean.class)) {
								methodParameter = Boolean.parseBoolean(aDataStr);
							} else if (fieldType.isAssignableFrom(Float.class)) {
								methodParameter = Float.parseFloat(aDataStr);
							} else if (fieldType.isAssignableFrom(String.class)) {
								methodParameter = aDataStr;
							} else {
								methodParameter = JacksonUtils.getInstance().readValue(aDataStr, fieldType);
							}
						}
//						logger.info("fieldName={},aDataStr={}",fieldName, aDataStr);
						aMethod.invoke(classInstance, methodParameter);
						j++;
					}
					tempList.add(classInstance);
				}
			}
			return tempList;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	private static List<String> getDataListFromLine(String aLineStr) {
		List<String> retDatas = new ArrayList<>();
		char[] chars = aLineStr.toCharArray();
		int startDataIndex = 0;
		boolean findStr = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			//双引号被转义
			if (i + 1 < chars.length && c == '\\' && chars[i + 1] == '"') {
				i++;
				continue;
			}
			if (c == '"' && !findStr) {
				startDataIndex = i + 1;
				findStr = true;
			} else if (c == '"' && findStr) {
				String ret = new String(chars, startDataIndex, i - startDataIndex);
				ret = ret.trim();
				// 跳过,
				i++;
				startDataIndex = i + 1;
				findStr = false;
				retDatas.add(ret);
			} else if (c == ',' && !findStr) {
				String ret = new String(chars, startDataIndex, i - startDataIndex);
				ret = ret.trim();
				startDataIndex = i + 1;
				retDatas.add(ret);
			}
		}
		// 结尾处理
		if (chars[chars.length - 1] != '"') {
			String ret = new String(chars, startDataIndex, chars.length - startDataIndex);
			ret = ret.trim();
			retDatas.add(ret);
		}
		return retDatas;
	}

	private static String extractFiledName(String methodName) {
		String ret = StringUtils.EMPTY;
		if (methodName.startsWith("set") || methodName.startsWith("get")) {
			ret = methodName.substring(3);
			if (ret.length() == 0) {
				logger.error("method name error!name=" + methodName);
				return ret;
			}
			ret = lowerCaseFirstLetter(ret);
		}
		return ret;
	}

	private static String lowerCaseFirstLetter(String ret){
		char[] chars = ret.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		ret = new String(chars);
		return ret;
	}

}
