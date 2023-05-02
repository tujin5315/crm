package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.CustomerMapper;
import com.msbtj.crm.dao.CustomerOrderMapper;
import com.msbtj.crm.query.CustomerOrderQuery;
import com.msbtj.crm.vo.Customer;
import com.msbtj.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {
    @Resource
    private CustomerOrderMapper customerOrderMapper;
    /**
     * 多条件分页查询客户列表信息
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    public Map<String, Object> queryOrderByOrderId(Integer orderId) {
        return customerOrderMapper.queryOrderByOrderId(orderId);
    }
}
