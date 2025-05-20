package game.module.template;

@ExcelTemplateAnn(file = "HeadFrame")
public class HeadFrameTemplate {

	private Integer id;

	private Integer vip;

	private String fragment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

}
