package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbHeadFrame.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadFrameTemplate {

    @JsonProperty("FRAMEID")
    private Integer FRAMEID;

    @JsonProperty("NAME")
    private String NAME;

    @JsonProperty("ARRATT")
    private List<AttTemplate> ARRATT;

    @JsonProperty("TIMELIMIT")
    private Integer TIMELIMIT;

    @Override
    public String toString() {
        return "HeadFrameTemplate{" +
                "FRAMEID=" + FRAMEID +
                ", NAME='" + NAME + '\'' +
                ", ARRATT=" + ARRATT +
                ", TIMELIMIT=" + TIMELIMIT +
                '}';
    }

    public Integer getFRAMEID() {
        return FRAMEID;
    }

    public void setFRAMEID(Integer FRAMEID) {
        this.FRAMEID = FRAMEID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public List<AttTemplate> getARRATT() {
        return ARRATT;
    }

    public void setARRATT(List<AttTemplate> ARRATT) {
        this.ARRATT = ARRATT;
    }

    public Integer getTIMELIMIT() {
        return TIMELIMIT;
    }

    public void setTIMELIMIT(Integer TIMELIMIT) {
        this.TIMELIMIT = TIMELIMIT;
    }

    public static final class AttTemplate{

        @JsonProperty("ATT")
        private Integer ATT;

        @JsonProperty("COUNT")
        private Integer COUNT;

        public Integer getATT() {
            return ATT;
        }

        public void setATT(Integer ATT) {
            this.ATT = ATT;
        }

        public Integer getCOUNT() {
            return COUNT;
        }

        public void setCOUNT(Integer COUNT) {
            this.COUNT = COUNT;
        }

        @Override
        public String toString() {
            return "AttTemplate{" +
                    "ATT=" + ATT +
                    ", COUNT=" + COUNT +
                    '}';
        }
    }
}
