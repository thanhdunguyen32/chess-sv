package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Activity4View {
    private int id;
    private String name;
    private Date startTime;
    private Date endTime;
    private Boolean isOpen;
    private boolean isConfig;

    public Activity4View(int id, String name, Date startTime, Date endTime, Boolean isOpen, boolean isConfig) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpen = isOpen;
        this.isConfig = isConfig;
    }
}
