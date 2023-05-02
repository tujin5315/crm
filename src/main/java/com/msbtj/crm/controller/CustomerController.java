package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.CustomerQuery;
import com.msbtj.crm.service.CustomerService;
import com.msbtj.crm.vo.Customer;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Resource
    private CustomerService customerService;

    /**
     * 多条件查询客户信息
     * @param customerQuery
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCusDevPlanByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }


    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    @ResponseBody
    @PostMapping("add")
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加客户成功");
    }

    /**
     * 修改客户信息
     * @param customer
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("修改客户信息成功");
    }

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("删除客户信息成功");
    }


    /**
     * 进入客户信息管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "/customer/customer";
    }


    /**
     * 打开添加|更新客户信息对话框
     * @return
     */
    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, HttpServletRequest request){
        // 如果id不为空，则为修改操作，先查询客户记录
        if(id!=null){
            // 通过id查询客户信息
            Customer customer = customerService.selectByPrimaryKey(id);
            // 将查询到的记录返回到作用域中
            request.setAttribute("customer",customer);
        }
        return "/customer/add_update";
    }

    /**
     * 进入客户订单查询页面
     */
    @RequestMapping("orderInfoPage")
    public String orderInfoPage(Integer id, Model model){
         // 通过客户id查询客户记录，设置到请求域中
        model.addAttribute("customer",customerService.selectByPrimaryKey(id));
         return  "customer/customer_order";
    }

}
