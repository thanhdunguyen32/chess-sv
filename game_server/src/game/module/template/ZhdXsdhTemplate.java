package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actXsdh.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdXsdhTemplate {
    private Integer grid;
    private List<RewardTemplateSimple> grch;
    private List<RewardTemplateSimple> consume;
    private Integer buytime;

    public Integer getGrid() {
        return grid;
    }

    public void setGrid(Integer grid) {
        this.grid = grid;
    }

    public List<RewardTemplateSimple> getGrch() {
        return grch;
    }

    public void setGrch(List<RewardTemplateSimple> grch) {
        this.grch = grch;
    }

    public List<RewardTemplateSimple> getConsume() {
        return consume;
    }

    public void setConsume(List<RewardTemplateSimple> consume) {
        this.consume = consume;
    }

    public Integer getBuytime() {
        return buytime;
    }

    public void setBuytime(Integer buytime) {
        this.buytime = buytime;
    }

    @Override
    public String toString() {
        return "ZhdXsdhTemplate{" +
                "grid=" + grid +
                ", grch=" + grch +
                ", consume=" + consume +
                ", buytime=" + buytime +
                '}';
    }

}
