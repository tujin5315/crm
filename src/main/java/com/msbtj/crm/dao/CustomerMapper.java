package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.Customer;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {
    // 通过客户名称查询客户信息
    Customer selectByCustomerName(String name);
    // 通过客户id批量更新客户流失状态
    int updateCustomerStateByIds(List<Integer> lossCustomerIds);
    // 查询流失的客户数据
    List<Customer> queryLossCustomer();
}