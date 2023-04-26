package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.enums.StateStatus;
import com.msbtj.crm.query.SaleChanceQuery;
import com.msbtj.crm.service.SaleChanceService;
import com.msbtj.crm.utils.CookieUtil;
import com.msbtj.crm.utils.LoginUserUtil;
import com.msbtj.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 装载营销机会管理数据
     * @param saleChanceQuery
     * flag: 营销机会数据查询(分页多条件查询)
     *  如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
        //判断flag的值
        if(flag!=null && flag == 1){
            // 查询客户开发计划
            // 设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            // 设置指派人(当前登录用户的id)
            // 从cookie中获取当前登录用户的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 跳转到营销机会管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }
    /**
     * 添加营销机会
     */
    @ResponseBody
    @PostMapping("add")
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        // 从cookie中获取当前登录的用户名
        String userName = CookieUtil.getCookieValue(request,"userName");
        // 设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        // 调用service添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功");
    }

    /**
     * 营销更新修改操作
     * @param saleChance
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateSaleChance(SaleChance saleChance){
        // 调用service添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功");
    }

    /**
     * 进入添加、修改营销机会数据页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer id,HttpServletRequest request){
        System.out.println(id);
        // 判断id是否为空
        if(id != null){
            // 通过id查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            System.out.println(saleChance.toString());
            // 将数据通过request设置到请求域中
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }


    /**
     * 删除营销机会
     */

    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteSaleChance(Integer[] ids){
        // 直接调用service层方法
        saleChanceService.deleteSaleChance(ids);
        // 返回
        return success("该营销机会已被删除");
    }
}
