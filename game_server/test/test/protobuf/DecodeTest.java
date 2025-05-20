package test.protobuf;

import game.module.gm.ProtoMessageGM.C2SGmCmd;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.MessageLite;

public class DecodeTest {

	public static void main(String[] args) {
		Map<Integer, Class<? extends MessageLite>> messageMap = new HashMap<Integer, Class<? extends MessageLite>>();
		messageMap.put(101, C2SGmCmd.class);

		Class<? extends MessageLite> aClass = messageMap.get(101);
		//Object parsedObj = aClass.newInstance().getParserForType().parseFrom(new LiteralByteString(new byte[] {}));
	}

}
