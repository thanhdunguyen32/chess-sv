package game.module.template;


@ExcelTemplateAnn(file = "SystemModule")
public class SystemModuleTemplate {

	private Integer id;

	private String name;

	private Integer lv;
	
	private String open_stage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public String getOpen_stage() {
		return open_stage;
	}

	public void setOpen_stage(String open_stage) {
		this.open_stage = open_stage;
	}

}
