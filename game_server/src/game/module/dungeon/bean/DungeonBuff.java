package game.module.dungeon.bean;

public class DungeonBuff {
    private Float ppthr;
    private Float pskidam;
    private Float atk;
    private Float pcrid;
    private Float pmthr;
    private Float pcri;

    public DungeonBuff() {
    }

    public DungeonBuff(Float ppthr, Float pskidam, Float atk, Float pcrid, Float pmthr, Float pcri) {
        this.ppthr = ppthr;
        this.pskidam = pskidam;
        this.atk = atk;
        this.pcrid = pcrid;
        this.pmthr = pmthr;
        this.pcri = pcri;
    }

    public Float getPpthr() {
        return ppthr;
    }

    public void setPpthr(Float ppthr) {
        this.ppthr = ppthr;
    }

    public Float getPskidam() {
        return pskidam;
    }

    public void setPskidam(Float pskidam) {
        this.pskidam = pskidam;
    }

    public Float getAtk() {
        return atk;
    }

    public void setAtk(Float atk) {
        this.atk = atk;
    }

    public Float getPcrid() {
        return pcrid;
    }

    public void setPcrid(Float pcrid) {
        this.pcrid = pcrid;
    }

    public Float getPmthr() {
        return pmthr;
    }

    public void setPmthr(Float pmthr) {
        this.pmthr = pmthr;
    }

    public Float getPcri() {
        return pcri;
    }

    public void setPcri(Float pcri) {
        this.pcri = pcri;
    }

    @Override
    public String toString() {
        return "DungeonBuff{" +
                "ppthr=" + ppthr +
                ", pskidam=" + pskidam +
                ", atk=" + atk +
                ", pcrid=" + pcrid +
                ", pmthr=" + pmthr +
                ", pcri=" + pcri +
                '}';
    }
}
