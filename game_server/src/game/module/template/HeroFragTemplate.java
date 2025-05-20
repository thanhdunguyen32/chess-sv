package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbGeneralChip.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroFragTemplate {

	private Integer id;

	@JsonProperty("NAME")
	private String NAME;

	@JsonProperty("INTRO")
	private String INTRO;

	@JsonProperty("CTYPE")
	private Integer CTYPE;

	@JsonProperty("QUALITY")
	private Integer QUALITY;

	@JsonProperty("STAR")
	private Integer STAR;

	@JsonProperty("CHIP")
	private Integer CHIP;

	@JsonProperty("GCOND")
	private Object GCOND;

	@JsonProperty("EXID")
	private String EXID;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String NAME) {
		this.NAME = NAME;
	}

	public String getINTRO() {
		return INTRO;
	}

	public void setINTRO(String INTRO) {
		this.INTRO = INTRO;
	}

	public Integer getCTYPE() {
		return CTYPE;
	}

	public void setCTYPE(Integer CTYPE) {
		this.CTYPE = CTYPE;
	}

	public Integer getQUALITY() {
		return QUALITY;
	}

	public void setQUALITY(Integer QUALITY) {
		this.QUALITY = QUALITY;
	}

	public Integer getSTAR() {
		return STAR;
	}

	public void setSTAR(Integer STAR) {
		this.STAR = STAR;
	}

	public Integer getCHIP() {
		return CHIP;
	}

	public void setCHIP(Integer CHIP) {
		this.CHIP = CHIP;
	}

	public Object getGCOND() {
		return GCOND;
	}

	public void setGCOND(Object GCOND) {
		this.GCOND = GCOND;
	}

	public String getEXID() {
		return EXID;
	}

	public void setEXID(String EXID) {
		this.EXID = EXID;
	}

	@Override
	public String toString() {
		return "HeroFragTemplate{" +
				"id=" + id +
				", NAME='" + NAME + '\'' +
				", INTRO='" + INTRO + '\'' +
				", CTYPE=" + CTYPE +
				", QUALITY=" + QUALITY +
				", STAR=" + STAR +
				", CHIP=" + CHIP +
				", GCOND=" + GCOND +
				", EXID='" + EXID + '\'' +
				'}';
	}
}
