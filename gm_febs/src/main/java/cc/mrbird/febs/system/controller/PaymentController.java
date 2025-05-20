package cc.mrbird.febs.system.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.system.entity.PaymentEntity;
import cc.mrbird.febs.system.service.PaymentService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequestMapping("payment")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @ResponseBody
    @PostMapping("buy")
    public FebsResponse buyPaymentController(@RequestBody PaymentEntity body) {
        Map<String, Object> result = new HashMap<>();
        String log = paymentService.payCallback(body.getUserId(), body.getOrderId(), body.getMoney(), body.getTime(),
                body.getPid(), body.getServerId());
        // result = "success"
        if (log.equals("success")) {
            result.put("success", true);
            result.put("msg", "");
        } else {
            // log = "fail - " + e.getMessage()
            String[] logParts = log.split(" - ");
            result.put("success", false);
            result.put("msg", logParts[1]);
        }

        return new FebsResponse().data(result);
    }

}
