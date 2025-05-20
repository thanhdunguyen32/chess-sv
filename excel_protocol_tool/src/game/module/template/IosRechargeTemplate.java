package game.module.template;

@ExcelTemplateAnn(file = "IosRecharge")
public class IosRechargeTemplate {

	private String item_id;

	private Integer yuan;

	private Integer product_id;

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public Integer getYuan() {
		return yuan;
	}

	public void setYuan(Integer yuan) {
		this.yuan = yuan;
	}

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	@Override
	public String toString() {
		return "IosRechargeTemplate [item_id=" + item_id + ", yuan=" + yuan + ", product_id=" + product_id + "]";
	}

}
