package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actCxry.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdCxryTemplate {
    private Integer gnummax;
    private Integer zfmax;
    private List<Integer> wish_generals;

    public Integer getGnummax() {
        return gnummax;
    }

    public void setGnummax(Integer gnummax) {
        this.gnummax = gnummax;
    }

    public Integer getZfmax() {
        return zfmax;
    }

    public void setZfmax(Integer zfmax) {
        this.zfmax = zfmax;
    }

    public List<Integer> getWish_generals() {
        return wish_generals;
    }

    public void setWish_generals(List<Integer> wish_generals) {
        this.wish_generals = wish_generals;
    }

    @Override
    public String toString() {
        return "ZhdCxryTemplate{" +
                "gnummax=" + gnummax +
                ", zfmax=" + zfmax +
                ", wish_generals=" + wish_generals +
                '}';
    }

}
