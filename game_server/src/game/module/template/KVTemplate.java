package game.module.template;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KVTemplate {

    @JsonProperty("KEY")
    private String KEY;

    @JsonProperty("VAL")
    private Integer VAL;

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public Integer getVAL() {
        return VAL;
    }

    public void setVAL(Integer VAL) {
        this.VAL = VAL;
    }

    @Override
    public String toString() {
        return "KVTemplate{" +
                "KEY='" + KEY + '\'' +
                ", VAL=" + VAL +
                '}';
    }
}
