package com.msbtj.crm.query;

import com.msbtj.crm.base.BaseQuery;

public class OrderDetailQuery extends BaseQuery {
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
