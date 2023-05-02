package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    Map<String, Object> queryOrderByOrderId(Integer orderId);
}