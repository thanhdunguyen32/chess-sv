package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "mySpin.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySpineTemplate {

    private List<List<List<Integer>>> drops;

    private List<Integer> rates;

    private Integer addScore;

    private Integer addLuckyStar;

    private Integer freeRefreshHour;

    public List<List<List<Integer>>> getDrops() {
        return drops;
    }

    public void setDrops(List<List<List<Integer>>> drops) {
        this.drops = drops;
    }

    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }

    public Integer getAddScore() {
        return addScore;
    }

    public void setAddScore(Integer addScore) {
        this.addScore = addScore;
    }

    public Integer getAddLuckyStar() {
        return addLuckyStar;
    }

    public void setAddLuckyStar(Integer addLuckyStar) {
        this.addLuckyStar = addLuckyStar;
    }

    public Integer getFreeRefreshHour() {
        return freeRefreshHour;
    }

    public void setFreeRefreshHour(Integer freeRefreshHour) {
        this.freeRefreshHour = freeRefreshHour;
    }

    @Override
    public String toString() {
        return "MySpineTemplate{" +
                "drops=" + drops +
                ", rates=" + rates +
                ", addScore=" + addScore +
                ", addLuckyStar=" + addLuckyStar +
                ", freeRefreshHour=" + freeRefreshHour +
                '}';
    }

}
