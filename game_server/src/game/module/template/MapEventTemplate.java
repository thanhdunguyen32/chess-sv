package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "dbMapEvent.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapEventTemplate {

    @JsonProperty("EVENT")
    private MapEventAllTemplate EVENT;

    public MapEventAllTemplate getEVENT() {
        return EVENT;
    }

    public void setEVENT(MapEventAllTemplate EVENT) {
        this.EVENT = EVENT;
    }

    @Override
    public String toString() {
        return "MapEventTemplate{" +
                "EVENT=" + EVENT +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MapEventAllTemplate{
        @JsonProperty("1")
        private Map<Integer,MapEvent1Template> EVENT1;
        @JsonProperty("2")
        private Map<Integer,MapEvent2Template> EVENT2;
        @JsonProperty("6")
        private Map<Integer,MapEvent6Template> EVENT6;

        public Map<Integer, MapEvent1Template> getEVENT1() {
            return EVENT1;
        }

        public void setEVENT1(Map<Integer, MapEvent1Template> EVENT1) {
            this.EVENT1 = EVENT1;
        }

        public Map<Integer, MapEvent2Template> getEVENT2() {
            return EVENT2;
        }

        public void setEVENT2(Map<Integer, MapEvent2Template> EVENT2) {
            this.EVENT2 = EVENT2;
        }

        public Map<Integer, MapEvent6Template> getEVENT6() {
            return EVENT6;
        }

        public void setEVENT6(Map<Integer, MapEvent6Template> EVENT6) {
            this.EVENT6 = EVENT6;
        }

        @Override
        public String toString() {
            return "MapEventAllTemplate{" +
                    "EVENT1=" + EVENT1 +
                    ", EVENT2=" + EVENT2 +
                    ", EVENT6=" + EVENT6 +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MapEvent1Template{
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("lv")
        private Integer lv;
        @JsonProperty("items")
        private List<RewardTemplateSimple1> items;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLv() {
            return lv;
        }

        public void setLv(Integer lv) {
            this.lv = lv;
        }

        public List<RewardTemplateSimple1> getItems() {
            return items;
        }

        public void setItems(List<RewardTemplateSimple1> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "MapEvent1Template{" +
                    "id=" + id +
                    ", lv=" + lv +
                    ", items=" + items +
                    '}';
        }
    }

    public static final class RewardTemplateSimple1{
        @JsonProperty("gsid")
        private Integer gsid;

        @JsonProperty("count")
        private Integer count;

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "RewardTemplateSimple1{" +
                    "gsid=" + gsid +
                    ", count=" + count +
                    '}';
        }
    }

    public static final class RewardTemplateSimple2{
        @JsonProperty("gsid")
        private Integer gsid;

        @JsonProperty("count")
        private int[] count;

        @JsonProperty("rate")
        private Integer rate;

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public int[] getCount() {
            return count;
        }

        public void setCount(int[] count) {
            this.count = count;
        }

        public Integer getRate() {
            return rate;
        }

        public void setRate(Integer rate) {
            this.rate = rate;
        }

        @Override
        public String toString() {
            return "RewardTemplateSimple2{" +
                    "gsid=" + gsid +
                    ", count=" + Arrays.toString(count) +
                    ", rate=" + rate +
                    '}';
        }
    }

    public static final class RewardTemplateSimple6{
        @JsonProperty("gsid")
        private Integer gsid;

        @JsonProperty("count")
        private int[] count;

        public Integer getGsid() {
            return gsid;
        }

        public void setGsid(Integer gsid) {
            this.gsid = gsid;
        }

        public int[] getCount() {
            return count;
        }

        public void setCount(int[] count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "RewardTemplateSimple2{" +
                    "gsid=" + gsid +
                    ", count=" + Arrays.toString(count) +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MapEvent2Template{
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("lv")
        private Integer lv;
        @JsonProperty("items")
        private List<RewardTemplateSimple2> items;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLv() {
            return lv;
        }

        public void setLv(Integer lv) {
            this.lv = lv;
        }

        public List<RewardTemplateSimple2> getItems() {
            return items;
        }

        public void setItems(List<RewardTemplateSimple2> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "MapEvent2Template{" +
                    "id=" + id +
                    ", lv=" + lv +
                    ", items=" + items +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MapEvent6Template{
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("lv")
        private Integer lv;
        @JsonProperty("items")
        private List<RewardTemplateSimple6> items;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLv() {
            return lv;
        }

        public void setLv(Integer lv) {
            this.lv = lv;
        }

        public List<RewardTemplateSimple6> getItems() {
            return items;
        }

        public void setItems(List<RewardTemplateSimple6> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "MapEvent2Template{" +
                    "id=" + id +
                    ", lv=" + lv +
                    ", items=" + items +
                    '}';
        }
    }
}
