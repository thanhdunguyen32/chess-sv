package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbGeneralComp.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralCompTemplate {

    private Integer id;

    @JsonProperty("CONS")
    private List<GeneralCompConds> CONS;

    @JsonProperty("MAIN")
    private Integer MAIN;

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<GeneralCompConds> getCONS() {
        return CONS;
    }

    public void setCONS(List<GeneralCompConds> CONS) {
        this.CONS = CONS;
    }

    public Integer getMAIN() {
        return MAIN;
    }

    public void setMAIN(Integer MAIN) {
        this.MAIN = MAIN;
    }

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    @Override
    public String toString() {
        return "GeneralCompTemplate{" +
                "id=" + id +
                ", CONS=" + CONS +
                ", MAIN=" + MAIN +
                ", ITEMS=" + ITEMS +
                '}';
    }

    public static final class GeneralCompConds {
        @JsonProperty("COND")
        private Object COND;

        @JsonProperty("COUNT")
        private Integer COUNT;

        public Object getCOND() {
            return COND;
        }

        public void setCOND(Object COND) {
            this.COND = COND;
        }

        public Integer getCOUNT() {
            return COUNT;
        }

        public void setCOUNT(Integer COUNT) {
            this.COUNT = COUNT;
        }

        @Override
        public String toString() {
            return "GeneralCompConds{" +
                    "COND=" + COND +
                    ", COUNT=" + COUNT +
                    '}';
        }
    }
}
