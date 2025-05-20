package cn.hxh.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Files;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumDescriptorProto;
import com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.Descriptors.DescriptorValidationException;

public class ProtobufObDump {

	public static void main(String[] args) throws FileNotFoundException, IOException, DescriptorValidationException {
		decodeProto("Base");
		decodeProto("Common");
		decodeProto("Game");
		decodeProto("Http");
		decodeProto("Serialize");
	}

	private static void decodeProto(String pbName)
			throws FileNotFoundException, IOException, DescriptorValidationException {
		DescriptorProtos.FileDescriptorSet descriptorSet = DescriptorProtos.FileDescriptorSet
				.parseFrom(new FileInputStream("D:\\git_slg\\client\\res\\pb\\" + pbName + ".pb"));
//		System.out.println(descriptorSet);
//		Files.write(descriptorSet.toString(), new File("Game.desc"), Charset.forName("UTF-8"));
		for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet.getFileList()) {
			StringBuilder sb = new StringBuilder();
			sb.append("syntax = \"proto2\";\n\n");
			sb.append("package proto;\n");
			for (String aDepend : fdp.getDependencyList()) {
				sb.append("import \"" + aDepend + "\";\n");
			}
			sb.append("option java_outer_classname = \"" + fdp.getOptions().getJavaOuterClassname() + "\";\n");
			sb.append("option optimize_for = SPEED;\n");
			sb.append("\n");
			String fileName = fdp.getName();
			for (DescriptorProto descriptorProto : fdp.getMessageTypeList()) {
				buildMessage(descriptorProto, sb);
			}
			for (EnumDescriptorProto enumDescriptorProto : fdp.getEnumTypeList()) {
				sb.append("enum " + enumDescriptorProto.getName() + "{\n");
				for (EnumValueDescriptorProto enumValueDescriptorProto : enumDescriptorProto.getValueList()) {
					sb.append("\t").append(enumValueDescriptorProto.getName()).append(" = ")
							.append(enumValueDescriptorProto.getNumber()).append(";\n");
				}
				sb.append("}\n\n");
			}
			Files.write(sb.toString(), new File("proto/"+fileName), StandardCharsets.UTF_8);
		}
	}

	private static void buildMessage(DescriptorProto descriptorProto, StringBuilder sb) {
		sb.append("message " + descriptorProto.getName() + "{\n");
		for (FieldDescriptorProto aFieldDesc : descriptorProto.getFieldList()) {
			String labelStr = getLabelString(aFieldDesc.getLabel().getNumber());
			String typeStr = getTypeString(aFieldDesc.getType().getNumber(), aFieldDesc.getTypeName());
			sb.append("\t").append(labelStr).append(" ").append(typeStr).append(" ").append(aFieldDesc.getName())
					.append(" = ").append(aFieldDesc.getNumber()).append(";\n");
		}
		// nested
		for (DescriptorProto nestedProto : descriptorProto.getNestedTypeList()) {
			buildMessage(nestedProto, sb);
		}
		sb.append("}\n\n");
	}

	private static String getLabelString(int labelNumber) {
		String ret = "";
		switch (labelNumber) {
		case Label.LABEL_OPTIONAL_VALUE:
			ret = "optional";
			break;
		case Label.LABEL_REPEATED_VALUE:
			ret = "repeated";
			break;
		case Label.LABEL_REQUIRED_VALUE:
			ret = "required";
			break;
		default:
			break;
		}
		return ret;
	}

	private static String getTypeString(int typeNumber, String typeName) {
		String ret = "";
		switch (typeNumber) {
		case Type.TYPE_BOOL_VALUE:
			ret = "bool";
			break;
		case Type.TYPE_BYTES_VALUE:
			ret = "bytes";
			break;
		case Type.TYPE_DOUBLE_VALUE:
			ret = "double";
			break;
		case Type.TYPE_FLOAT_VALUE:
			ret = "float";
			break;
		case Type.TYPE_INT32_VALUE:
			ret = "int32";
			break;
		case Type.TYPE_INT64_VALUE:
			ret = "int64";
			break;
		case Type.TYPE_MESSAGE_VALUE:
			ret = typeName.substring(1);
			break;
		case Type.TYPE_STRING_VALUE:
			ret = "string";
			break;
		default:
			ret = Type.forNumber(typeNumber).name();
			break;
		}
		return ret;
	}

}
