package com.msbtj.crm.query;

import com.msbtj.crm.base.BaseQuery;

public class CustomerLossQuery extends BaseQuery {
    private String cusNo; // 客户编号
    private String cusName; // 客户姓名
    private Integer state; // 流失状态

    public String getCusNo() {
        return cusNo;
    }

    public void setCusNo(String cusNo) {
        this.cusNo = cusNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
