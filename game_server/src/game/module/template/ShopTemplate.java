package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "shopDay.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopTemplate {

    @JsonProperty("GSID")
    private Integer GSID;

    @JsonProperty("COIN")
    private Integer COIN;

    @JsonProperty("BUYTIME")
    private Integer BUYTIME;

    @JsonProperty("COUNT")
    private Integer COUNT;

    @JsonProperty("PRICE")
    private Integer PRICE;

    @JsonProperty("DISCOUNT")
    private Integer DISCOUNT;

    public Integer getGSID() {
        return GSID;
    }

    public void setGSID(Integer GSID) {
        this.GSID = GSID;
    }

    public Integer getCOIN() {
        return COIN;
    }

    public void setCOIN(Integer COIN) {
        this.COIN = COIN;
    }

    public Integer getBUYTIME() {
        return BUYTIME;
    }

    public void setBUYTIME(Integer BUYTIME) {
        this.BUYTIME = BUYTIME;
    }

    public Integer getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(Integer COUNT) {
        this.COUNT = COUNT;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
    }

    public Integer getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(Integer DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    @Override
    public String toString() {
        return "ShopTemplate{" +
                "GSID=" + GSID +
                ", COIN=" + COIN +
                ", BUYTIME=" + BUYTIME +
                ", COUNT=" + COUNT +
                ", PRICE=" + PRICE +
                ", DISCOUNT=" + DISCOUNT +
                '}';
    }
}
