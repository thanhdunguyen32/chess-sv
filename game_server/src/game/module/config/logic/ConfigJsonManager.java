package game.module.config.logic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import game.module.chat.logic.ChatManager;

public class ConfigJsonManager {
    private static Logger logger = LoggerFactory.getLogger(ConfigJsonManager.class);

    static class SingletonHolder {
        static ConfigJsonManager instance = new ConfigJsonManager();
    }

    public static ConfigJsonManager getInstance() {
        return SingletonHolder.instance;
    }

    public String loadConfigJsons(String[] configFiles) {
        Map<String, String> jsonMap = new HashMap<>();

        for (String filename : configFiles) {
            if (filename.equals("dbGeneralReset.json")) {
                filename = "dbGeneralResetClient.json";
            }
            String content = loadJsonFile(filename + ".json");
            if (content != null) {
                jsonMap.put(filename, content);
            }
        }
        
        return jsonMap.toString();
    }

    public String loadJsonFile(String filename) {
        try {
            if (filename.equals("dbGeneralReset.json")) {
                filename = "dbGeneralResetClient.json";
            }
            // Đọc file JSON từ thư mục /data
            Path filePath = Paths.get("data", filename);
            byte[] bytes = Files.readAllBytes(filePath);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Lỗi khi load file JSON: " + filename, e);
            return null;
        }
    }

    public String loadJsonFilePart(String filename, int part) {
        Map<String, Object> result = new HashMap<>();

        if (filename.equals("dbGeneralReset.json")) {
            filename = "dbGeneralResetClient.json";
        }
        
        try {
            Path filePath = Paths.get("data", filename);
            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            
            // Chia nội dung thành các phần, mỗi phần 5KB
            int partSize = 5 * 1024; // 5KB
            int totalParts = (content.length() + partSize - 1) / partSize;
            
            int start = part * partSize;
            int end = Math.min(start + partSize, content.length());
            
            // Trả về metadata và nội dung phần hiện tại
            result.put("total_parts", totalParts);
            result.put("current_part", part);
            result.put("content", content.substring(start, end));
            
            return new ObjectMapper().writeValueAsString(result);
            
        } catch (IOException e) {
            logger.error("Lỗi đọc file JSON: " + filename, e);
            return null;
        }
    }
}
