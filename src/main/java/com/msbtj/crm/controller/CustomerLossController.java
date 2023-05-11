package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.CustomerLossQuery;
import com.msbtj.crm.service.CustomerLossService;
import com.msbtj.crm.service.CustomerService;
import com.msbtj.crm.vo.CustomerLoss;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {
    @Resource
    private CustomerLossService customerLossService;

    /**
     * 进入客户流失页面
     */
    @RequestMapping("index")
    public String index(){
        return "customerLoss/customer_loss";
    }

    /**
     * 页面数据展示(多条件翻页查询数据)
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerLossByParams(customerLossQuery);
    }
    @RequestMapping("toCustomerReprPage")
    public String toCustomerReprPage(Integer id, Model model){
        // 通过流失客户的id查询流失客户的数据
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(id);
        // 将查询的结果设置到请求域中
        model.addAttribute("customerLoss",customerLoss);
        return "customerLoss/customer_rep";
    }

    /**
     * 更新流失客户的流失状态
     */
    @RequestMapping("updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(Integer id,String lossReason){
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return success("更新流失客户状态成功");
    }
}
