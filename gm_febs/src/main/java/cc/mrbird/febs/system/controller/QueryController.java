package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.service.QueryService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import org.springframework.web.bind.annotation.RequestBody;

import com.wuwenze.poi.ExcelKit;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("query")
public class QueryController extends BaseController {

    @Autowired
    private QueryService queryService;
    @Autowired
    private GmLanManager gmLanManager;

    @GetMapping("daily_stat")
    @RequiresPermissions("query:view")
    public FebsResponse dailyStatView(User user, QueryRequest request) {
        List<DailyStatEntity> dailyStatAll = queryService.getDailyStatAll();
        Map<String, Object> dataTable = getDataTable(dailyStatAll);
        //设置本月总计和上月总计
        Date now = new Date();
        Date lastMonthNow = DateUtils.addMonths(now,-1);
        int thisMonthSum = 0;
        int lastMonthSum = 0;
        for (DailyStatEntity dailyStatEntity : dailyStatAll){
            if(DateUtils.truncatedEquals(now,dailyStatEntity.getStatTime(), Calendar.MONTH)){
                thisMonthSum += dailyStatEntity.getPaySum();
            }else if(DateUtils.truncatedEquals(lastMonthNow,dailyStatEntity.getStatTime(), Calendar.MONTH)){
                lastMonthSum += dailyStatEntity.getPaySum();
            }
        }
        dataTable.put("thisMonthSum",thisMonthSum);
        dataTable.put("lastMonthSum",lastMonthSum);
        return new FebsResponse().success().data(dataTable);
    }

    @GetMapping("dailyStat-excel")
    @RequiresPermissions("query:view")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void dailyStatExcel(HttpServletResponse response) {
        log.info("export daily stat!");
        List<DailyStatEntity> dailyStatAll = queryService.getDailyStatAll();
        ExcelKit.$Export(DailyStatEntity.class, response).downXlsx(dailyStatAll, false);
    }

