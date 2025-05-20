package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

/**
 * 任务进度：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "dbNoviceTrain.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoviceTrainTemplate {

	@JsonProperty("NAME")
	private String NAME;

	@JsonProperty("EXID")
	private String EXID;

	@JsonProperty("PMARK")
	private String PMARK;

	@JsonProperty("CNUM")
	private Integer CNUM;

	@JsonProperty("GETMARK")
	private Integer GETMARK;

	@JsonProperty("REWARD")
	private List<RewardTemplateSimple> REWARD;

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String NAME) {
		this.NAME = NAME;
	}

	public String getPMARK() {
		return PMARK;
	}

	public void setPMARK(String PMARK) {
		this.PMARK = PMARK;
	}

	public String getEXID() {
		return EXID;
	}

	public void setEXID(String EXID) {
		this.EXID = EXID;
	}

	public Integer getCNUM() {
		return CNUM;
	}

	@Override
	public String toString() {
		return "MissionDailyTemplate{" +
				"NAME='" + NAME + '\'' +
				", PMARK=" + PMARK +
				", EXID='" + EXID + '\'' +
				", CNUM=" + CNUM +
				", GETMARK=" + GETMARK +
				", REWARD=" + REWARD +
				'}';
	}

	public void setCNUM(Integer CNUM) {
		this.CNUM = CNUM;
	}

	public Integer getGETMARK() {
		return GETMARK;
	}

	public void setGETMARK(Integer GETMARK) {
		this.GETMARK = GETMARK;
	}

	public List<RewardTemplateSimple> getREWARD() {
		return REWARD;
	}

	public void setREWARD(List<RewardTemplateSimple> REWARD) {
		this.REWARD = REWARD;
	}
}
