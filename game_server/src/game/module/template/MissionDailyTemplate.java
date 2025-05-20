package game.module.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

/**
 * 任务进度：配置文件类
 * 
 * @author zhangning
 *
 */
@ExcelTemplateAnn(file = "dbMissionDaily.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionDailyTemplate {

	@JsonProperty("NAME")
	private String NAME;

	@JsonProperty("INTRO")
	private String INTRO;

	@JsonProperty("PMARK")
	private String PMARK;

	@JsonProperty("EXID")
	private String EXID;

	@JsonProperty("CNUM")
	private Integer CNUM;

	@JsonProperty("GETMARK")
	private Integer GETMARK;

	@JsonProperty("REWARD")
	private List<RewardTemplateConfig> REWARD;

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
				", INTRO='" + INTRO + '\'' +
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

	public List<RewardTemplateConfig> getREWARD() {
		return REWARD;
	}

	public void setREWARD(List<RewardTemplateConfig> REWARD) {
		this.REWARD = REWARD;
	}
}