    @ResponseBody
    @GetMapping("playerAllBase")
    public FebsResponse playerAllBase(@RequestParam(required = false) Integer serverId) {
        try {
            // Nếu không có serverId, lấy server đầu tiên trong danh sách
            if (serverId == null) {
                List<GsEntity> servers = queryService.getAllServer();
                if (servers != null && !servers.isEmpty()) {
                    serverId = servers.get(0).getId();
                }
            }
            
            if (serverId == null) {
                return new FebsResponse().fail().message("Không tìm thấy máy chủ nào");
            }
            
            log.info("Getting player data for server ID: {}", serverId);
            Map<String, Object> data = queryService.getPlayerAllBase(serverId);
            data.put("timestamp", System.currentTimeMillis());
            return new FebsResponse().success().data(data);
            
        } catch (Exception e) {
            log.error("Error getting player data: ", e);
            return new FebsResponse().fail().message(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("all-server")
    public FebsResponse getAllServer() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("serverList", queryService.getAllServer());
            data.put("timestamp", System.currentTimeMillis());
            return new FebsResponse().success().data(data);
        } catch (Exception e) {
            return new FebsResponse().fail().message(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("user-info")
    public FebsResponse getUserInfo(@RequestBody UserInfo.UserInfoRequest body) {
        try {
            log.info("body={}", body.toString());
            Map<String, Object> data = new HashMap<>();

            String userInfo = queryService.getUserByUsernameAndServer(body.getUsername(), body.getServerID());
            log.info("userInfo={}", userInfo);
            
            if (StringUtils.isBlank(userInfo)) {
                return new FebsResponse().fail().message("User info not found");
            }
            
            try {
                // Parse the string directly into a Map
                Map<String, Object> userInfoMap = new HashMap<>();
                
                // Clean up the string first - remove any curly braces
                userInfo = userInfo.replaceAll("[{}]", "").trim();
                String[] pairs = userInfo.split(",\\s*");
                
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        
                        // Convert value to appropriate type
                        if (value.equals("null")) {
                            userInfoMap.put(key, null);
                        } else if (value.matches("\\d+")) {
                            userInfoMap.put(key, Long.parseLong(value));
                        } else if (value.matches("\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d+")) {
                            userInfoMap.put(key, value); // Keep date as string
                        } else {
                            userInfoMap.put(key, value);
                        }
                    }
                }
                
                data.put("userInfo", userInfoMap);
            } catch (Exception e) {
                log.error("Failed to parse user info: {}", userInfo, e);
                return new FebsResponse().fail().message("Invalid user info format");
            }
            
            data.put("timestamp", System.currentTimeMillis());
            return new FebsResponse().success().data(data);
        } catch (Exception e) {
            log.error("Error getting user info", e);
            return new FebsResponse().fail().message(e.getMessage());
        }
    }
    
    @GetMapping("chatContent")
    @RequiresPermissions("query:view")
    public FebsResponse chatContent() {
        log.info("get chat content!");
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!！");
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getChatContent(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                List<ChatEntity> retlist = new ArrayList<ChatEntity>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    int playerId = retMsg.readInt();
                    String name = retMsg.readString();
                    String chatContent = retMsg.readString();
                    long chatTime = retMsg.readLong();
                    ChatEntity aEntity = new ChatEntity();
                    aEntity.setPlayerId(playerId);
                    aEntity.setPlayerName(name);
                    aEntity.setContent(chatContent);
                    aEntity.setChatTime(new Date(chatTime));
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }

    @GetMapping("payLogAll")
    @RequiresPermissions("query:view")
    public FebsResponse payLogAll() {
        log.info("get pay logs all!");
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!！");
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getPayLogAll(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                List<PayLogEntity> retlist = new ArrayList<>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    int playerId = retMsg.readInt();
                    String name = retMsg.readString();
                    String orderId = retMsg.readString();
                    int sumMoney = retMsg.readInt();
                    long payTime = retMsg.readLong();
                    PayLogEntity aEntity = new PayLogEntity();
                    aEntity.setPlayerId(playerId);
                    aEntity.setPlayerName(name);
                    aEntity.setOrderId(orderId);
                    aEntity.setPaySum(sumMoney);
                    aEntity.setPayTime(new Date(payTime));
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }


    @GetMapping("payLogAll-excel")
    @RequiresPermissions("query:view")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void payLogAllExcel(HttpServletResponse response) {
        log.info("export pay logs all!");
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return;
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getPayLogAll(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return;
            } else {
                List<PayLogEntity> retlist = new ArrayList<>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    int playerId = retMsg.readInt();
                    String name = retMsg.readString();
                    String orderId = retMsg.readString();
                    int sumMoney = retMsg.readInt();
                    long payTime = retMsg.readLong();
                    PayLogEntity aEntity = new PayLogEntity();
                    aEntity.setPlayerId(playerId);
                    aEntity.setPlayerName(name);
                    aEntity.setOrderId(orderId);
                    aEntity.setPaySum(sumMoney);
                    aEntity.setPayTime(new Date(payTime));
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                ExcelKit.$Export(PayLogEntity.class, response).downXlsx(retlist, false);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return;
        }
    }

    @GetMapping("dailyTopPay")
    @RequiresPermissions("query:view")
    public FebsResponse dailyTopPay(String query_date) {
        log.info("daily top pay!query={}", query_date);
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!！");
        }
        if (StringUtils.isBlank(query_date)) {
            List<TopPayPlayerEntity> retlist = new ArrayList<TopPayPlayerEntity>();
            Map<String, Object> dataTable = getDataTable(retlist);
            return new FebsResponse().success().data(dataTable);
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getDailyPayLog(gsEntity.getHost(), gsEntity.getPort(), query_date);
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                List<TopPayPlayerEntity> retlist = new ArrayList<>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    int playerId = retMsg.readInt();
                    String name = retMsg.readString();
                    String accountId = retMsg.readString();
                    int sumMoney = retMsg.readInt();
                    TopPayPlayerEntity aEntity = new TopPayPlayerEntity();
                    aEntity.setPlayer_id(playerId);
                    aEntity.setName(name);
                    aEntity.setAccount_id(accountId);
                    aEntity.setSumMoney(sumMoney);
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }

    @GetMapping("logItemGo")
    @RequiresPermissions("query:view")
    public FebsResponse logItemGo(Integer player_id, Integer item_id) {
        log.info("log item go!player={},item={}", player_id, item_id);
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!！");
        }
        if (player_id == null && item_id == null) {
            List<LogItemGoEntity> retlist = new ArrayList<LogItemGoEntity>();
            Map<String, Object> dataTable = getDataTable(retlist);
            return new FebsResponse().success().data(dataTable);
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            if (player_id == null) {
                player_id = 0;
            }
            if (item_id == null) {
                item_id = 0;
            }
            gmLanManager.getItemGoLog(gsEntity.getHost(), gsEntity.getPort(), item_id, player_id);
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                List<LogItemGoEntity> retlist = new ArrayList<>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    LogItemGoEntity aEntity = new LogItemGoEntity();
                    aEntity.setPlayerId(retMsg.readInt());
                    aEntity.setPlayerName(retMsg.readString());
                    aEntity.setModuleType(retMsg.readInt());
                    aEntity.setItemId(retMsg.readInt());
                    aEntity.setChangeValue(retMsg.readInt());
                    aEntity.setCreateTime(new Date(retMsg.readLong()));
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }

    @GetMapping("payRankAll")
    @RequiresPermissions("query:view")
    public FebsResponse payRankAll() {
        log.info("get top pay player rank!");
        //判断gs是否为空
        User user = getCurrentUser();
        if (user.getGsEntity() == null) {
            return new FebsResponse().fail().message("Vui lòng chọn máy chủ trò chơi mặc định!！");
        }
        GsEntity gsEntity = user.getGsEntity();
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (connectRet) {
            gmLanManager.getTopPayPlayers(gsEntity.getHost(), gsEntity.getPort());
            GsSyncRequest.getInstance().doSend();
            RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();
            if (retMsg == null) {
                log.error("get info error!gsEntity={}", gsEntity);
                return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
            } else {
                List<TopPayPlayerEntity> retlist = new ArrayList<TopPayPlayerEntity>();
                int sizeAll = retMsg.readInt();
                for (int i = 0; i < sizeAll; i++) {
                    int playerId = retMsg.readInt();
                    String name = retMsg.readString();
                    String accountId = retMsg.readString();
                    int sumMoney = retMsg.readInt();
                    TopPayPlayerEntity aEntity = new TopPayPlayerEntity();
                    aEntity.setPlayer_id(playerId);
                    aEntity.setName(name);
                    aEntity.setAccount_id(accountId);
                    aEntity.setSumMoney(sumMoney);
                    retlist.add(aEntity);
                }
                GsSyncRequest.getInstance().releaseRetMsg();
                Map<String, Object> dataTable = getDataTable(retlist);
                return new FebsResponse().success().data(dataTable);
            }
        } else {
            log.error("connect error!gsEntity={}", gsEntity);
            return new FebsResponse().fail().message("Kết nối máy chủ trò chơi bất thường!");
        }
    }

}
