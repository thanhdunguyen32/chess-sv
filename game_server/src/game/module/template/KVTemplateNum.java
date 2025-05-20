package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KVTemplateNum extends KVTemplate {

    @JsonProperty("NUM")
    private Integer NUM;

    public Integer getNUM() {
        return NUM;
    }

    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }

    @Override
    public String toString() {
        return "KVTemplateNum{" +
                "NUM=" + NUM +
                "} " + super.toString();
    }
}
