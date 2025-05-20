package game.module.template;

@ExcelTemplateAnn(file = "ChestGood")
public class ChestGoodTemplate {

	private Integer id;

	private String begintime;

	private String endtime;

	private String award;

	private String awardshow;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getAwardshow() {
		return awardshow;
	}

	public void setAwardshow(String awardshow) {
		this.awardshow = awardshow;
	}

}
