package cn.hxh.tool;

import cn.hxh.common.TemplateCollection;
import com.google.common.io.Files;
import game.module.template.ExcelTemplateAnn;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertorMain {

	private static Logger logger = LoggerFactory.getLogger(ConvertorMain.class);

	public static final String EXCEL_FILE_EXTENSION = ".xlsx";

	public static final String BINARY_FILE_EXTENSION = ".pro";

	public static final String FILE_PATH_SAVE_BINARY = "./generated/";

	public void doConvert(String directoryFullPath, JTextArea logMessageArea) {
		FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("game.module");
		List<String> annotated = fastClasspathScanner.scan().getNamesOfClassesWithAnnotation(ExcelTemplateAnn.class);
		try {
			// clean path
			cleanDirectory();
			logMessageArea.append("-----------start--------------\n");
			logMessageArea.paintImmediately(logMessageArea.getBounds());
			logger.info("--------------start------------------");
			for (String aClassName : annotated) {
				Class<?> processorClass = Class.forName(aClassName);
				ExcelTemplateAnn ann = processorClass.getAnnotation(ExcelTemplateAnn.class);
				String fileName = ann.file();
				// 文件是否存在
				File excelFile = new File(directoryFullPath + fileName + EXCEL_FILE_EXTENSION);
				if (!excelFile.exists()) {
					logMessageArea.append("文件不存在:" + excelFile.toString() + "\n");
					logMessageArea.paintImmediately(logMessageArea.getBounds());
					logger.error("文件不存在:" + excelFile.toString() + "\n");
					continue;
				}
				logMessageArea.append("开始处理:" + excelFile.getName() + "\n");
				logMessageArea.paintImmediately(logMessageArea.getBounds());
				logger.info("开始处理:" + excelFile.getName() + "\n");
				try {
					List<?> retList = readExcel(excelFile, processorClass);
					saveBinary(retList, fileName);
				} catch (Exception e) {
					logger.error("", e);
					logMessageArea.append("error:" + e.getMessage() + "\n");
					logMessageArea.paintImmediately(logMessageArea.getBounds());
				}
				logMessageArea.append("处理完成:" + excelFile.getName() + "\n");
				logMessageArea.paintImmediately(logMessageArea.getBounds());
				logger.info("处理完成:" + excelFile.getName() + "\n");
			}
			logMessageArea.append("-----------finish--------------");
			logMessageArea.paintImmediately(logMessageArea.getBounds());
			logger.info("-----------finish--------------");
		} catch (Exception e) {
			logger.error("", e);
			logMessageArea.append("error:" + e.getMessage() + "\n");
			logMessageArea.paintImmediately(logMessageArea.getBounds());
		}
	}

	public void doConvert(String directoryFullPath) {
		FastClasspathScanner fastClasspathScanner = new FastClasspathScanner("game.module");
		List<String> annotated = fastClasspathScanner.scan().getNamesOfClassesWithAnnotation(ExcelTemplateAnn.class);
		try {
			// clean path
			cleanDirectory();
			logger.info("--------------start------------------");
			for (String aClassName : annotated) {
				Class<?> processorClass = Class.forName(aClassName);
				ExcelTemplateAnn ann = processorClass.getAnnotation(ExcelTemplateAnn.class);
				String fileName = ann.file();
				// 文件是否存在
				File excelFile = new File(directoryFullPath + fileName + EXCEL_FILE_EXTENSION);
				if (!excelFile.exists()) {
					logger.error("文件不存在:" + excelFile.toString() + "\n");
					System.exit(2);
					continue;
				}
				logger.info("开始处理:" + excelFile.getName() + "\n");
				try {
					List<?> retList = readExcel(excelFile, processorClass);
					saveBinary(retList, fileName);
				} catch (Exception e) {
					logger.error("", e);
					System.exit(3);
				}
				logger.info("处理完成:" + excelFile.getName() + "\n");
			}
			logger.info("-----------finish--------------");
		} catch (Exception e) {
			logger.error("", e);
			System.exit(4);
		}
	}

	private void saveBinary(List<?> dataList, String fileName) {
		Schema<TemplateCollection> schema = RuntimeSchema.getSchema(TemplateCollection.class);
		LinkedBuffer buffer = LinkedBuffer.allocate(512);

		// ser
		try {
			byte[] protostuff = ProtostuffIOUtil.toByteArray(new TemplateCollection(dataList), schema, buffer);
			Files.write(protostuff, new File(FILE_PATH_SAVE_BINARY + fileName + BINARY_FILE_EXTENSION));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buffer.clear();
		}
	}

	private void cleanDirectory() throws IOException {
		File generatedFilePath = new File(FILE_PATH_SAVE_BINARY);
		if (generatedFilePath.exists()) {
			generatedFilePath.delete();
		}
		generatedFilePath.mkdir();
	}

	private Map<String, Integer> columnKeyMap = new HashMap<String, Integer>();

	public <T> List<T> readExcel(File excelFile, Class<T> templateClass) throws IOException, InstantiationException,
			IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException,
			InvocationTargetException {
		List<T> retList = new ArrayList<T>();
		// 创建对Excel工作簿文件的引用
		InputStream is = new FileInputStream(excelFile);
		Workbook workbook2 = new XSSFWorkbook(is);
		// 创建对工作表的引用。
		// 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
		Sheet sheet2 = workbook2.getSheet("Sheet1");
		// 读取左上端单元
		Row titleRow = sheet2.getRow(1);
		short rowMaxIndex = titleRow.getLastCellNum();
		for (int i = 0; i < rowMaxIndex; i++) {
			Cell aCell = titleRow.getCell(i, Row.RETURN_BLANK_AS_NULL);
			if (aCell != null) {
				// System.out.println(aCell.getStringCellValue() + ";");
				columnKeyMap.put(StringUtils.trim(aCell.getStringCellValue()), i);
			}
		}
		// parse method
		Method[] methods = templateClass.getDeclaredMethods();
		List<Method> setMethods = new ArrayList<Method>();
		for (Method aMethod : methods) {
			if (aMethod.getName().startsWith("set")) {
				setMethods.add(aMethod);
			}
		}

		int lastRowCount = sheet2.getLastRowNum();
		for (int j = 3; j <= lastRowCount; j++) {
			Row dataRow = sheet2.getRow(j);
			if (dataRow == null) {
				continue;
			}
			T classInstance = templateClass.newInstance();

			for (Method aMethod : setMethods) {
				String setMethodName = aMethod.getName();
				String fieldName = extractFiledName(setMethodName);
				// column index
				if (!columnKeyMap.containsKey(fieldName)) {
					logger.error("字段不存在：fieldName={}", fieldName);
				}
				int columnIndex = columnKeyMap.get(fieldName);
				Cell valCell = dataRow.getCell(columnIndex);
				// get value
				Field thisField = templateClass.getDeclaredField(fieldName);
				Class<?> fieldType = thisField.getType();
				Object methodParameter = null;
				if (fieldType.isAssignableFrom(Integer.class)) {
					if (valCell == null) {
						//logger.error("cell null!fieldName={},columnIndex={},row={},lastRowCount={},templateClass={}",
						//		fieldName, columnIndex, j, lastRowCount, templateClass);
						methodParameter = 0;
					} else {
						int cellType = valCell.getCellType();
						switch (cellType) {
						case Cell.CELL_TYPE_STRING:
							methodParameter = (int) Float.parseFloat(valCell.getStringCellValue());
							break;
						default:
							methodParameter = (int) (valCell.getNumericCellValue());
							break;
						}
					}
				} else if (fieldType.isAssignableFrom(Float.class)) {
					if (valCell == null) {
						//logger.error("cell null!fieldName={},columnIndex={},row={},lastRowCount={},templateClass={}",
						//		fieldName, columnIndex, j, lastRowCount, templateClass);
						methodParameter = 0f;
					} else {
						methodParameter = (float) valCell.getNumericCellValue();
					}
				} else if (fieldType.isAssignableFrom(String.class)) {
					if (valCell == null) {
						//logger.error("fieldName={},columnIndex={},row={},lastRowCount={},templateClass={}", fieldName,
						//		columnIndex, j, lastRowCount, templateClass);
						methodParameter = "";
					} else {
						int cellType = valCell.getCellType();
						switch (cellType) {
						case Cell.CELL_TYPE_NUMERIC:
							BigDecimal bg = new BigDecimal(valCell.getNumericCellValue());
							methodParameter = bg.toPlainString();
							break;

						default:
							methodParameter = valCell.getStringCellValue();
							break;
						}
					}
				}
				aMethod.invoke(classInstance, methodParameter);
			}
			retList.add(classInstance);
		}
		return retList;
	}

	private String extractFiledName(String methodName) {
		String ret = StringUtils.EMPTY;
		if (methodName.startsWith("set") || methodName.startsWith("get")) {
			ret = methodName.substring(3);
			if (ret == null || ret.length() == 0) {
				logger.error("method name error!name=" + methodName);
				return ret;
			}
			char chars[] = ret.toCharArray();
			chars[0] = Character.toLowerCase(chars[0]);
			ret = new String(chars);
		}
		return ret;
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			logger.error("参数错误！");
			System.exit(1);
		}
		String directoryFullPath = args[0];
		new ConvertorMain().doConvert(directoryFullPath+ "/");
	}

}
