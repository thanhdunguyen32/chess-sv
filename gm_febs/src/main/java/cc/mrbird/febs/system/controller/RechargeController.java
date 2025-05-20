package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.system.service.RechargeService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("recharge")
public class RechargeController extends BaseController {

    @Autowired
    private RechargeService rechargeService;

    @ResponseBody
    @GetMapping("list")
    @RequiresPermissions("recharge:view")
    public FebsResponse rechargeList() {
        try {
            User user = getCurrentUser();
            Map<String, Object> data = rechargeService.getRechargeList(user);
            return new FebsResponse().success().data(data);
        } catch (Exception e) {
            return new FebsResponse().fail().message(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("lists")
    public FebsResponse getRechargeListByServer(@RequestParam Integer serverID) {
        try {
            Map<String, Object> data = rechargeService.getRechargeListByServer(serverID);
            data.put("timestamp", System.currentTimeMillis());
            return new FebsResponse().success().data(data);
        } catch (Exception e) {
            return new FebsResponse().fail().message(e.getMessage());
        }
    }
}