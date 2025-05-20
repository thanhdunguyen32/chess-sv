package game.module.template;

public class AwardTemplate {

	private Integer id;
	
	private Integer type;

	private String award;
	
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "AwardTemplate [id=" + id + ", type=" + type + ", award=" + award + ", desc=" + desc + "]";
	}

}
