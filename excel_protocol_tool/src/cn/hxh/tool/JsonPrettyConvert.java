package cn.hxh.tool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

public class JsonPrettyConvert {

	public static void main(String[] args) throws IOException {
		String jsonStr = Files.asCharSource(new File("E:/cocos/projs/sango_card/assets/resources/data/Stages.json"), Charset.forName("UTF-8")).read();
		ObjectMapper mapper = new ObjectMapper();
		Object jsonObj = mapper.readValue(jsonStr, Object.class);
		Files.asCharSink(new File("E:/cocos/projs/sango_card/assets/resources/data/Stages.json"), Charset.forName("UTF-8"))
				.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj));
	}

}
