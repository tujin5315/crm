package com.msbtj.crm.query;

import com.msbtj.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    // 分页参数
    // 条件参数
    private String customerName; // 客户名
    private String createMan; // 创建人
    private Integer state; // 分配状态
    private Integer assignMan; // 指派人

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    private String devResult; //

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
