package game.module.template;

@ExcelTemplateAnn(file = "Novice")
public class NoviceTemplate {

	private Integer id;

	private String parameter;

	private String open_item_reward;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String getOpen_item_reward() {
		return open_item_reward;
	}

	public void setOpen_item_reward(String open_item_reward) {
		this.open_item_reward = open_item_reward;
	}
}
