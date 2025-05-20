package lion.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtils {

    private static Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    static class SingletonHolder {
        static JacksonUtils instance = new JacksonUtils();
    }

    public static JacksonUtils getInstance() {
        return JacksonUtils.SingletonHolder.instance;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return mapper.readValue(content, valueTypeRef);
    }

    public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(content, valueType);
    }

    public String getPrettyJsonStr(Object jsonObj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
    }

    public String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error("JSON serialization failed", e);
            return "{}";
        }
    }


    public static void main(String[] args) {
//        System.out.println("123");
//        ObjectMapper objectMapper = JacksonUtils.getInstance().getObjectMapper();
//        System.out.println(objectMapper);
    }
}
