package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.CustomerLossMapper;
import com.msbtj.crm.dao.CustomerReprieveMapper;
import com.msbtj.crm.query.CustomerReprieveQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.vo.CustomerLoss;
import com.msbtj.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve,Integer> {
    @Resource
    private CustomerReprieveMapper customerReprieveMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> queryCustomerReprieveByLossId(CustomerReprieveQuery customerReprieveQuery) {
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerReprieveQuery.getPage(),customerReprieveQuery.getLimit());
        // 得到对应分页对象
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieveMapper.selectByParams(customerReprieveQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加暂缓数据
       1.参数校验
          流失客户ID   lossId
             非空，数据存在
          暂缓措施内容
             非空
       2.设置参数的默认值
            是否有效
                默认有效   1
            创建时间
                默认时间   系统时间
            修改时间
                默认时间   系统时间
       3.执行添加操作，判断受影响的行数
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1.参数校验 */
        Integer lossId = customerReprieve.getLossId();
        String measure = customerReprieve.getMeasure();
        checkParams(lossId,measure);
        /* 2.设置参数的默认值 */
        // 是否有效   默认有效   1
        customerReprieve.setIsValid(1);
        // 创建时间   系统时间
        customerReprieve.setCreateDate(new Date());
        // 修改时间   系统时间
        customerReprieve.setUpdateDate(new Date());
        /* 3.执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve)!=1,"添加数据失败");
    }

    /**
     * 参数校验
     * @param lossId
     * @param measure
     */
    private void checkParams(Integer lossId, String measure) {
        // 流失客户ID lossID 非空，数据存在
        AssertUtil.isTrue(null == lossId || customerLossMapper.selectByPrimaryKey(lossId) == null,"流失客户记录不存在");
        // 暂缓措施内容  非空
        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施内容不能为空");
    }

    /**
     * 修改暂缓数据
     * 1.参数校验
        主键id
            非空  数据不能为空
         流失客户ID   lossId
            非空，数据存在
         暂缓措施内容
            非空
     * 2.设置默认值
         修改时间
            系统时间
     * 3.执行更新操作，判断受影响的行数
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1.参数校验 */
        // 主键id  非空
        AssertUtil.isTrue(null == customerReprieve.getId(),"待更新记录不存在");
        CustomerReprieve temp = customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId());
        // 主键id  数据不能为空
        AssertUtil.isTrue(temp==null,"待更新记录不存在");
        checkParams(customerReprieve.getLossId(),customerReprieve.getMeasure());
        AssertUtil.isTrue(!(temp.getLossId().equals(customerReprieve.getLossId())),"待更新记录的流失客户不对称");
        /* 设置默认值 */
        // 修改时间  系统时间
        customerReprieve.setUpdateDate(new Date());
        /* 执行更新操作，判断受影响的行数 */
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve)!=1,"待更新记录失败");

    }
}
