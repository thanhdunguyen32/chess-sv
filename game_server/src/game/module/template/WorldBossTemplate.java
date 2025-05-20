package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;
import java.util.Map;

@ExcelTemplateAnn(file = "myWorldBoss.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorldBossTemplate {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("bset")
    private Map<Integer, ChapterBattleTemplate> bset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<Integer, ChapterBattleTemplate> getBset() {
        return bset;
    }

    public void setBset(Map<Integer, ChapterBattleTemplate> bset) {
        this.bset = bset;
    }

    @Override
    public String toString() {
        return "WorldBossTemplate{" +
                "id=" + id +
                ", bset=" + bset +
                '}';
    }

}
