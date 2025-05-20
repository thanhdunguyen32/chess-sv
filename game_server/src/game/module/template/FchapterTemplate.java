package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbFchapter.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FchapterTemplate {

    @JsonProperty("ID")
    private Integer ID;

    @JsonProperty("POWER")
    private Integer POWER;

    @JsonProperty("MIN")
    private Integer MIN;

    @JsonProperty("BOSSRATE")
    private Integer BOSSRATE;

    @JsonProperty("ITEMS")
    private List<RewardTemplateRange> ITEMS;

    @JsonProperty("DROPS")
    private List<FChapterDrops> DROPS;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getPOWER() {
        return POWER;
    }

    public void setPOWER(Integer POWER) {
        this.POWER = POWER;
    }

    public Integer getMIN() {
        return MIN;
    }

    public void setMIN(Integer MIN) {
        this.MIN = MIN;
    }

    public Integer getBOSSRATE() {
        return BOSSRATE;
    }

    public void setBOSSRATE(Integer BOSSRATE) {
        this.BOSSRATE = BOSSRATE;
    }

    public List<RewardTemplateRange> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateRange> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public List<FChapterDrops> getDROPS() {
        return DROPS;
    }

    public void setDROPS(List<FChapterDrops> DROPS) {
        this.DROPS = DROPS;
    }

    @Override
    public String toString() {
        return "FchapterTemplate{" +
                "ID=" + ID +
                ", POWER=" + POWER +
                ", MIN=" + MIN +
                ", BOSSRATE=" + BOSSRATE +
                ", ITEMS=" + ITEMS +
                ", DROPS=" + DROPS +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class FChapterDrops{

        @JsonProperty("NUM")
        private Integer NUM;

        @JsonProperty("ITEMS")
        private List<RewardTemplateWeight> ITEMS;

        public Integer getNUM() {
            return NUM;
        }

        public void setNUM(Integer NUM) {
            this.NUM = NUM;
        }

        public List<RewardTemplateWeight> getITEMS() {
            return ITEMS;
        }

        public void setITEMS(List<RewardTemplateWeight> ITEMS) {
            this.ITEMS = ITEMS;
        }

        @Override
        public String toString() {
            return "FChapterDrops{" +
                    "NUM=" + NUM +
                    ", ITEMS=" + ITEMS +
                    '}';
        }
    }
}
