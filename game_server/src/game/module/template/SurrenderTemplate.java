package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;

@ExcelTemplateAnn(file = "dbSurrender.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurrenderTemplate {

    private Integer chapterId;

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("LOYAL")
    private SurrenderTemplateLoyal LOYAL;

    @JsonProperty("CAST")
    private Integer CAST;

    @JsonProperty("SRENDER")
    private List<RewardTemplateSimple> SRENDER;

    @JsonProperty("KILL")
    private List<RewardTemplateSimple> KILL;


    @Override
    public String toString() {
        return "SurrenderTemplate{" +
                "chapterId=" + chapterId +
                ", GSID=" + GSID +
                ", LOYAL=" + LOYAL +
                ", CAST=" + CAST +
                ", SRENDER=" + SRENDER +
                ", KILL=" + KILL +
                '}';
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getGSID() {
        return GSID;
    }

    public void setGSID(Integer GSID) {
        this.GSID = GSID;
    }

    public SurrenderTemplateLoyal getLOYAL() {
        return LOYAL;
    }

    public void setLOYAL(SurrenderTemplateLoyal LOYAL) {
        this.LOYAL = LOYAL;
    }

    public Integer getCAST() {
        return CAST;
    }

    public void setCAST(Integer CAST) {
        this.CAST = CAST;
    }

    public List<RewardTemplateSimple> getSRENDER() {
        return SRENDER;
    }

    public void setSRENDER(List<RewardTemplateSimple> SRENDER) {
        this.SRENDER = SRENDER;
    }

    public List<RewardTemplateSimple> getKILL() {
        return KILL;
    }

    public void setKILL(List<RewardTemplateSimple> KILL) {
        this.KILL = KILL;
    }

    public static final class SurrenderTemplateLoyal{
        @JsonProperty("BASE")
        private Integer BASE;
        @JsonProperty("RANGE")
        private int[] RANGE;

        public Integer getBASE() {
            return BASE;
        }

        public void setBASE(Integer BASE) {
            this.BASE = BASE;
        }

        public int[] getRANGE() {
            return RANGE;
        }

        public void setRANGE(int[] RANGE) {
            this.RANGE = RANGE;
        }

        @Override
        public String toString() {
            return "SurrenderTemplateLoyal{" +
                    "BASE=" + BASE +
                    ", RANGE=" + Arrays.toString(RANGE) +
                    '}';
        }
    }

}
