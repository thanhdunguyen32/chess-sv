package game.module.season.bean;

import java.util.Date;
import java.util.List;

public class BattleSeason {

    private List<Integer> addonVals;

    private Date etime;

    private Integer year;

    private Integer season;

    private List<Integer> pos;

    private String zhdName;

    private Date monthEndTime;

    public List<Integer> getAddonVals() {
        return addonVals;
    }

    public void setAddonVals(List<Integer> addonVals) {
        this.addonVals = addonVals;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public List<Integer> getPos() {
        return pos;
    }

    public void setPos(List<Integer> pos) {
        this.pos = pos;
    }

    public String getZhdName() {
        return zhdName;
    }

    public void setZhdName(String zhdName) {
        this.zhdName = zhdName;
    }

    public Date getMonthEndTime() {
        return monthEndTime;
    }

    public void setMonthEndTime(Date monthEndTime) {
        this.monthEndTime = monthEndTime;
    }
}
