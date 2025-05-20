package cc.mrbird.febs.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class TemplateCollection {

	private List<?> objList;

	public TemplateCollection() {

	}

	public TemplateCollection(List<?> objList) {
		this.objList = objList;
	}

	public <T> List<T> getObjList(Class<T> typeClass) {
		return (List<T>) objList;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
