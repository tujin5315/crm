package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.CustomerReprieveQuery;
import com.msbtj.crm.service.CustomerReprieveService;
import com.msbtj.crm.vo.CustomerReprieve;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_rep")
public class CustomerReprieveController extends BaseController {

    @Resource
    private CustomerReprieveService customerReprieveService;
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCustomerReprieveByLossId(CustomerReprieveQuery customerReprieveQuery){
        return customerReprieveService.queryCustomerReprieveByLossId(customerReprieveQuery);
    }

    /**
     * 添加暂缓数据
     * @param customerReprieve
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCustomerRepr(CustomerReprieve customerReprieve){
        customerReprieveService.addCustomerRepr(customerReprieve);
        return success("添加数据成功");
    }

    /**
     * 修改暂缓数据
     * @param customerReprieve
     * @return
     */

    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateCustomerRepr(CustomerReprieve customerReprieve){
        customerReprieveService.updateCustomerRepr(customerReprieve);
        return success("修改数据成功");
    }
    /**
     * 打开添加|修改暂缓数据页面
     */
    @RequestMapping("addOrUpdateCustomerReprPage")
    public String addOrUpdateCustomerReprPage(Integer lossId, HttpServletRequest request,Integer id){
        // 将流失客户的id储存在作用域中(便于隐藏域去拿取id，利用id才知晓对谁进行修改)
        request.setAttribute("lossId",lossId);
        // 通过id查询，判断数据是否为空
        if(id!=null) {
            // 通过id查询客户流失数据信息
            CustomerReprieve customerRep = customerReprieveService.selectByPrimaryKey(id);
            // 将customerRep对象设置到作用域中
            request.setAttribute("customerRep",customerRep);
        }
        return "customerLoss/customer_rep_add_update";
    }


}
