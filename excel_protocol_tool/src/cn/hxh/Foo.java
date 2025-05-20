package cn.hxh;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Map;

public class Foo {

	private Integer id;

	private String name;

	private String address;

	private Map<String,String> tools;

	private int[] jobs;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int[] getJobs() {
		return jobs;
	}

	public void setJobs(int[] jobs) {
		this.jobs = jobs;
	}

	public Map<String, String> getTools() {
		return tools;
	}

	public void setTools(Map<String, String> tools) {
		this.tools = tools;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
