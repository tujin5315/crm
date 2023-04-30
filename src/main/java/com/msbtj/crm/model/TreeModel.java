package com.msbtj.crm.model;

public class TreeModel {
    private Integer id;
    private Integer pid;
    private String name;
    private boolean checked = false ;   // 复选框是否被勾选，如果是true，则勾选；如果是false，则不勾选

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
