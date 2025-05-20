package game.module.template;

import cc.mrbird.febs.template.ExcelTemplateAnn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "Items")
public class ItemTemplate {

	private Integer id;
	private Integer type;
	private String function;
	private Integer lv;
	private Integer quality;
	private Integer price;
	private Integer force;
	private Float enchantforce;
	private Integer value;
	private Integer power;
	private Integer intelligence;
	private Integer agile;
	private Integer hp;
	private Integer attack;
	private Integer magic;
	private Integer armor;
	private Integer resistance;
	private Integer physicalcrit;
	private Integer magiccrit;
	private Integer hpRecovery;
	private Integer enRecovery;
	private Integer arPenetration;
	private Integer rePenetration;
	private Integer hit;
	private Integer dodge;
	private Integer blood;
	private Float treatment;
	private Float enReduce;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(Integer intelligence) {
		this.intelligence = intelligence;
	}

	public Integer getAgile() {
		return agile;
	}

	public void setAgile(Integer agile) {
		this.agile = agile;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getAttack() {
		return attack;
	}

	public void setAttack(Integer attack) {
		this.attack = attack;
	}

	public Integer getMagic() {
		return magic;
	}

	public void setMagic(Integer magic) {
		this.magic = magic;
	}

	public Integer getArmor() {
		return armor;
	}

	public void setArmor(Integer armor) {
		this.armor = armor;
	}

	public Integer getResistance() {
		return resistance;
	}

	public void setResistance(Integer resistance) {
		this.resistance = resistance;
	}

	public Integer getPhysicalcrit() {
		return physicalcrit;
	}

	public void setPhysicalcrit(Integer physicalcrit) {
		this.physicalcrit = physicalcrit;
	}

	public Integer getMagiccrit() {
		return magiccrit;
	}

	public void setMagiccrit(Integer magiccrit) {
		this.magiccrit = magiccrit;
	}

	public Integer getHpRecovery() {
		return hpRecovery;
	}

	public void setHpRecovery(Integer hpRecovery) {
		this.hpRecovery = hpRecovery;
	}

	public Integer getEnRecovery() {
		return enRecovery;
	}

	public void setEnRecovery(Integer enRecovery) {
		this.enRecovery = enRecovery;
	}

	public Integer getArPenetration() {
		return arPenetration;
	}

	public void setArPenetration(Integer arPenetration) {
		this.arPenetration = arPenetration;
	}

	public Integer getRePenetration() {
		return rePenetration;
	}

	public void setRePenetration(Integer rePenetration) {
		this.rePenetration = rePenetration;
	}

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}

	public Integer getDodge() {
		return dodge;
	}

	public void setDodge(Integer dodge) {
		this.dodge = dodge;
	}

	public Integer getBlood() {
		return blood;
	}

	public void setBlood(Integer blood) {
		this.blood = blood;
	}

	public Float getTreatment() {
		return treatment;
	}

	public void setTreatment(Float treatment) {
		this.treatment = treatment;
	}

	public Float getEnReduce() {
		return enReduce;
	}

	public void setEnReduce(Float enReduce) {
		this.enReduce = enReduce;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getForce() {
		return force;
	}

	public void setForce(Integer force) {
		this.force = force;
	}

	public Float getEnchantforce() {
		return enchantforce;
	}

	public void setEnchantforce(Float enchantforce) {
		this.enchantforce = enchantforce;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
