package game.module.pay.bean;

import java.util.Set;

public class DbVipPackGet {

    private Set<Integer> vipLevels;

    public Set<Integer> getVipLevels() {
        return vipLevels;
    }

    public void setVipLevels(Set<Integer> vipLevels) {
        this.vipLevels = vipLevels;
    }

    @Override
    public String toString() {
        return "DbVipPackGet{" +
                "vipLevels=" + vipLevels +
                '}';
    }
}
