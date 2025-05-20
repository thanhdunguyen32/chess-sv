package leji.protocol;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorJava {

    private static Logger logger = LoggerFactory.getLogger(GeneratorJava.class);

    private static Map<Integer, Integer> REQ_ALL_PROTOCOL_ID_MAP = new HashMap<>();

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
        Map<String, Object> messageMap = new HashMap<>();
        for (File jsonFile : filenames) {
            String fileName = jsonFile.getName();
            fileName = StringUtils.remove(fileName, ".json");
            logger.info("fileName={}", upperCase(fileName));
            Map<String, Object> templateMap = mapper.readValue(jsonFile, Map.class);
            messageMap.putAll(templateMap);
        }
        buildBaseBean(protocolPath, "base", messageMap);
        for (File jsonFile : filenames) {
            String fileName = jsonFile.getName();
            fileName = StringUtils.remove(fileName, ".json");
            logger.info("fileName={}", upperCase(fileName));
            Map<String, Object> templateMap = mapper.readValue(jsonFile, Map.class);
            messageMap.putAll(templateMap);
            buildMessage(protocolPath, fileName, templateMap);
        }
    }

    private static boolean isLoggOpen(Map<String, Object> aObj) {
        if (aObj == null) {
            return false;
        }
        if (aObj.get("is_req") != null && (boolean) aObj.get("is_req")) {
            return true;
        }
        if (aObj.get("is_resp") != null && (boolean) aObj.get("is_resp")) {
            return true;
        }

        return false;
    }

    private static boolean isOpenByteBuf(Map<String, Object> templateMap) {
        for (Object obj : templateMap.values()) {
            if (obj.toString().contains("bytearray")) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasArray(Map<String, Object> templateMap) {
        for (Object obj : templateMap.values()) {
            if (obj.toString().contains("[]")) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasMap(Map<String, Object> templateMap) {
        for (Object obj : templateMap.values()) {
            if (obj.toString().contains("{}")) {
                return true;
            }
        }
        return false;
    }

    private static void buildMessage(String protocolRootPath, String fileName, Map<String, Object> templateMap) throws IOException {
        fileName = upperCase(fileName);
        StringBuilder sb = new StringBuilder();
        sb.append("package ws;\n");
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import io.netty.buffer.ByteBufAllocator;\n");
        if (isOpenByteBuf(templateMap)) {
            sb.append("import io.netty.buffer.ByteBuf;\n");
        }
        if (hasArray(templateMap)) {
            sb.append("import java.util.List;\n");
        }
        if (hasMap(templateMap)) {
            sb.append("import java.util.Map;\n");
        }
        sb.append("import lion.netty4.message.MyRequestMessage;\n");
        sb.append("import lion.netty4.message.MySendToMessage;\n");
        //add base bean import
        for (Map.Entry<String, Object> aEntry : templateMap.entrySet()) {
            String aCalssName = aEntry.getKey();
            Map<String, Object> aObj = (Map<String, Object>) aEntry.getValue();
            //base bean
            if (aObj.get("is_req") == null && aObj.get("is_resp") == null) {
                sb.append("import ws.WsMessageBase." + aCalssName + ";\n");
            }
        }
        sb.append("\n");
        sb.append("public final class WsMessage").append(fileName).append("{\n");

        for (Map.Entry<String, Object> aEntry : templateMap.entrySet()) {
            String aCalssName = aEntry.getKey();
            Map<String, Object> aObj = (Map<String, Object>) aEntry.getValue();
            replaceReservedFields(aObj);
            //只处理c2s s2c bean
            if (aObj.get("is_req") == null && aObj.get("is_resp") == null) {
                continue;
            }
            sb.append("\tpublic static final class ").append(aCalssName).append("{\n");
            if (isLoggOpen(aObj)) {
                sb.append("\t\tprivate static final Logger logger = LoggerFactory.getLogger(").append(aCalssName).append(".class);\n");
            }
            List<Map<String, String>> fieldsObj = (List<Map<String, String>>) aObj.get("fields");
            // 字段
            if (fieldsObj != null && fieldsObj.size() > 0) {
                for (Map<String, String> aFileConfig : fieldsObj) {
                    aFileConfig.remove("desc");
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String fieldName = bEntry.getKey();
                        String fieldType = bEntry.getValue();
                        String javaTypeName = getJavaTypeName(fieldType);
                        if (fieldType.startsWith("[]") && aObj.get("is_req") != null) {
                            fieldType = fieldType.substring(2);
                            String primitiveTypeName = getJavaTypeName(fieldType);
                            sb.append("\t\tpublic ").append(primitiveTypeName + "[]").append(" ").append(fieldName).append(";\n");
                        } else {
                            sb.append("\t\tpublic ").append(javaTypeName).append(" ").append(fieldName).append(";\n");
                        }
                        // sb.append("\t\tpublic " + javaTypeName + " get" +
                        // upperCase(fieldName) + "(){\n");
                        // sb.append("\t\t\treturn " + fieldName + ";\n");
                        // sb.append("\t\t}\n");
                    }
                }
            }
            // 构造函数
            if (aObj.get("is_req") == null || !(boolean) aObj.get("is_req")) {
                boolean isFieldRaw = true;
                if (fieldsObj != null && fieldsObj.size() > 0) {
                    List<Pair<String, String>> fieldPairList = new ArrayList<>();
                    for (Map<String, String> aFileConfig : fieldsObj) {
                        aFileConfig.remove("desc");
                        for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                            String fieldType = bEntry.getValue();
                            fieldPairList.add(new StringPair(bEntry.getKey(), fieldType));
                            isFieldRaw = isFieldRaw(fieldType);
                            if (!isFieldRaw) {
                                break;
                            }
                        }
                        if (!isFieldRaw) {
                            break;
                        }
                    }
                    if (isFieldRaw) {
                        sb.append("\t\tpublic " + aCalssName + "(" + getFiendsStr(fieldPairList) + "){\n");
                        for (Pair<String, String> bEntry : fieldPairList) {
                            String fieldName = bEntry.getLeft();
//							String fieldType = bEntry.getRight();
                            sb.append("\t\t\t" + fieldName + "=p" + fieldName + ";\n");
                        }
                        sb.append("\t\t}\n");
                        sb.append("\t\tpublic " + aCalssName + "(){}\n");
                    }
                }
            }
            //生成toString方法
            if (fieldsObj != null && fieldsObj.size() > 0) {
                sb.append("\t\t@Override\n");
                sb.append("\t\tpublic String toString() {\n");
                sb.append("\t\t\treturn \"" + aCalssName + " [");
                for (Map<String, String> aFileConfig : fieldsObj) {
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String fieldName = bEntry.getKey();
                        String fieldType = bEntry.getValue();
                        if (fieldType.startsWith("[]") && aObj.get("is_req") != null) {
                            sb.append(fieldName + "=\"+java.util.Arrays.toString(" + fieldName + ")+\",");
                        } else {
                            sb.append(fieldName + "=\"+" + fieldName + "+\",");
                        }
                    }
                }
                sb.append("]\";\n");
                sb.append("\t\t}\n");
            }
            // 请求函数处理方法
            if (aObj.get("is_req") != null && (boolean) aObj.get("is_req")) {
                int msgCode = (int) aObj.get("id");
                if (REQ_ALL_PROTOCOL_ID_MAP.containsKey(msgCode)) {
                    System.err.println("msgCode:" + msgCode + " repeat!!!! please check...");
                    throw new RuntimeErrorException(null, "msgCode:" + msgCode + " repeat!!!!");
                }
                REQ_ALL_PROTOCOL_ID_MAP.put(msgCode, msgCode);
                sb.append("\t\tpublic static final int id = " + msgCode + ";\n");
                if (fieldsObj != null && fieldsObj.size() > 0) {
                    sb.append("\n");
                    sb.append("\t\tpublic static " + aCalssName + " parse(MyRequestMessage request){\n");
                    sb.append("\t\t\t" + aCalssName + " retObj = new " + aCalssName + "();\n");
                    // 读取字段
                    sb.append("\t\t\ttry{\n");
                    for (Map<String, String> aFileConfig : fieldsObj) {
                        aFileConfig.remove("desc");
                        for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                            String fieldName = bEntry.getKey();
                            String fieldType = bEntry.getValue();
                            sb.append(getReadStr(templateMap, fieldName, fieldType, true, "i"));
                        }
                    }
                    sb.append("\t\t\t}catch(Exception e){logger.error(\"bulid protocol error!\",e);}\n");
                    sb.append("\t\t\treturn retObj;\n");
                    sb.append("\t\t}\n");
                }
            }
            // 返回函数处理
            if (aObj.get("is_resp") != null && (boolean) aObj.get("is_resp")) {
                int msgCode = (int) aObj.get("id");
                sb.append("\t\tpublic static final int msgCode = " + msgCode + ";\n");
                sb.append("\t\tpublic MySendToMessage build(ByteBufAllocator alloc){\n");
                sb.append("\t\t\ttry{\n");
                sb.append("\t\t\tMySendToMessage retMsg = new MySendToMessage(alloc, " + msgCode + ");\n");
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
                sb.append("\t\t\treturn retMsg;\n");
                sb.append("\t\t\t}catch(Exception e){logger.error(\"bulid protocol error!\",e);return null;}\n");
                sb.append("\t\t}\n");
            }
            sb.append("\t}\n");
        }
        sb.append("}\n");
        // 创建文件对象
        File fileText = new File(protocolRootPath + "java/WsMessage" + fileName + ".java");
        // 向文件写入对象写入信息
        FileWriter fileWriter = new FileWriter(fileText);

        // 写文件
        fileWriter.write(sb.toString());
        // 关闭
        fileWriter.close();
        // FileUtils.write(new File("E:\\protocols\\java\\WsMessage" + fileName
        // + ".java"), sb.toString(),
        // Charset.forName("UTF-8"));
    }

    private static void buildBaseBean(String protocolRootPath, String fileName, Map<String, Object> templateMap) throws IOException {
        fileName = upperCase(fileName);
        StringBuilder sb = new StringBuilder();
        sb.append("package ws;\n");
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import io.netty.buffer.ByteBufAllocator;\n");
        if (isOpenByteBuf(templateMap)) {
            sb.append("import io.netty.buffer.ByteBuf;\n");
        }
        if (hasArray(templateMap)) {
            sb.append("import java.util.List;\n");
        }
        if (hasMap(templateMap)) {
            sb.append("import java.util.Map;\n");
        }
        sb.append("import lion.netty4.message.MyRequestMessage;\n");
        sb.append("import lion.netty4.message.MySendToMessage;\n");
        sb.append("public final class WsMessage").append(fileName).append("{\n");

        for (Map.Entry<String, Object> aEntry : templateMap.entrySet()) {
            String aCalssName = aEntry.getKey();
            Map<String, Object> aObj = (Map<String, Object>) aEntry.getValue();
            replaceReservedFields(aObj);
            //只处理base bean
            if (aObj.get("is_req") != null || aObj.get("is_resp") != null) {
                continue;
            }
            sb.append("\tpublic static final class ").append(aCalssName).append("{\n");
            if (isLoggOpen(aObj)) {
                sb.append("\t\tprivate static final Logger logger = LoggerFactory.getLogger(").append(aCalssName).append(".class);\n");
            }
            List<Map<String, String>> fieldsObj = (List<Map<String, String>>) aObj.get("fields");
            // 字段
            if (fieldsObj != null && fieldsObj.size() > 0) {
                for (Map<String, String> aFileConfig : fieldsObj) {
                    aFileConfig.remove("desc");
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String fieldName = bEntry.getKey();
                        String fieldType = bEntry.getValue();
                        String javaTypeName = getJavaTypeName(fieldType);
                        if (fieldType.startsWith("[]") && aObj.get("is_req") != null) {
                            fieldType = fieldType.substring(2);
                            String primitiveTypeName = getJavaTypeName(fieldType);
                            sb.append("\t\tpublic ").append(primitiveTypeName + "[]").append(" ").append(fieldName).append(";\n");
                        } else {
                            sb.append("\t\tpublic ").append(javaTypeName).append(" ").append(fieldName).append(";\n");
                        }
                    }
                }
            }
            // 构造函数
            if (aObj.get("is_req") == null || !(boolean) aObj.get("is_req")) {
                boolean isFieldRaw = true;
                if (fieldsObj != null && fieldsObj.size() > 0) {
                    List<Pair<String, String>> fieldPairList = new ArrayList<>();
                    for (Map<String, String> aFileConfig : fieldsObj) {
                        aFileConfig.remove("desc");
                        for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                            String fieldType = bEntry.getValue();
                            fieldPairList.add(new StringPair(bEntry.getKey(), fieldType));
                            isFieldRaw = isFieldRaw(fieldType);
                            if (!isFieldRaw) {
                                break;
                            }
                        }
                        if (!isFieldRaw) {
                            break;
                        }
                    }
                    if (isFieldRaw) {
                        sb.append("\t\tpublic " + aCalssName + "(" + getFiendsStr(fieldPairList) + "){\n");
                        for (Pair<String, String> bEntry : fieldPairList) {
                            String fieldName = bEntry.getLeft();
//							String fieldType = bEntry.getRight();
                            sb.append("\t\t\tthis." + fieldName + "=p" + fieldName + ";\n");
                        }
                        sb.append("\t\t}\n");
                        sb.append("\t\tpublic " + aCalssName + "(){}\n");
                    }
                }
            }
            //生成toString方法
            if (fieldsObj != null && fieldsObj.size() > 0) {
                sb.append("\t\t@Override\n");
                sb.append("\t\tpublic String toString() {\n");
                sb.append("\t\t\treturn \"" + aCalssName + " [");
                for (Map<String, String> aFileConfig : fieldsObj) {
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String fieldName = bEntry.getKey();
                        String fieldType = bEntry.getValue();
                        if (fieldType.startsWith("[]") && aObj.get("is_req") != null) {
                            sb.append(fieldName + "=\"+java.util.Arrays.toString(" + fieldName + ")+\",");
                        } else {
                            sb.append(fieldName + "=\"+" + fieldName + "+\",");
                        }
                    }
                }
                sb.append("]\";\n");
                sb.append("\t\t}\n");
            }
            sb.append("\t}\n");
        }
        sb.append("}\n");
        // 创建文件对象
        File fileText = new File(protocolRootPath + "java/WsMessage" + fileName + ".java");
        // 向文件写入对象写入信息
        FileWriter fileWriter = new FileWriter(fileText);

        // 写文件
        fileWriter.write(sb.toString());
        // 关闭
        fileWriter.close();
        // FileUtils.write(new File("E:\\protocols\\java\\WsMessage" + fileName
        // + ".java"), sb.toString(),
        // Charset.forName("UTF-8"));
    }

    private static void replaceReservedFields(Map<String, Object> aObj) {
        List<Map<String, String>> fieldsObj = (List<Map<String, String>>) aObj.get("fields");
        if (fieldsObj == null) {
            return;
        }
        for (Map<String, String> aPair : fieldsObj) {
            String classStr = aPair.get("class");
            if (classStr != null) {
                aPair.remove("class");
                aPair.put("pclass", classStr);
            }
        }
    }

    private static String getFiendsStr(List<Pair<String, String>> fieldPairList) {
        String ret = "";
        List<String> fieldsList = new ArrayList<>();
        for (Pair<String, String> aEntry : fieldPairList) {
            String fieldKey = aEntry.getLeft();
            String fieldVal = aEntry.getRight();
            String javaTypeName = getJavaTypeName(fieldVal);
            fieldsList.add(javaTypeName + " p" + fieldKey);
        }
        return StringUtils.join(fieldsList, ",");
    }

    private static boolean isFieldRaw(String fieldType) {
        boolean ret = false;
        if (fieldType.equals("int") || fieldType.equals("byte") || fieldType.equals("long") || fieldType.equals("bool") || fieldType.equals("float") || fieldType.equals("short")
                || fieldType.equals("string")) {
            ret = true;
        }
        return ret;
    }

    private static String getReadStr(Map<String, Object> rootMap, String fieldName, String fieldType, boolean checkExist, String indexKey) {
        String ret = "";
        if (fieldType.equals("int")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readInt();\n";
        } else if (fieldType.equals("byte")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readByte();\n";
        } else if (fieldType.equals("long")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readLong();\n";
        } else if (fieldType.equals("short")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readShort();\n";
        } else if (fieldType.equals("bool")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readBool();\n";
        } else if (fieldType.equals("float")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readFloat();\n";
        } else if (fieldType.equals("string")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readString();\n";
        } else if (fieldType.equals("bytearray")) {
            ret = "\t\t\tretObj." + fieldName + "=request.readByteArray();\n";
        } else if (fieldType.startsWith("[]")) {
            fieldType = fieldType.substring(2);
            String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
            fieldNameNew = StringUtils.replace(fieldNameNew, "()", "");
            fieldNameNew = removeArrayIndex(fieldNameNew);
            ret += "\t\t\tint " + fieldNameNew + "_size = request.readInt();\n";
            // ret += "\t\t\tif(" + fieldNameNew + "_size >0){\n";
            ret += "\t\t\t\tretObj." + fieldName + " = new " + getJavaTypeName(fieldType) + "[" + fieldNameNew + "_size];\n";
            ret += "\t\t\t\tfor(int " + indexKey + "=0;" + indexKey + "<" + fieldNameNew + "_size;" + indexKey + "++){\n";
            ret += "\t\t" + getReadStr(rootMap, fieldName + "[" + indexKey + "]", fieldType, false, fieldName + "_idx");
            ret += "\t\t\t\t}\n";

            // ret += "\t\t\t}\n";
        } else {
            if (checkExist) {
                ret += "\t\t\tboolean " + toJavaField(fieldName) + "_exist = request.readBool();\n";
                ret += "\t\t\tif(" + toJavaField(fieldName) + "_exist){\n";
            }
            ret += "\t\t\t\tretObj." + fieldName + " = new " + fieldType + "();\n";

            Map<String, Object> beanObj = (Map<String, Object>) rootMap.get(fieldType);
            List<Map<String, String>> fieldsObj = (List<Map<String, String>>) beanObj.get("fields");
            // 字段
            if (fieldsObj != null && fieldsObj.size() > 0) {
                for (Map<String, String> aFileConfig : fieldsObj) {
                    aFileConfig.remove("desc");
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String bFieldName = bEntry.getKey();
                        String bFieldType = bEntry.getValue();
                        ret += "\t\t" + getReadStr(rootMap, fieldName + "." + bFieldName, bFieldType, true, bFieldName + "_idx");
                    }
                }
            }
            if (checkExist) {
                ret += "\t\t\t}\n";
            }
        }
        return ret;
    }

    private static String removeArrayIndex(String astr){
        String ret = StringUtils.remove(astr, "[");
        ret = StringUtils.remove(ret, "]");
        return ret;
    }

    private static String toJavaField(String astr){
        String ret = removeArrayIndex(astr);
        return StringUtils.replace(ret, ".","_");
    }

    private static String getWriteStr(Map<String, Object> rootMap, String fieldType, String fieldName, String indexKey, boolean needCheckObj) {
        String ret = "";
        if (fieldType.equals("int")) {
            ret = "\t\t\tretMsg.writeInt(" + fieldName + ");\n";
        } else if (fieldType.equals("byte")) {
            ret = "\t\t\tretMsg.writeByte(" + fieldName + ");\n";
        } else if (fieldType.equals("short")) {
            ret = "\t\t\tretMsg.writeShort(" + fieldName + ");\n";
        } else if (fieldType.equals("long")) {
            ret = "\t\t\tretMsg.writeLong(" + fieldName + ");\n";
        } else if (fieldType.equals("bool")) {
            ret = "\t\t\tretMsg.writeBool(" + fieldName + ");\n";
        } else if (fieldType.equals("float")) {
            ret = "\t\t\tretMsg.writeFloat(" + fieldName + ");\n";
        } else if (fieldType.equals("string") || fieldType.equals("String")) {
            ret = "\t\t\tretMsg.writeString(" + fieldName + ");\n";
        } else if (fieldType.equals("bytearray")) {
            ret = "\t\t\tretMsg.writeByteArray(" + fieldName + ");\n";
        } else if (fieldType.startsWith("[]")) {
            fieldType = fieldType.substring(2);
            ret += "\t\t\tif(" + fieldName + " == null || " + fieldName + ".size() == 0){\n";
            ret += "\t\t\t\tretMsg.writeInt(0);\n";
            ret += "\t\t\t}else{\n";
            ret += "\t\t\t\tretMsg.writeInt(" + fieldName + ".size());\n";

            String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
            fieldNameNew = StringUtils.replace(fieldNameNew, "()", "");
            ret += "\t\t\t\tfor(" + getJavaTypeObj(fieldType) + " " + fieldNameNew + "1 : " + fieldName + "){\n";
            ret += getWriteStr(rootMap, fieldType, fieldNameNew + "1", fieldName + "_idx", false);
            ret += "\t\t\t\t}\n";

            ret += "\t\t\t}\n";
        } else if (fieldType.startsWith("{}")) {
            String mapConfigStr = fieldType.substring(2);
            String[] split = StringUtils.split(mapConfigStr, ":");
            String keyTypeName = split[0];
            String valueTypeName = split[1];
            ret += "\t\t\tif(" + fieldName + " == null || " + fieldName + ".size() == 0){\n";
            ret += "\t\t\t\tretMsg.writeInt(0);\n";
            ret += "\t\t\t}else{\n";
            ret += "\t\t\t\tretMsg.writeInt(" + fieldName + ".size());\n";

            String fieldNameNew = StringUtils.replace(fieldName, ".", "_");
            fieldNameNew = StringUtils.replace(fieldNameNew, "()", "");
            ret += "\t\t\t\tfor(Map.Entry<" + getJavaTypeObj(keyTypeName) + "," + getJavaTypeObj(valueTypeName) + "> " + fieldNameNew + "1 : " + fieldName +
                    ".entrySet()){\n";
            ret += getWriteStr(rootMap, keyTypeName, fieldNameNew + "1.getKey()", fieldName + "_idx", false);
            ret += getWriteStr(rootMap, valueTypeName, fieldNameNew + "1.getValue()", fieldName + "_idx", false);
            ret += "\t\t\t\t}\n";

            ret += "\t\t\t}\n";
        } else {
            if (needCheckObj) {
                ret += "\t\t\tif(" + fieldName + " == null){\n";
                ret += "\t\t\t\tretMsg.writeBool(false);\n";
                ret += "\t\t\t}else{\n";
                ret += "\t\t\t\tretMsg.writeBool(true);\n";
            }
            Map<String, Object> beanObj = (Map<String, Object>) rootMap.get(fieldType);
            if (beanObj == null) {
                throw new RuntimeException("类没有定义:" + fieldType);
            }
            replaceReservedFields(beanObj);
            List<Map<String, String>> fieldsObj = (List<Map<String, String>>) beanObj.get("fields");
            // 字段
            if (fieldsObj != null && fieldsObj.size() > 0) {
                for (Map<String, String> aFileConfig : fieldsObj) {
                    aFileConfig.remove("desc");
                    for (Map.Entry<String, String> bEntry : aFileConfig.entrySet()) {
                        String bFieldName = bEntry.getKey();
                        String bFieldType = bEntry.getValue();
                        ret += "\t\t" + getWriteStr(rootMap, bFieldType, fieldName + "." + bFieldName, bFieldName + "_idx", true);
                    }
                }
            }
            if (needCheckObj) {
                ret += "\t\t\t}\n";
            }
        }
        return ret;
    }

    private static String getJavaTypeName(String fieldType) {
        String ret = "";
        if (fieldType.equals("int")) {
            ret = "int";
        } else if (fieldType.equals("byte")) {
            ret = "byte";
        } else if (fieldType.equals("short")) {
            ret = "short";
        } else if (fieldType.equals("long")) {
            ret = "long";
        } else if (fieldType.equals("bool")) {
            ret = "boolean";
        } else if (fieldType.equals("float")) {
            ret = "float";
        } else if (fieldType.equals("string")) {
            ret = "String";
        } else if (fieldType.equals("bytearray")) {
            ret = "ByteBuf";
        } else if (fieldType.startsWith("[]")) {
            fieldType = fieldType.substring(2);
            String primitiveTypeName = getJavaTypeName(fieldType);
            String javaTypeObj = getJavaTypeObj(primitiveTypeName);
            ret = "List<" + javaTypeObj + ">";
        } else if (fieldType.startsWith("{}")) {
            String mapConfigStr = fieldType.substring(2);
            String[] split = StringUtils.split(mapConfigStr, ":");
            String keyTypeName = getJavaTypeName(split[0]);
            String valueTypeName = getJavaTypeName(split[1]);
            ret = "Map<" + getJavaTypeObj(keyTypeName) + "," + getJavaTypeObj(valueTypeName) + ">";
        } else {
            ret = fieldType;
        }
        return ret;
    }

    public static boolean isCustomObject(String fieldType) {
        return !fieldType.equals("int") && !fieldType.equals("byte") && !fieldType.equals("short") && !fieldType.equals("long") && !fieldType.equals("bool") &&
                !fieldType.equals("float") && !fieldType.equals("string") && !fieldType.startsWith("[]");
    }

    private static String getJavaTypeObj(String primitiveType) {
        switch (primitiveType) {
            case "int":
                return "Integer";
            case "byte":
                return "Byte";
            case "short":
                return "Short";
            case "long":
                return "Long";
            case "bool":
            case "boolean":
                return "Boolean";
            case "float":
                return "Float";
            case "string":
                return "String";
            default:
                if (primitiveType.startsWith("[]")) {
                    primitiveType = primitiveType.substring(2);
                    String javaTypeObj = getJavaTypeObj(primitiveType);
                    primitiveType = "List<" + javaTypeObj + ">";
                }
                break;
        }
        return primitiveType;
    }

    public static String upperCase(String str) {
        byte[] ch = str.getBytes();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (byte) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static final class StringPair extends Pair<String, String> {

        /**
         *
         */
        private static final long serialVersionUID = -2369662918387868995L;

        private String a;

        private String b;

        public StringPair(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String setValue(String value) {
            return null;
        }

        @Override
        public String getLeft() {
            return a;
        }

        @Override
        public String getRight() {
            return b;
        }

    }
}
