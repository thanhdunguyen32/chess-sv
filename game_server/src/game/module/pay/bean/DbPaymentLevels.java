package game.module.pay.bean;

import java.util.Set;

public class DbPaymentLevels {

    private Set<Integer> paymentLevels;

    public Set<Integer> getPaymentLevels() {
        return paymentLevels;
    }

    public void setPaymentLevels(Set<Integer> paymentLevels) {
        this.paymentLevels = paymentLevels;
    }

    @Override
    public String toString() {
        return "DbPaymentLevels{" +
                "paymentLevels=" + paymentLevels +
                '}';
    }
}
