package game.module.box.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ItemPair {

	private Integer itemTemplateId;

	private Integer count;

	public ItemPair(Integer itemTemplateId, Integer count) {
		super();
		this.itemTemplateId = itemTemplateId;
		this.count = count;
	}

	public Integer getItemTemplateId() {
		return itemTemplateId;
	}

	public void setItemTemplateId(Integer itemTemplateId) {
		this.itemTemplateId = itemTemplateId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
