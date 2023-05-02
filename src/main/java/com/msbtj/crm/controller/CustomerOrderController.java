package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.CustomerOrderQuery;
import com.msbtj.crm.query.CustomerQuery;
import com.msbtj.crm.service.CustomerOrderService;
import com.msbtj.crm.vo.CustomerOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;

    /**
     * 多条件查询客户信息
     * @param customerOrderQuery
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCusDevPlanByParams(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryCustomerByParams(customerOrderQuery);
    }
    /**
     * 打开订单详情页面
     */
    @RequestMapping("orderDetailPage")
    public String orderDetailPage(Model model, Integer orderId){
        // 利用orderId查询customerOrder的信息
        Map<String,Object> map = customerOrderService.queryOrderByOrderId(orderId);
        // 将数据设置到请求域中
        model.addAttribute("order",map);
        return "customer/customer_order_detail";
    }
}
