package game.module.chapter.bean;

import game.module.hero.bean.GeneralBean;
import game.module.mythical.bean.MythicalAnimal;

import java.util.Map;

public class DbBattleset {
    private MythicalAnimal mythic;
    private Map<Integer, GeneralBean> team;

    public MythicalAnimal getMythic() {
        return mythic;
    }

    public void setMythic(MythicalAnimal mythic) {
        this.mythic = mythic;
    }

    public Map<Integer, GeneralBean> getTeam() {
        return team;
    }

    public void setTeam(Map<Integer, GeneralBean> team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "DbBattleset{" +
                "mythic=" + mythic +
                ", team=" + team +
                '}';
    }
}
