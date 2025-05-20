package game.module.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import game.common.excel.ExcelTemplateAnn;

import java.util.List;

@ExcelTemplateAnn(file = "mailZhd.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailZhdTemplate {

    private String title;

    private String content;

    private List<RewardTemplateSimple> reward;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RewardTemplateSimple> getReward() {
        return reward;
    }

    public void setReward(List<RewardTemplateSimple> reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "MailZhdTemplate{" +
                "title='" + title + '\'' +
                ", content=" + content +
                ", reward=" + reward +
                '}';
    }
}
