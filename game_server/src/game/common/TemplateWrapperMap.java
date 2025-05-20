package game.common;

import java.util.Map;

public class TemplateWrapperMap<T> {

    private Integer status;
    private Map<Integer, T> list;
    private String tablename;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<Integer, T> getList() {
        return list;
    }

    public void setList(Map<Integer, T> list) {
        this.list = list;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}
