package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {
    // 通过订单id查询订单信息
    Map<String, Object> queryOrderByOrderId(Integer orderId);
    // 通过客户id查询该客户的最新订单记录
    CustomerOrder queryCustomerOrderByCustomerId(Integer id);
}