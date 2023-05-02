package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.query.CustomerQuery;
import com.msbtj.crm.query.OrderDetailQuery;
import com.msbtj.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order_details")
public class OrderDetailsController extends BaseController {
    @Resource
    private OrderDetailsService orderDetailsService;
    /**
     * 多条件查询
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCusDevPlanByParams(OrderDetailQuery orderDetailQuery){
        return orderDetailsService.queryCustomerByParams(orderDetailQuery);
    }
}
