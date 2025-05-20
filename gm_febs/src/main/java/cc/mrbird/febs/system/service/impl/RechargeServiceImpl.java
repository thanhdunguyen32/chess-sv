package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.RechargeService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import lion.netty4.message.RequestByteMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@Slf4j
@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private GmLanManager gmLanManager;
    @Autowired
    private GsService gsService;

    @Override
    public Map<String, Object> getRechargeList(User user) {
        Map<String, Object> dataTable = new HashMap<>();

        GsEntity gsEntity = user.getGsEntity();
        if (gsEntity == null) {
            throw new RuntimeException("Vui lòng chọn máy chủ trò chơi mặc định!");
        }

        boolean connectRet = gmLanManager.connect(gsEntity);
        if (!connectRet) {
            throw new RuntimeException("Không thể kết nối đến máy chủ trò chơi");
        }

        gmLanManager.getRechargeList(gsEntity.getHost(), gsEntity.getPort());
        GsSyncRequest.getInstance().doSend();
        RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();

        if (retMsg == null) {
            log.error("get info error!gsEntity={}", gsEntity);
            throw new RuntimeException("Kết nối máy chủ trò chơi bất thường!");
        }

        log.info("get info success!retMsg={}", retMsg.readString());
        return dataTable;
    }

    @Override
    public Map<String, Object> getRechargeListByServer(Integer serverID) {
        Map<String, Object> dataTable = new HashMap<>();
        log.info("getRechargeListByServer called with serverID={}", serverID);

        try {
            List<GsEntity> gsAll = gsService.getGsList();
            GsEntity gsEntity = null;
            for (GsEntity gs : gsAll) {
                if (gs.getId().equals(serverID)) {
                    gsEntity = gs;
                    break;
                }
            }
            if (gsEntity == null) {
                throw new RuntimeException("Không tìm thấy máy chủ trò chơi với ID: " + serverID);
            }

            boolean connectRet = gmLanManager.connect(gsEntity);

            if (!connectRet) {
                log.error("Failed to connect to game server with ID={}", serverID);
                throw new RuntimeException("Không thể kết nối đến máy chủ trò chơi");
            }

            gmLanManager.getRechargeList(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();

            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                throw new RuntimeException("Kết nối máy chủ trò chơi bất thường!");
            }

            String response = retMsg.readString();

            try {
                // Loại bỏ [] ở đầu và cuối
                response = response.substring(1, response.length() - 1);

                // Chuyển đổi dbRecharge={...} thành {"dbRecharge":{...}}
                String jsonStr = response.replace("dbRecharge=", "\"dbRecharge\":");
                JSONObject fullJson = new JSONObject("{" + jsonStr + "}");
                
                log.info("Parsed JSON: {}", fullJson);

                dataTable.put("serverID", serverID);
                dataTable.put("result", fullJson.getJSONObject("dbRecharge").toMap());  // Convert to Map
                dataTable.put("timestamp", System.currentTimeMillis());

            } catch (Exception e) {
                log.error("Error parsing response: ", e);
                throw new RuntimeException("Invalid response format from game server");
            }

        } catch (Exception e) {
            log.error("Error in getRechargeListByServer: ", e);
            throw e;
        }

        return dataTable;
    }
}