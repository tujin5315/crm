package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.CusDevPlanQuery;
import com.msbtj.crm.service.CusDevPlanService;
import com.msbtj.crm.service.SaleChanceService;
import com.msbtj.crm.vo.CusDevPlan;
import com.msbtj.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    /**
     * 客户开发计划数据查询(分页多条件)
     * @param cusDevPlanQuery
     * flag: 营销机会数据查询(分页多条件查询)
     *  如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(CusDevPlanQuery cusDevPlanQuery){

        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }
    /**
     * 添加计划项
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        // 调用service层方法
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        // 返回结果
        return success("计划项添加成功");
    }
    /**
     * 更新计划项
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        // 调用service层方法
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        // 返回结果
        return success("计划项更新成功");
    }
    /**
     * 进入添加或修改的页面
     */
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer saleChanceId,HttpServletRequest request){
        // 将营销机会id设置到请求域中，给计划页面获取
        request.setAttribute("saleChanceId",saleChanceId);
        return "cusDevPlan/add_update";
    }


}
