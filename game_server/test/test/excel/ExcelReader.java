//package test.excel;
//
//import game.common.excel.ExcelTemplateAnn;
//
//import java.beans.Introspector;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.reflections.Reflections;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ExcelReader {
//
//	private static Logger logger = LoggerFactory.getLogger(ExcelReader.class);
//
//	public static String FILE_PATH_PREFIX = "./template/";
//
//	private Map<String, Integer> columnKeyMap = new HashMap<String, Integer>();
//
//	public List<Object> readExcel(String fileName, Class<?> templateClass) throws IOException, InstantiationException,
//			IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, InvocationTargetException {
//		List<Object> retList = new ArrayList<Object>();
//		// 创建对Excel工作簿文件的引用
//		Workbook workbook2 = new XSSFWorkbook(new FileInputStream(FILE_PATH_PREFIX + fileName));
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
//			Object classInstance = templateClass.newInstance();
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
//			ret = Introspector.decapitalize(ret);
//		}
//		return ret;
//	}
//
//	public static void main(String[] args) {
//		ExcelReader er = new ExcelReader();
//
//		Reflections reflections = new Reflections("test.excel");
//		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ExcelTemplateAnn.class);
//		try {
//			for (Class<?> processorClass : annotated) {
//				ExcelTemplateAnn ann = processorClass.getAnnotation(ExcelTemplateAnn.class);
//				String fileName = ann.file();
//				List<Object> retList = er.readExcel(fileName, processorClass);
//				System.out.println(retList);
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//
//	}
//
//}
