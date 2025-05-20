package leji.protocol;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorJs {

	private static Logger logger = LoggerFactory.getLogger(GeneratorJs.class);

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
		StringBuilder sb = new StringBuilder();
		sb.append("var WsEncoder = require(\"WsEncoder\");\nvar MyProtocols={\n\n");
		ObjectMapper mapper = new ObjectMapper();
		for (File jsonFile : filenames) {
			String fileName = jsonFile.getName();
			fileName = StringUtils.remove(fileName, ".json");
			String bigFileName = upperCase(fileName);
			logger.info("fileName={}", bigFileName);
			Map<String, Object> templateMap = mapper.readValue(jsonFile, Map.class);
			buildMessage(bigFileName, templateMap, sb);
		}
		sb.append("\n}\nmodule.exports = MyProtocols;");
		FileUtils.write(new File(protocolPath+"/js/MyProtocols.js"), sb.toString(), Charset.forName("UTF-8"));
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
				params = StringUtils.isEmpty(params) ? params : "," + params;
				sb.append("send_" + aCalssName + ":function(senderSocket" + params + "){\n");
				sb.append("\tvar myEncoder = WsEncoder.alloc();\n");
				int msgCode = (int) aObj.get("id");
				sb.append("\tmyEncoder.writeInt(" + msgCode + ");\n");
				// 读取字段
				if (fieldsObj != null && fieldsObj.size() > 0) {
					for (Map<String, String> aFileConfig : fieldsObj) {
						aFileConfig.remove("desc");
						for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
							String fieldName = bEntry.getKey();
							String fieldType = bEntry.getValue();
							String writeAFieldStr = getWriteStr(templateMap, fieldType, fieldName, true);
							sb.append(writeAFieldStr);
						}
					}
				}
				sb.append("\tvar rawContent = myEncoder.end();\n");
				sb.append("\tmyEncoder.free();\n");
				sb.append("\tsenderSocket.sendMessage(rawContent);\n},\n\n");
			}
			// 返回函数处理
			if (aObj.get("is_resp") != null && (boolean) aObj.get("is_resp")) {
				int msgCode = (int) aObj.get("id");
				sb.append("get_" + msgCode + ":function(myDecoder){\n");
				sb.append("\tvar retObj = {};\n");
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
				sb.append("\treturn retObj;\n},\n\n");
			}
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
		if (fieldType.equals("int") || fieldType.equals("byte") || fieldType.equals("short") || fieldType.equals("long")
				|| fieldType.equals("bool") || fieldType.equals("float") || fieldType.equals("string")) {
			ret = true;
		}
		return ret;
	}

	private static String getReadStr(Map<String, Object> rootMap, String fieldName, String fieldType,
			boolean checkExist, String indexKey) {
		String ret = "";
		if (fieldType.equals("int")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readInt();\n";
		} else if (fieldType.equals("short")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readShort();\n";
		} else if (fieldType.equals("byte")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readByte();\n";
		} else if (fieldType.equals("long")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readLong();\n";
		} else if (fieldType.equals("bool")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readBool();\n";
		} else if (fieldType.equals("float")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readFloat();\n";
		} else if (fieldType.equals("string")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readString();\n";
		} else if (fieldType.equals("bytearray")) {
			ret = "\tretObj." + fieldName + "=myDecoder.readBuffer();\n";
		} else if (fieldType.startsWith("[]")) {
			fieldType = fieldType.substring(2);
			String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
			fieldNameNew = StringUtils.remove(fieldNameNew, "[");
			fieldNameNew = StringUtils.remove(fieldNameNew, "]");
			ret += "\tretObj." + fieldName + " = [];\n";
			ret += "\tlet " + fieldNameNew + "_size = myDecoder.readInt();\n";
			ret += "\tif(" + fieldNameNew + "_size >0){\n";
			ret += "\t\tfor(var " + indexKey + "=0; "+ indexKey +"<" + fieldNameNew + "_size;"+indexKey+"++){\n";
			ret += "\t\t" + getReadStr(rootMap, fieldName + "[" + indexKey + "]", fieldType, false, fieldName + "_idx");
			ret += "\t\t}\n";

			ret += "\t}\n";
		} else {
			if (checkExist) {
				String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
				fieldNameNew = StringUtils.remove(fieldNameNew, "[");
				fieldNameNew = StringUtils.remove(fieldNameNew, "]");
				ret += "\tlet " + fieldNameNew + "_exist = myDecoder.readBool();\n";
				ret += "\tif(" + fieldNameNew + "_exist == true){\n";
			}
			ret += "\t\tretObj." + fieldName + " = {};\n";

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
				ret += "\t}\n";
			}
		}
		return ret;
	}

	private static String getWriteStr(Map<String, Object> rootMap, String fieldType, String fieldName,
			boolean needCheckObj) {
		String ret = "";
		if (fieldType.equals("int")) {
			ret = "\tmyEncoder.writeInt(p_" + fieldName + ");\n";
		} else if (fieldType.equals("byte")) {
			ret = "\tmyEncoder.writeByte(p_" + fieldName + ");\n";
		} else if (fieldType.equals("long")) {
			ret = "\tmyEncoder.writeLong(p_" + fieldName + ");\n";
		} else if (fieldType.equals("short")) {
			ret = "\tmyEncoder.writeShort(p_" + fieldName + ");\n";
		} else if (fieldType.equals("bool")) {
			ret = "\tmyEncoder.writeBool(p_" + fieldName + ");\n";
		} else if (fieldType.equals("float")) {
			ret = "\tmyEncoder.writeFloat(p_" + fieldName + ");\n";
		} else if (fieldType.equals("string")) {
			ret = "\tmyEncoder.writeString(p_" + fieldName + ");\n";
		} else if (fieldType.equals("bytearray")) {
			ret = "\tmyEncoder.writeBuffer(p_" + fieldName + ");\n";
		} else if (fieldType.startsWith("[]")) {
			fieldType = fieldType.substring(2);
			ret += "\tif(p_" + fieldName + " == null) {\n";
			ret += "\t\tmyEncoder.writeInt(0);\n";
			ret += "\t}else{\n";
			ret += "\t\tmyEncoder.writeInt(p_" + fieldName + ".length);\n";
			String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
			ret += "\t\tp_" + fieldName + ".forEach(function(p_" + fieldNameNew + "_v){\n";
			ret += getWriteStr(rootMap, fieldType, fieldNameNew + "_v", false);
			ret += "\t\t});\n";

			ret += "\t}\n";
		} else {
			if (needCheckObj) {
				ret += "\tif(p_" + fieldName + " == null){\n";
				ret += "\t\tmyEncoder.writeBool(false);\n";
				ret += "\t}else { \n";
				ret += "\t\tmyEncoder.writeBool(true);\n";
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
						ret += "\t\t" + getWriteStr(rootMap, bFieldType, fieldName + "." + bFieldName, true);
					}
				}
			}
			if (needCheckObj) {
				ret += "\t}\n";
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
