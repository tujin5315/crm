package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.CustomerLossMapper;
import com.msbtj.crm.query.CustomerLossQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.vo.CustomerLoss;
import com.msbtj.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页条件查询
     * @param customerLossQuery
     * @return
     */
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossMapper.selectByParams(customerLossQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 更新流失状态
      1.参数校验
        主键id
            非空      数据存在
        流失状态原因
            非空
      2.设置默认值
        是否流失  state     0=暂缓流失  1=确认流失
        更新时间       系统时间
        客户流失时间    confirmLosstime
        流失原因        lossReason
      3.执行更新操作，判断受影响的行数
     * @param id
     * @param lossReason
     */
    public void updateCustomerLossStateById(Integer id,String lossReason) {
        /* 参数校验 */
        AssertUtil.isTrue(id == null,"待流失客户数据不能为空");
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customerLoss==null,"待流失客户数据不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(lossReason),"流失原因不能为空");
        /* 设置默认值 */
        customerLoss.setState(1); // 确认流失
        customerLoss.setUpdateDate(new Date()); // 更新时间
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date()); // 客户流失时间
        /* 执行更新操作，判断受影响的行数 */
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss)!=1,"待流失客户数据判定失败");
    }
}
