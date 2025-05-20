package game.module.template;

import java.util.List;

import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "s_building")
public class BuildingTemplate {

	private Integer buildingId;
	
	private String desc;
	
	private Integer initPos;
	
	private Integer canDestroy;
	
	private List<List<Integer>> canBuildType;
	
	private Integer buildingType;

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getInitPos() {
		return initPos;
	}

	public void setInitPos(Integer initPos) {
		this.initPos = initPos;
	}

	public Integer getCanDestroy() {
		return canDestroy;
	}

	public void setCanDestroy(Integer canDestroy) {
		this.canDestroy = canDestroy;
	}

	public List<List<Integer>> getCanBuildType() {
		return canBuildType;
	}

	public void setCanBuildType(List<List<Integer>> canBuildType) {
		this.canBuildType = canBuildType;
	}

	public Integer getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(Integer buildingType) {
		this.buildingType = buildingType;
	}

	@Override
	public String toString() {
		return "BuildingTemplate [buildingId=" + buildingId + ", desc=" + desc + ", initPos=" + initPos
				+ ", canDestroy=" + canDestroy + ", canBuildType=" + canBuildType + ", buildingType=" + buildingType
				+ "]";
	}
	
}
