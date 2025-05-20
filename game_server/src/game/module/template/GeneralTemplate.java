package game.module.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

/**
 * Mẫu tướng - chứa thông tin cơ bản của một vị tướng trong game
 */
@ExcelTemplateAnn(file = "dbGeneral.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralTemplate {

	// ID của tướng
	@JsonProperty("GSID")
	private Integer GSID;

	// Tên tướng (tiếng Trung)
	@JsonProperty("NAME")
	private String NAME;

	// Tên tướng (tiếng Việt)
	@JsonProperty("NAME_VN")
	private String NAME_VN;

	// Icon đại diện
	@JsonProperty("HEADICON")
	private String HEADICON;

	// Model 3D của tướng
	@JsonProperty("MODEL")
	private String MODEL;

	// File âm thanh
	@JsonProperty("VOICE")
	private String VOICE;

	// Phe phái (1: Ngụy, 2: Thục, 3: Ngô)
	@JsonProperty("CAMP")
	private Integer CAMP;

	// Hệ tướng (1: Chiến, 2: Kích, 3: Xạ, 4: Quân, 5: Mưu)
	@JsonProperty("OCCU")
	private Integer OCCU;

	// Số sao
	@JsonProperty("STAR")
	private Integer STAR;

	// Tăng máu theo sao
	@JsonProperty("SHP_ADD")
	private Integer SHP_ADD;

	// Tăng công theo sao
	@JsonProperty("SATK_ADD")
	private Integer SATK_ADD;

	// Cấp độ độc quyền
	@JsonProperty("EXLV")
	private Integer EXLV;

	// Cấp độ tối đa
	@JsonProperty("MAXCLASS")
	private Integer MAXCLASS;

	// Số sao tối đa
	@JsonProperty("MAXSTAR")
	private Integer MAXSTAR;

	// Danh sách kỹ năng
	@JsonProperty("SKILL")
	private List<Integer> SKILL;

	// Thuộc tính cơ bản
	@JsonProperty("PROPERTY")
	private HeroTemplateProperty PROPERTY;

	// Tăng trưởng thuộc tính
	@JsonProperty("GROWTH")
	private HeroTemplateGrowth GROWTH;

	public Integer getGSID() {
		return GSID;
	}

	public void setGSID(Integer GSID) {
		this.GSID = GSID;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String NAME) {
		this.NAME = NAME;
	}

	public String getNAME_VN() {
		return NAME_VN;
	}

	public void setNAME_VN(String NAME_VN) {
		this.NAME_VN = NAME_VN;
	}

	public String getHEADICON() {
		return HEADICON;
	}

	public void setHEADICON(String HEADICON) {
		this.HEADICON = HEADICON;
	}

	public String getMODEL() {
		return MODEL;
	}

	public void setMODEL(String MODEL) {
		this.MODEL = MODEL;
	}

	public String getVOICE() {
		return VOICE;
	}

	public void setVOICE(String VOICE) {
		this.VOICE = VOICE;
	}

	public Integer getCAMP() {
		return CAMP;
	}

	public void setCAMP(Integer CAMP) {
		this.CAMP = CAMP;
	}

	public Integer getOCCU() {
		return OCCU;
	}

	public void setOCCU(Integer OCCU) {
		this.OCCU = OCCU;
	}

	public Integer getSTAR() {
		return STAR;
	}

	public void setSTAR(Integer STAR) {
		this.STAR = STAR;
	}

	public Integer getSHP_ADD() {
		return SHP_ADD;
	}

	public void setSHP_ADD(Integer SHP_ADD) {
		this.SHP_ADD = SHP_ADD;
	}

	public Integer getSATK_ADD() {
		return SATK_ADD;
	}

	public void setSATK_ADD(Integer SATK_ADD) {
		this.SATK_ADD = SATK_ADD;
	}

	public Integer getEXLV() {
		return EXLV;
	}

	public void setEXLV(Integer EXLV) {
		this.EXLV = EXLV;
	}

	public Integer getMAXCLASS() {
		return MAXCLASS;
	}

	public void setMAXCLASS(Integer MAXCLASS) {
		this.MAXCLASS = MAXCLASS;
	}

	public Integer getMAXSTAR() {
		return MAXSTAR;
	}

	public void setMAXSTAR(Integer MAXSTAR) {
		this.MAXSTAR = MAXSTAR;
	}

	public List<Integer> getSKILL() {
		return SKILL;
	}

	public void setSKILL(List<Integer> SKILL) {
		this.SKILL = SKILL;
	}

	public HeroTemplateProperty getPROPERTY() {
		return PROPERTY;
	}

	public void setPROPERTY(HeroTemplateProperty PROPERTY) {
		this.PROPERTY = PROPERTY;
	}

	public HeroTemplateGrowth getGROWTH() {
		return GROWTH;
	}

	public void setGROWTH(HeroTemplateGrowth GROWTH) {
		this.GROWTH = GROWTH;
	}

	@Override
	public String toString() {
		return "HeroTemplate{" +
				"GSID=" + GSID +
				", NAME='" + NAME + '\'' +
				", NAME_VN='" + NAME_VN + '\'' +
				", HEADICON='" + HEADICON + '\'' +
				", MODEL='" + MODEL + '\'' +
				", VOICE='" + VOICE + '\'' +
				", CAMP=" + CAMP +
				", OCCU=" + OCCU +
				", STAR=" + STAR +
				", SHP_ADD=" + SHP_ADD +
				", SATK_ADD=" + SATK_ADD +
				", EXLV=" + EXLV +
				", MAXCLASS=" + MAXCLASS +
				", MAXSTAR=" + MAXSTAR +
				", SKILL=" + SKILL +
				", PROPERTY=" + PROPERTY +
				", GROWTH=" + GROWTH +
				'}';
	}
}
