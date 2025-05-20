package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

@ExcelTemplateAnn(file = "dbRoleInfo.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleInfoTemplate {

    @JsonProperty("findzone")
    private String findzone;

    @JsonProperty("attzone")
    private String attzone;

    @JsonProperty("attrange")
    private Integer attrange;

    @JsonProperty("attinfo")
    private RoleInfoAttackTemplate attinfo;

    @JsonProperty("sttinfo")
    private RoleInfoAttackTemplate sttinfo;

    public String getFindzone() {
        return findzone;
    }

    public void setFindzone(String findzone) {
        this.findzone = findzone;
    }

    public String getAttzone() {
        return attzone;
    }

    public void setAttzone(String attzone) {
        this.attzone = attzone;
    }

    public Integer getAttrange() {
        return attrange;
    }

    public void setAttrange(Integer attrange) {
        this.attrange = attrange;
    }

    public RoleInfoAttackTemplate getAttinfo() {
        return attinfo;
    }

    public void setAttinfo(RoleInfoAttackTemplate attinfo) {
        this.attinfo = attinfo;
    }

    public RoleInfoAttackTemplate getSttinfo() {
        return sttinfo;
    }

    public void setSttinfo(RoleInfoAttackTemplate sttinfo) {
        this.sttinfo = sttinfo;
    }

    @Override
    public String toString() {
        return "RoleInfoTemplate{" +
                "findzone='" + findzone + '\'' +
                ", attzone='" + attzone + '\'' +
                ", attrange=" + attrange +
                ", attinfo=" + attinfo +
                ", sttinfo=" + sttinfo +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class RoleInfoAttackTemplate{

        @JsonProperty("batti")
        private Integer batti;
        @JsonProperty("hframe")
        private Integer hframe;
        @JsonProperty("tframe")
        private Integer tframe;
        @JsonProperty("speed")
        private Integer speed;
        @JsonProperty("add")
        private Integer add;

        public Integer getBatti() {
            return batti;
        }

        public void setBatti(Integer batti) {
            this.batti = batti;
        }

        public Integer getHframe() {
            return hframe;
        }

        public void setHframe(Integer hframe) {
            this.hframe = hframe;
        }

        public Integer getTframe() {
            return tframe;
        }

        public void setTframe(Integer tframe) {
            this.tframe = tframe;
        }

        public Integer getSpeed() {
            return speed;
        }

        public void setSpeed(Integer speed) {
            this.speed = speed;
        }

        public Integer getAdd() {
            return add;
        }

        public void setAdd(Integer add) {
            this.add = add;
        }

        @Override
        public String toString() {
            return "RoleInfoAttackTemplate{" +
                    "batti=" + batti +
                    ", hframe=" + hframe +
                    ", tframe=" + tframe +
                    ", speed=" + speed +
                    ", add=" + add +
                    '}';
        }
    }

}
