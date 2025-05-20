package game.module.affair.bean;

import java.util.List;
import java.util.Map;

public class DbAffairs {

    private List<AffairBean> affairList;

    public List<AffairBean> getAffairList() {
        return affairList;
    }

    public void setAffairList(List<AffairBean> affairList) {
        this.affairList = affairList;
    }

    public String toString() {
        return "DbAffairs{" +
                "affairList=" + affairList +
                '}';
    }
}
