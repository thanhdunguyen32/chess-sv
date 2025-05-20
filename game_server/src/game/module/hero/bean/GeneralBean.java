package game.module.hero.bean;

import java.util.List;

public class GeneralBean {

    private Long uuid;

    private Integer playerId;

    private Integer templateId;

    private Integer level;
    private Integer pclass;
    private Integer star;
    private Integer occu;
    private Integer power;
    private List<Integer> equip;
    private Integer treasure;
    private List<Integer> skill;
    private Integer hp;
    private Integer atk;
    private Integer def;
    private Integer mdef;
    private Float atktime;
    private Integer range;
    private Integer msp;
    private Integer pasp;
    private Integer pcri;
    private Integer pcrid;
    private Integer pmdex;
    private Integer pdam;
    private Integer php;
    private Integer patk;
    private Integer pdef;
    private Integer pmdef;
    private Integer ppbs;
    private Integer pmbs;
    private Integer pefc;
    private Integer ppthr;
    private Integer patkdam;
    private Integer pskidam;
    private Integer pckatk;
    private Integer pmthr;
    private Integer pdex;
    private Integer pmsatk;
    private Integer pmps;
    private Integer pcd;
    private GeneralExclusive exclusive;
    private List<Integer> talent;

    @Override
    public String toString() {
        return "GeneralBean{" +
                "uuid=" + uuid +
                ", playerId=" + playerId +
                ", templateId=" + templateId +
                ", level=" + level +
                ", pclass=" + pclass +
                ", star=" + star +
                ", occu=" + occu +
                ", power=" + power +
                ", equip=" + equip +
                ", treasure=" + treasure +
                ", skill=" + skill +
                ", hp=" + hp +
                ", atk=" + atk +
                ", def=" + def +
                ", mdef=" + mdef +
                ", atktime=" + atktime +
                ", range=" + range +
                ", msp=" + msp +
                ", pasp=" + pasp +
                ", pcri=" + pcri +
                ", pcrid=" + pcrid +
                ", pmdex=" + pmdex +
                ", pdam=" + pdam +
                ", php=" + php +
                ", patk=" + patk +
                ", pdef=" + pdef +
                ", pmdef=" + pmdef +
                ", ppbs=" + ppbs +
                ", pmbs=" + pmbs +
                ", pefc=" + pefc +
                ", ppthr=" + ppthr +
                ", patkdam=" + patkdam +
                ", pskidam=" + pskidam +
                ", pckatk=" + pckatk +
                ", pmthr=" + pmthr +
                ", pdex=" + pdex +
                ", pmsatk=" + pmsatk +
                ", pmps=" + pmps +
                ", pcd=" + pcd +
                ", exclusive=" + exclusive +
                ", talent=" + talent +
                '}';
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPclass() {
        return pclass;
    }

    public void setPclass(Integer pclass) {
        this.pclass = pclass;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getOccu() {
        return occu;
    }

    public void setOccu(Integer occu) {
        this.occu = occu;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public List<Integer> getEquip() {
        return equip;
    }

    public void setEquip(List<Integer> equip) {
        this.equip = equip;
    }

    public Integer getTreasure() {
        return treasure;
    }

    public void setTreasure(Integer treasure) {
        this.treasure = treasure;
    }

    public List<Integer> getSkill() {
        return skill;
    }

    public void setSkill(List<Integer> skill) {
        this.skill = skill;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getMdef() {
        return mdef;
    }

    public void setMdef(Integer mdef) {
        this.mdef = mdef;
    }

    public Float getAtktime() {
        return atktime;
    }

    public void setAtktime(Float atktime) {
        this.atktime = atktime;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getMsp() {
        return msp;
    }

    public void setMsp(Integer msp) {
        this.msp = msp;
    }

    public Integer getPasp() {
        return pasp;
    }

    public void setPasp(Integer pasp) {
        this.pasp = pasp;
    }

    public Integer getPcri() {
        return pcri;
    }

    public void setPcri(Integer pcri) {
        this.pcri = pcri;
    }

    public Integer getPcrid() {
        return pcrid;
    }

    public void setPcrid(Integer pcrid) {
        this.pcrid = pcrid;
    }

    public Integer getPmdex() {
        return pmdex;
    }

    public void setPmdex(Integer pmdex) {
        this.pmdex = pmdex;
    }

    public Integer getPdam() {
        return pdam;
    }

    public void setPdam(Integer pdam) {
        this.pdam = pdam;
    }

    public Integer getPhp() {
        return php;
    }

    public void setPhp(Integer php) {
        this.php = php;
    }

    public Integer getPatk() {
        return patk;
    }

    public void setPatk(Integer patk) {
        this.patk = patk;
    }

    public Integer getPdef() {
        return pdef;
    }

    public void setPdef(Integer pdef) {
        this.pdef = pdef;
    }

    public Integer getPpbs() {
        return ppbs;
    }

    public void setPpbs(Integer ppbs) {
        this.ppbs = ppbs;
    }

    public Integer getPmbs() {
        return pmbs;
    }

    public void setPmbs(Integer pmbs) {
        this.pmbs = pmbs;
    }

    public Integer getPefc() {
        return pefc;
    }

    public void setPefc(Integer pefc) {
        this.pefc = pefc;
    }

    public Integer getPpthr() {
        return ppthr;
    }

    public void setPpthr(Integer ppthr) {
        this.ppthr = ppthr;
    }

    public Integer getPatkdam() {
        return patkdam;
    }

    public void setPatkdam(Integer patkdam) {
        this.patkdam = patkdam;
    }

    public Integer getPskidam() {
        return pskidam;
    }

    public void setPskidam(Integer pskidam) {
        this.pskidam = pskidam;
    }

    public Integer getPckatk() {
        return pckatk;
    }

    public void setPckatk(Integer pckatk) {
        this.pckatk = pckatk;
    }

    public Integer getPmthr() {
        return pmthr;
    }

    public void setPmthr(Integer pmthr) {
        this.pmthr = pmthr;
    }

    public Integer getPdex() {
        return pdex;
    }

    public void setPdex(Integer pdex) {
        this.pdex = pdex;
    }

    public Integer getPmsatk() {
        return pmsatk;
    }

    public void setPmsatk(Integer pmsatk) {
        this.pmsatk = pmsatk;
    }

    public GeneralExclusive getExclusive() {
        return exclusive;
    }

    public void setExclusive(GeneralExclusive exclusive) {
        this.exclusive = exclusive;
    }

    public Integer getPmdef() {
        return pmdef;
    }

    public void setPmdef(Integer pmdef) {
        this.pmdef = pmdef;
    }

    public Integer getPmps() {
        return pmps;
    }

    public void setPmps(Integer pmps) {
        this.pmps = pmps;
    }

    public Integer getPcd() {
        return pcd;
    }

    public void setPcd(Integer pcd) {
        this.pcd = pcd;
    }

    public List<Integer> getTalent() {
        return talent;
    }

    public void setTalent(List<Integer> talent) {
        this.talent = talent;
    }
}
