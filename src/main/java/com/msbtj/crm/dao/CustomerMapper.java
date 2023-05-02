package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.Customer;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    Customer selectByCustomerName(String name);
}