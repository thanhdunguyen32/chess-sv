package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.User;

import java.util.Map;

public interface RechargeService {
    Map<String, Object> getRechargeList(User user);
    Map<String, Object> getRechargeListByServer(Integer serverID);
} 