package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.CusDevPlanMapper;
import com.msbtj.crm.query.CusDevPlanQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.vo.CusDevPlan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 多条件分页查询客户开发计划
     * (返回的数据格式必须满足leiui中要求的数据格式)
     * @return
     */
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加营销计划
      1、参数校验
          营销机会ID  非空
          计划内容    非空
          计划时间    非空
      2、设置参数的默认值
          是否有效
          创建时间
          修改时间
      3、执行添加操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        /* 参数校验 */
        checkCusDevPlanParams(cusDevPlan);
        /* 设置参数的默认值 */
        //是否有效      默认有效
        cusDevPlan.setIsValid(1);
        //创建时间      系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //修改时间      系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        /* 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"计划项数据添加失败");
    }

    /**
     计划项更新操作
         1、参数校验
             计划项ID       非空，数据存在
             营销机会ID      非空，数据存在
             计划内容        非空
             计划时间        非空
         2、设置参数的默认值
            修改时间        系统当前时间
         3、执行更新操作
            判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        /* 参数校验 */
        // 计划项ID       非空，数据存在
        AssertUtil.isTrue(null == cusDevPlan.getId() || null == cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()),"系统异常，请重新尝试");
        checkCusDevPlanParams(cusDevPlan);
        /* 设置参数的默认值 */
        // 修改时间        系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"更新失败！");
    }

    /**
      计划项删除操作
        1.判断id是否为空，数据是否存在
        2.修改isValid属性
        3.执行更新操作
      @param id
     */
    public void deleteCusDevPlan(Integer id){
        // 判断id是否为空，数据是否存在
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        // 找到查找对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        // 修改isValid属性
        cusDevPlan.setIsValid(0);
        // 修改更新时间
        cusDevPlan.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"更新失败");
    }

    /**
     * 参数校验
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        // 营销机会ID  非空
        AssertUtil.isTrue(cusDevPlan.getSaleChanceId()==null,"数据异常，请重试");
        // 计划内容    非空
        AssertUtil.isTrue(cusDevPlan.getPlanItem()==null,"计划内容不能为空");
        // 计划时间    非空
        AssertUtil.isTrue(cusDevPlan.getPlanDate()==null,"计划时间不能为空");
    }

}
