package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "dbExclusiveLv.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExclusiveLvTemplate {

    @JsonProperty("ITEMS")
    private List<RewardTemplateSimple> ITEMS;

    @JsonProperty("LOCK")
    private List<RewardTemplateSimple> LOCK;

    public List<RewardTemplateSimple> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<RewardTemplateSimple> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public List<RewardTemplateSimple> getLOCK() {
        return LOCK;
    }

    public void setLOCK(List<RewardTemplateSimple> LOCK) {
        this.LOCK = LOCK;
    }

    @Override
    public String toString() {
        return "ExclusiveLvTemplate{" +
                "ITEMS=" + ITEMS +
                ", LOCK=" + LOCK +
                '}';
    }
}
