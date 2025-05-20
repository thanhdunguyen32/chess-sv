package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.service.GsService;
import cc.mrbird.febs.system.service.PaymentService;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.lan.GsSyncRequest;
import lion.netty4.message.RequestByteMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private GsService gsService;
    @Autowired
    private GmLanManager gmLanManager;

    @Override
    public String payCallback(final String userId, final String orderid, final int money, int time,
            final String pid, final int serverid) {
        log.info("payCallback,userId={},orderid={},money={},time={},productId={}", userId, orderid, money, time, pid);
        List<GsEntity> gsAll = gsService.getGsList();
        GsEntity gsEntity = null;
        for (GsEntity gs : gsAll) {
            if (gs.getId().equals(serverid)) {
                gsEntity = gs;
                break;
            }
        }
        if (gsEntity == null) {
            throw new RuntimeException("Không tìm thấy máy chủ trò chơi với ID: " + serverid);
        }
        boolean connectRet = gmLanManager.connect(gsEntity);
        if (!connectRet) {
            throw new RuntimeException("Không thể kết nối đến máy chủ trò chơi");
        }
        gmLanManager.buyPaymentItem(gsEntity.getHost(), gsEntity.getPort(), userId, orderid, money, time, pid,
                serverid);
        GsSyncRequest.getInstance().doSend();
        RequestByteMessage retMsg = GsSyncRequest.getInstance().getRetMsg();

        if (retMsg == null) {
            log.error("get info error!gsEntity={}", gsEntity);
            throw new RuntimeException("Kết nối máy chủ trò chơi bất thường!");
        }

        return retMsg.readString();
    }

}
