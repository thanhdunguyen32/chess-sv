package game.module.template;

import java.util.Arrays;

import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "s_building_conf")
public class BuildingConfTemplate {

	private Integer id;

	private String desc;

	private Integer buildingType;

	private Integer level;

	private Integer upTime;

	private Integer roleLv;

	private int[] upNeedBuilding;

	private int[] upNeedSkill;

	private int[][] upNeedResource;

	private int[] resourceOut;

	private int[][] capacity;

	private int[] initButton;

	private int[] clickButton;

	private int[] openButton;

	private String mode;

	private int[][] fixCost;

	private Integer buildingPng;

	private Integer upNeedCamp;

	private int[][] namePos;

	private int[][] viewPos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(Integer buildingType) {
		this.buildingType = buildingType;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getUpTime() {
		return upTime;
	}

	public void setUpTime(Integer upTime) {
		this.upTime = upTime;
	}

	public Integer getRoleLv() {
		return roleLv;
	}

	public void setRoleLv(Integer roleLv) {
		this.roleLv = roleLv;
	}

	public int[] getUpNeedBuilding() {
		return upNeedBuilding;
	}

	public void setUpNeedBuilding(int[] upNeedBuilding) {
		this.upNeedBuilding = upNeedBuilding;
	}

	public int[] getUpNeedSkill() {
		return upNeedSkill;
	}

	public void setUpNeedSkill(int[] upNeedSkill) {
		this.upNeedSkill = upNeedSkill;
	}

	public int[][] getUpNeedResource() {
		return upNeedResource;
	}

	public void setUpNeedResource(int[][] upNeedResource) {
		this.upNeedResource = upNeedResource;
	}

	public int[] getResourceOut() {
		return resourceOut;
	}

	public void setResourceOut(int[] resourceOut) {
		this.resourceOut = resourceOut;
	}

	public int[][] getCapacity() {
		return capacity;
	}

	public void setCapacity(int[][] capacity) {
		this.capacity = capacity;
	}

	public int[] getInitButton() {
		return initButton;
	}

	public void setInitButton(int[] initButton) {
		this.initButton = initButton;
	}

	public int[] getClickButton() {
		return clickButton;
	}

	public void setClickButton(int[] clickButton) {
		this.clickButton = clickButton;
	}

	public int[] getOpenButton() {
		return openButton;
	}

	public void setOpenButton(int[] openButton) {
		this.openButton = openButton;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int[][] getFixCost() {
		return fixCost;
	}

	public void setFixCost(int[][] fixCost) {
		this.fixCost = fixCost;
	}

	public Integer getBuildingPng() {
		return buildingPng;
	}

	public void setBuildingPng(Integer buildingPng) {
		this.buildingPng = buildingPng;
	}

	public Integer getUpNeedCamp() {
		return upNeedCamp;
	}

	public void setUpNeedCamp(Integer upNeedCamp) {
		this.upNeedCamp = upNeedCamp;
	}

	public int[][] getNamePos() {
		return namePos;
	}

	public void setNamePos(int[][] namePos) {
		this.namePos = namePos;
	}

	public int[][] getViewPos() {
		return viewPos;
	}

	public void setViewPos(int[][] viewPos) {
		this.viewPos = viewPos;
	}

	@Override
	public String toString() {
		return "BuildingConfTemplate [id=" + id + ", desc=" + desc + ", buildingType=" + buildingType + ", level="
				+ level + ", upTime=" + upTime + ", roleLv=" + roleLv + ", upNeedBuilding="
				+ Arrays.toString(upNeedBuilding) + ", upNeedSkill=" + Arrays.toString(upNeedSkill)
				+ ", upNeedResource=" + Arrays.toString(upNeedResource) + ", resourceOut="
				+ Arrays.toString(resourceOut) + ", capacity=" + Arrays.toString(capacity) + ", initButton="
				+ Arrays.toString(initButton) + ", clickButton=" + Arrays.toString(clickButton) + ", openButton="
				+ Arrays.toString(openButton) + ", mode=" + mode + ", fixCost=" + Arrays.toString(fixCost)
				+ ", buildingPng=" + buildingPng + ", upNeedCamp=" + upNeedCamp + ", namePos="
				+ Arrays.toString(namePos) + ", viewPos=" + Arrays.toString(viewPos) + "]";
	}

}
