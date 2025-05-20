package cc.mrbird.febs.system.service;


public interface PaymentService {
    public String payCallback(final String userId, final String orderid, final int money, int time,
                    final String pid, final int serverid);
}


