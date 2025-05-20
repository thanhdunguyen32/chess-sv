package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbGeneralClass.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralClassTemplate {

    @JsonProperty("MAXLV")
    private Integer MAXLV;

    @JsonProperty("UNLOCK")
    private Integer UNLOCK;

    @JsonProperty("ADD")
    private GeneralClassTemplateAdd ADD;

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    public Integer getMAXLV() {
        return MAXLV;
    }

    public void setMAXLV(Integer MAXLV) {
        this.MAXLV = MAXLV;
    }

    public Integer getUNLOCK() {
        return UNLOCK;
    }

    public void setUNLOCK(Integer UNLOCK) {
        this.UNLOCK = UNLOCK;
    }

    public GeneralClassTemplateAdd getADD() {
        return ADD;
    }

    public void setADD(GeneralClassTemplateAdd ADD) {
        this.ADD = ADD;
    }

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    @Override
    public String toString() {
        return "GeneralClassTemplate{" +
                "MAXLV=" + MAXLV +
                ", UNLOCK=" + UNLOCK +
                ", ADD=" + ADD +
                ", ITEMS=" + ITEMS +
                '}';
    }

    public static final class GeneralClassTemplateAdd{

        @JsonProperty("HP")
        private Integer HP;

        @JsonProperty("ATK")
        private Integer ATK;

        @JsonProperty("DEF")
        private Integer DEF;

        @JsonProperty("MDEF")
        private Integer MDEF;

        public Integer getHP() {
            return HP;
        }

        public void setHP(Integer HP) {
            this.HP = HP;
        }

        public Integer getATK() {
            return ATK;
        }

        public void setATK(Integer ATK) {
            this.ATK = ATK;
        }

        public Integer getDEF() {
            return DEF;
        }

        public void setDEF(Integer DEF) {
            this.DEF = DEF;
        }

        public Integer getMDEF() {
            return MDEF;
        }

        public void setMDEF(Integer MDEF) {
            this.MDEF = MDEF;
        }

        @Override
        public String toString() {
            return "GeneralClassTemplateAdd{" +
                    "HP=" + HP +
                    ", ATK=" + ATK +
                    ", DEF=" + DEF +
                    ", MDEF=" + MDEF +
                    '}';
        }
    }

}
