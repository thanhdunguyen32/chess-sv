package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actDjrw.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdDjrwTemplate {

    private List<ZhdDjrwCheck> chk;
    private Integer cnum;
    private String name;
    private List<RewardTemplateSimple> items;
    private Integer status;

    public List<ZhdDjrwCheck> getChk() {
        return chk;
    }

    public void setChk(List<ZhdDjrwCheck> chk) {
        this.chk = chk;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RewardTemplateSimple> getItems() {
        return items;
    }

    public void setItems(List<RewardTemplateSimple> items) {
        this.items = items;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ZhdDjrwTemplate{" +
                "chk=" + chk +
                ", cnum=" + cnum +
                ", name='" + name + '\'' +
                ", items=" + items +
                ", status=" + status +
                '}';
    }

    public static final class ZhdDjrwCheck{
        @JsonProperty("MARK")
        private Integer MARK;
        @JsonProperty("NUM")
        private Integer NUM;

        public Integer getMARK() {
            return MARK;
        }

        public void setMARK(Integer MARK) {
            this.MARK = MARK;
        }

        public Integer getNUM() {
            return NUM;
        }

        public void setNUM(Integer NUM) {
            this.NUM = NUM;
        }

        @Override
        public String toString() {
            return "ZhdDjrwCheck{" +
                    "MARK=" + MARK +
                    ", NUM=" + NUM +
                    '}';
        }
    }
}
