//package game.common.excel;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ExcelTemplateReader {
//
//	private static Logger logger = LoggerFactory.getLogger(ExcelTemplateReader.class);
//
//	public static String FILE_PATH_PREFIX = "excel/";
//
//	private Map<String, Integer> columnKeyMap = new HashMap<String, Integer>();
//
//	public <T> List<T> readExcel(String fileName, Class<T> templateClass) throws IOException, InstantiationException,
//			IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, InvocationTargetException {
//		List<T> retList = new ArrayList<T>();
//		// 创建对Excel工作簿文件的引用
//		InputStream is = ExcelTemplateReader.class.getClassLoader().getResourceAsStream(FILE_PATH_PREFIX + fileName);
//		Workbook workbook2 = new XSSFWorkbook(is);
//		// 创建对工作表的引用。
//		// 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
//		Sheet sheet2 = workbook2.getSheet("Sheet1");
//		// 读取左上端单元
//		Row titleRow = sheet2.getRow(0);
//		short rowMaxIndex = titleRow.getLastCellNum();
//		for (int i = 0; i < rowMaxIndex; i++) {
//			Cell aCell = titleRow.getCell(i, Row.RETURN_BLANK_AS_NULL);
//			if (aCell != null) {
//				// System.out.println(aCell.getStringCellValue() + ";");
//				columnKeyMap.put(aCell.getStringCellValue(), i);
//			}
//		}
//		// parse method
//		Method[] methods = templateClass.getDeclaredMethods();
//		List<Method> setMethods = new ArrayList<Method>();
//		for (Method aMethod : methods) {
//			if (aMethod.getName().startsWith("set")) {
//				setMethods.add(aMethod);
//			}
//		}
//
//		int lastRowCount = sheet2.getLastRowNum();
//		for (int j = 1; j <= lastRowCount; j++) {
//			Row dataRow = sheet2.getRow(j);
//			T classInstance = templateClass.newInstance();
//
//			for (Method aMethod : setMethods) {
//				String setMethodName = aMethod.getName();
//				String fieldName = extractFiledName(setMethodName);
//				// column index
//				int columnIndex = columnKeyMap.get(fieldName);
//				Cell valCell = dataRow.getCell(columnIndex);
//				// get value
//				Field thisField = templateClass.getDeclaredField(fieldName);
//				Class<?> fieldType = thisField.getType();
//				Object methodParameter = null;
//				if (fieldType.isAssignableFrom(Integer.class)) {
//					methodParameter = (int) Math.round(valCell.getNumericCellValue());
//				} else if (fieldType.isAssignableFrom(Float.class)) {
//					methodParameter = (float) valCell.getNumericCellValue();
//				} else if (fieldType.isAssignableFrom(String.class)) {
//					methodParameter = valCell.getStringCellValue();
//				}
//				aMethod.invoke(classInstance, methodParameter);
//			}
//			retList.add(classInstance);
//		}
//		return retList;
//	}
//
//	private String extractFiledName(String methodName) {
//		String ret = StringUtils.EMPTY;
//		if (methodName.startsWith("set") || methodName.startsWith("get")) {
//			ret = methodName.substring(3);
//			if (ret == null || ret.length() == 0) {
//				logger.error("method name error!name=" + methodName);
//				return ret;
//			}
//			char chars[] = ret.toCharArray();
//			chars[0] = Character.toLowerCase(chars[0]);
//			ret = new String(chars);
//		}
//		return ret;
//	}
//
//	public <T> List<T> doRead(Class<T> templateClass) {
//		List<T> ret = new ArrayList<T>();
//		ExcelTemplateAnn ann = templateClass.getAnnotation(ExcelTemplateAnn.class);
//		String fileName = ann.file();
//		try {
//			ret = readExcel(fileName, templateClass);
//		} catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException
//				| InvocationTargetException | IOException e) {
//			logger.error("", e);
//		}
//		return ret;
//	}
//
//}
