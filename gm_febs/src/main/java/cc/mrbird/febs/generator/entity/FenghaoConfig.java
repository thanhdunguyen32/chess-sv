package cc.mrbird.febs.generator.entity;

import lombok.Data;

@Data
public class FenghaoConfig {
    private int opType;
    private int playerId;
    private String endTime;

    @Override
    public String toString() {
        return "FenghaoConfig{" +
                "opType=" + opType +
                ", playerId=" + playerId +
                ", endTime=" + endTime +
                '}';
    }
}
