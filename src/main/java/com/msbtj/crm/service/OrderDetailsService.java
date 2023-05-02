package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.OrderDetailsMapper;
import com.msbtj.crm.query.OrderDetailQuery;
import com.msbtj.crm.vo.Customer;
import com.msbtj.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    public Map<String, Object> queryCustomerByParams(OrderDetailQuery orderDetailQuery) {
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(orderDetailQuery.getPage(),orderDetailQuery.getLimit());
        // 得到对应分页对象
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }
}
