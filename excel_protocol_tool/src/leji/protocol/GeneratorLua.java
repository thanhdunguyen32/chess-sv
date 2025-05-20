package leji.protocol;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorLua {

	private static Logger logger = LoggerFactory.getLogger(GeneratorLua.class);

	public static void generate(String protocolPath) throws JsonParseException, JsonMappingException, IOException {
		File rootDir = new File(protocolPath);
		File[] filenames = rootDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("MyProtocols")) {
					return false;
				}
				return name.contains(".json");
				// return name.contains("bag.js");
			}
		});
		ObjectMapper mapper = new ObjectMapper();
		for (File jsonFile : filenames) {
			StringBuilder sb = new StringBuilder();
			String fileName = jsonFile.getName();
			fileName = StringUtils.remove(fileName, ".json");
			String bigFileName = upperCase(fileName);
			sb.append("local "+bigFileName+"Protocols = {};\n");
			sb.append("\n");
			logger.info("fileName={}", bigFileName);
			Map<String, Object> templateMap = mapper.readValue(jsonFile, Map.class);
			buildMessage(bigFileName, templateMap, sb);
			sb.append("return "+bigFileName+"Protocols;");
			
			// 创建文件对象  
		      File fileText = new File("E:\\protocols\\lua\\"+bigFileName+"Protocols.lua");  
		      // 向文件写入对象写入信息  
		      FileWriter fileWriter = new FileWriter(fileText);  
		  
		      // 写文件        
		      fileWriter.write(sb.toString());
		      // 关闭  
		      fileWriter.close();
			
//			FileUtils.write(new File("E:\\protocols\\lua\\"+bigFileName+"Protocols.lua"), sb.toString(), Charset.forName("UTF-8"));
		}
	}

	private static void buildMessage(String bigFileName, Map<String, Object> templateMap, StringBuilder sb)
			throws IOException {
		for (Map.Entry<String, Object> aEntry : templateMap.entrySet()) {
			String aCalssName = aEntry.getKey();
			Map<String, Object> aObj = (Map<String, Object>) aEntry.getValue();
			List<Map<String, String>> fieldsObj = (List<Map<String, String>>) aObj.get("fields");
			// 请求函数处理方法
			if (aObj.get("is_req") != null && (boolean) aObj.get("is_req")) {
				// 参数列表
				String params = "";
				if (fieldsObj != null && fieldsObj.size() > 0) {
					List<String> fieldPair = new ArrayList<>();
					for (Map<String, String> aFileConfig : fieldsObj) {
						aFileConfig.remove("desc");
						for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
							fieldPair.add(bEntry.getKey());
						}
					}
					params = getFieldsStr(fieldPair);
				}
				params = StringUtils.isEmpty(params) ? params : ","+params;
				sb.append("function "+bigFileName+"Protocols.send_" + aCalssName + "(socket_type" + params + ")\n");
				sb.append("\tlocal buffer = ByteBuffer.New();\n");
				int msgCode = (int) aObj.get("id");
				sb.append("\tbuffer:WriteShort(" + msgCode + ");\n");
				// 读取字段
				if (fieldsObj != null && fieldsObj.size() > 0) {
					for (Map<String, String> aFileConfig : fieldsObj) {
						aFileConfig.remove("desc");
						for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
							String fieldName = bEntry.getKey();
							String fieldType = bEntry.getValue();
							String writeAFieldStr = getWriteStr(templateMap, fieldType, fieldName, "i", true);
							sb.append(writeAFieldStr);
						}
					}
				}
				sb.append("\tif Game.CheckHasSocketConnection(socket_type) then\n");
				sb.append("\t\tnetworkMgr:SendIoMessage(socket_type,buffer);\n");
				sb.append("\telse\n");
				sb.append("\t\tCtrlManager.OpenFloatTipsPanel(\"网络异常，请重试！\");\n");
				sb.append("\tend\n");
				sb.append("end\n");
			}
			// 返回函数处理
			if (aObj.get("is_resp") != null && (boolean) aObj.get("is_resp")) {
				int msgCode = (int) aObj.get("id");
				sb.append("function "+bigFileName+"Protocols.get_" + msgCode + "(buffer)\n");
				sb.append("\tlocal obj = {};\n");
				// 读取字段
				if (fieldsObj != null && fieldsObj.size() > 0) {
					for (Map<String, String> aFileConfig : fieldsObj) {
						aFileConfig.remove("desc");
						for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
							String fieldName = bEntry.getKey();
							String fieldType = bEntry.getValue();
							String writeAFieldStr = getReadStr(templateMap, fieldName, fieldType, true, "i");
							sb.append(writeAFieldStr);
						}
					}
				}
				sb.append("\treturn obj;\n");
				sb.append("end\n");
			}
			sb.append("\n");
		}
	}

	private static String getFieldsStr(Collection<String> fieldPair) {
		String ret = "";
		List<String> fieldsList = new ArrayList<>();
		for (String fieldName : fieldPair) {
			fieldsList.add("p_" + fieldName);
		}
		return StringUtils.join(fieldsList, ",");
	}

	private static boolean isFieldRaw(String fieldType) {
		boolean ret = false;
		if (fieldType.equals("int") || fieldType.equals("byte") || fieldType.equals("short") || fieldType.equals("long") || fieldType.equals("bool") || fieldType.equals("float")
				|| fieldType.equals("string")) {
			ret = true;
		}
		return ret;
	}

	private static String getReadStr(Map<String, Object> rootMap, String fieldName, String fieldType,
			boolean checkExist, String indexKey) {
		String ret = "";
		if (fieldType.equals("int")) {
			ret = "\tobj." + fieldName + "=buffer:ReadInt();\n";
		} else if (fieldType.equals("short")) {
			ret = "\tobj." + fieldName + "=buffer:ReadShort();\n";
		} else if (fieldType.equals("byte")) {
			ret = "\tobj." + fieldName + "=buffer:ReadByte();\n";
		} else if (fieldType.equals("long")) {
			ret = "\tobj." + fieldName + "=buffer:ReadLong();\n";
		} else if (fieldType.equals("bool")) {
			ret = "\tobj." + fieldName + "=buffer:ReadByte();\n";
		} else if (fieldType.equals("float")) {
			ret = "\tobj." + fieldName + "=buffer:ReadFloat();\n";
		} else if (fieldType.equals("string")) {
			ret = "\tobj." + fieldName + "=buffer:ReadString();\n";
		} else if (fieldType.equals("bytearray")) {
			ret = "\tobj." + fieldName + "=buffer:ReadBuffer();\n";
		} else if (fieldType.startsWith("[]")) {
			fieldType = fieldType.substring(2);
			String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
			fieldNameNew = StringUtils.remove(fieldNameNew, "[");
			fieldNameNew = StringUtils.remove(fieldNameNew, "]");
			ret += "\tlocal " + fieldNameNew + "_size = buffer:ReadInt();\n";
			ret += "\tif " + fieldNameNew + "_size >0 then\n";
			ret += "\t\tobj." + fieldName + " = {}\n";
			ret += "\t\tfor " + indexKey + "=1, " + fieldNameNew + "_size do\n";
			ret += "\t\t" + getReadStr(rootMap, fieldName + "[" + indexKey + "]", fieldType, false, fieldName + "_idx");
			ret += "\t\tend\n";

			ret += "\tend\n";
		} else {
			if (checkExist) {
				String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
				fieldNameNew = StringUtils.remove(fieldNameNew, "[");
				fieldNameNew = StringUtils.remove(fieldNameNew, "]");
				ret += "\tlocal " + fieldNameNew + "_exist = buffer:ReadByte();\n";
				ret += "\tif " + fieldNameNew + "_exist > 0 then\n";
			}
			ret += "\t\tobj." + fieldName + " = {};\n";

			Map<String, Object> beanObj = (Map<String, Object>) rootMap.get(fieldType);
			List<Map<String, String>> fieldsObj = (List<Map<String, String>>) beanObj.get("fields");
			// 字段
			if (fieldsObj != null && fieldsObj.size() > 0) {
				for (Map<String, String> aFileConfig : fieldsObj) {
					aFileConfig.remove("desc");
					for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
						String bFieldName = bEntry.getKey();
						String bFieldType = bEntry.getValue();
						ret += "\t\t" + getReadStr(rootMap, fieldName + "." + bFieldName, bFieldType, true,
								bFieldName + "_idx");
					}
				}
			}
			if (checkExist) {
				ret += "\tend\n";
			}
		}
		return ret;
	}

	private static String getWriteStr(Map<String, Object> rootMap, String fieldType, String fieldName, String indexKey,
			boolean needCheckObj) {
		String ret = "";
		if (fieldType.equals("int")) {
			ret = "\tbuffer:WriteInt(p_" + fieldName + ");\n";
		} else if (fieldType.equals("byte")) {
			ret = "\tbuffer:WriteByte(p_" + fieldName + ");\n";
		} else if (fieldType.equals("long")) {
			ret = "\tbuffer:WriteLong(p_" + fieldName + ");\n";
		} else if (fieldType.equals("short")) {
			ret = "\tbuffer:WriteShort(p_" + fieldName + ");\n";
		} else if (fieldType.equals("bool")) {
			ret = "\tbuffer:WriteByte(p_" + fieldName + ");\n";
		} else if (fieldType.equals("float")) {
			ret = "\tbuffer:WriteFloat(p_" + fieldName + ");\n";
		} else if (fieldType.equals("string")) {
			ret = "\tbuffer:WriteString(p_" + fieldName + ");\n";
		} else if (fieldType.equals("bytearray")) {
			ret = "\tbuffer:WriteBuffer(p_" + fieldName + ");\n";
		} else if (fieldType.startsWith("[]")) {
			fieldType = fieldType.substring(2);
			ret += "\tif p_" + fieldName + " == nil then\n";
			ret += "\t\tbuffer:WriteInt(0);\n";
			ret += "\telse \n";
			ret += "\t\tbuffer:WriteInt(#p_" + fieldName + ");\n";
			String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
			ret += "\t\tfor " + indexKey + ",p_" + fieldNameNew + "_v in ipairs(p_" + fieldName + ") do\n";
			ret += getWriteStr(rootMap, fieldType, fieldNameNew + "_v", fieldName + "_idx", false);
			ret += "\t\tend\n";

			ret += "\tend\n";
		} else {
			if (needCheckObj) {
				ret += "\tif p_" + fieldName + " == nil then\n";
				ret += "\t\tbuffer:WriteByte(0);\n";
				ret += "\telse \n";
				ret += "\t\tbuffer:WriteByte(1);\n";
			}
			Map<String, Object> beanObj = (Map<String, Object>) rootMap.get(fieldType);
			List<Map<String, String>> fieldsObj = (List<Map<String, String>>) beanObj.get("fields");
			// 字段
			if (fieldsObj != null && fieldsObj.size() > 0) {
				for (Map<String, String> aFileConfig : fieldsObj) {
					aFileConfig.remove("desc");
					for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
						String bFieldName = bEntry.getKey();
						String bFieldType = bEntry.getValue();
						ret += "\t\t" + getWriteStr(rootMap, bFieldType, fieldName + "." + bFieldName,
								bFieldName + "_idx", true);
					}
				}
			}
			if (needCheckObj) {
				ret += "\tend\n";
			}
		}
		return ret;
	}

	public static String upperCase(String str) {
		byte[] ch = str.getBytes();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (byte) (ch[0] - 32);
		}
		return new String(ch);
	}
}
