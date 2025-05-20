package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "actMjbg.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZhdMjbgTemplate {
    private Integer djgsid;
    private Integer djcount;
    private List<RewardTemplateMjbg> items;
    private List<List<RewardTemplateMjbg>> finallist;
    private List<MjbgPageJump> list;

    public Integer getDjgsid() {
        return djgsid;
    }

    public void setDjgsid(Integer djgsid) {
        this.djgsid = djgsid;
    }

    public Integer getDjcount() {
        return djcount;
    }

    public void setDjcount(Integer djcount) {
        this.djcount = djcount;
    }

    public List<RewardTemplateMjbg> getItems() {
        return items;
    }

    public void setItems(List<RewardTemplateMjbg> items) {
        this.items = items;
    }

    public List<List<RewardTemplateMjbg>> getFinallist() {
        return finallist;
    }

    public void setFinallist(List<List<RewardTemplateMjbg>> finallist) {
        this.finallist = finallist;
    }

    public List<MjbgPageJump> getList() {
        return list;
    }

    public void setList(List<MjbgPageJump> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ZhdMjbgTemplate{" +
                "djgsid=" + djgsid +
                ", djcount=" + djcount +
                ", items=" + items +
                ", finallist=" + finallist +
                ", list=" + list +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class RewardTemplateMjbg {

        private Integer gsid;

        private Integer count;

        private Integer maxnum;

        private Integer rate;

        public RewardTemplateMjbg(Integer gsid, Integer count, Integer maxnum, Integer rate) {
            this.gsid = gsid;
            this.count = count;
            this.maxnum = maxnum;
            this.rate = rate;
        }

        public RewardTemplateMjbg() {
        }

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

        public Integer getMaxnum() {
            return maxnum;
        }

        public void setMaxnum(Integer maxnum) {
            this.maxnum = maxnum;
        }

        public Integer getRate() {
            return rate;
        }

        public void setRate(Integer rate) {
            this.rate = rate;
        }

        @Override
        public String toString() {
            return "RewardTemplateMjbg{" +
                    "gsid=" + gsid +
                    ", count=" + count +
                    ", maxnum=" + maxnum +
                    ", rate=" + rate +
                    '}';
        }
    }

    public static final class MjbgPageJump{
        private String intro;

        private String page;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "MjbgPageJump{" +
                    "intro='" + intro + '\'' +
                    ", page='" + page + '\'' +
                    '}';
        }
    }

}
