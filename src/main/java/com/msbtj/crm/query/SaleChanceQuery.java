package com.msbtj.crm.query;

import com.msbtj.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    // 分页参数
    // 条件参数
    private String customerName; // 客户名
    private String createMan; // 创建人
    private String state; // 分配状态

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
