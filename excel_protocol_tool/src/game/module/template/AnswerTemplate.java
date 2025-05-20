package game.module.template;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@ExcelTemplateAnn(file = "Answer")
public class AnswerTemplate {

	private Integer id;

	private String problem;

	private String select_1;

	private String select_2;

	private String select_3;

	private String select_4;

	private Integer correct;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getSelect_1() {
		return select_1;
	}

	public void setSelect_1(String select_1) {
		this.select_1 = select_1;
	}

	public String getSelect_2() {
		return select_2;
	}

	public void setSelect_2(String select_2) {
		this.select_2 = select_2;
	}

	public String getSelect_3() {
		return select_3;
	}

	public void setSelect_3(String select_3) {
		this.select_3 = select_3;
	}

	public String getSelect_4() {
		return select_4;
	}

	public void setSelect_4(String select_4) {
		this.select_4 = select_4;
	}

	public Integer getCorrect() {
		return correct;
	}

	public void setCorrect(Integer correct) {
		this.correct = correct;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
