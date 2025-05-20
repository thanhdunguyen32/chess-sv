package cc.mrbird.febs.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GsTreeWrap implements Serializable {
    private Integer id;
    private String name;
    private boolean checked = false;
    private boolean hasParent = false;
    private boolean hasChild = false;
}
