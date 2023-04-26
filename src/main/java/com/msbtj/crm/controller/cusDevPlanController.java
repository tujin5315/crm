package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.service.CusDevPlanService;
import com.msbtj.crm.service.SaleChanceService;
import com.msbtj.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("cus_dev_plan")
public class cusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;
    /**
     * 进入计划开发页面
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 打开计划项开发与详情页面
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer id, HttpServletRequest request){
        // 通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        // 将对象设置到请求域中
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }
}
