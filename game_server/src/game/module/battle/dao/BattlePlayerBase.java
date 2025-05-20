package game.module.battle.dao;

public class BattlePlayerBase {

    private int battleIndex;

    private int gsid;

    private int level;

    private int pclass;

    private int pos;

    private int maxhp;

    private int hp;

    private int atk;

    private int def;

    private int mdef;

    private float atktime;

    private int range;

    private int msp;

    private int pcri;

    private int pcrid;

    public int getBattleIndex() {
        return battleIndex;
    }

    public void setBattleIndex(int battleIndex) {
        this.battleIndex = battleIndex;
    }

    public int getPclass() {
        return pclass;
    }

    public void setPclass(int pclass) {
        this.pclass = pclass;
    }

    public int getGsid() {
        return gsid;
    }

    public void setGsid(int gsid) {
        this.gsid = gsid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMdef() {
        return mdef;
    }

    public void setMdef(int mdef) {
        this.mdef = mdef;
    }

    public float getAtktime() {
        return atktime;
    }

    public void setAtktime(float atktime) {
        this.atktime = atktime;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMsp() {
        return msp;
    }

    public void setMsp(int msp) {
        this.msp = msp;
    }

    public int getPcri() {
        return pcri;
    }

    public void setPcri(int pcri) {
        this.pcri = pcri;
    }

    public int getPcrid() {
        return pcrid;
    }

    public void setPcrid(int pcrid) {
        this.pcrid = pcrid;
    }

    public int getMaxhp() {
        return maxhp;
    }

    public void setMaxhp(int maxhp) {
        this.maxhp = maxhp;
    }

    public BattlePlayerBase(int battleIndex, int gsid, int level, int pclass, int pos, int maxhp, int hp, int atk, int def, int mdef, float atktime, int range,
                            int msp, int pcri, int pcrid) {
        this.battleIndex = battleIndex;
        this.gsid = gsid;
        this.level = level;
        this.pclass = pclass;
        this.pos = pos;
        this.maxhp = maxhp;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.mdef = mdef;
        this.atktime = atktime;
        this.range = range;
        this.msp = msp;
        this.pcri = pcri;
        this.pcrid = pcrid;
    }

    @Override
    public String toString() {
        return "BattlePlayer{" +
                "maxhp=" + maxhp +
                ", battleIndex=" + battleIndex +
                ", gsid=" + gsid +
                ", level=" + level +
                ", pclass=" + pclass +
                ", hp=" + hp +
                ", pos=" + pos +
                ", atk=" + atk +
                ", def=" + def +
                ", mdef=" + mdef +
                ", atktime=" + atktime +
                ", range=" + range +
                ", msp=" + msp +
                ", pcri=" + pcri +
                ", pcrid=" + pcrid +
                '}';
    }
}
