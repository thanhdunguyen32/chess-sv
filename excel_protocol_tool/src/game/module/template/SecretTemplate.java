package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 秘密基地配置表
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午4:33:55
 */
@ExcelTemplateAnn(file = "SecretMap")
public class SecretTemplate {

	/**
	 * 关卡ID
	 */
	private Integer id;

	private Integer lv;

	private String stage_list;
	
	private String award_chest;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public String getStage_list() {
		return stage_list;
	}

	public void setStage_list(String stage_list) {
		this.stage_list = stage_list;
	}

	public String getAward_chest() {
		return award_chest;
	}

	public void setAward_chest(String award_chest) {
		this.award_chest = award_chest;
	}

}
