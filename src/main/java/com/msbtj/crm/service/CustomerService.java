package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.dao.CustomerMapper;
import com.msbtj.crm.query.CustomerQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.utils.PhoneUtil;
import com.msbtj.crm.vo.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;

    /**
     * 多条件分页查询客户列表信息
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery){
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        // 得到对应分页对象
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户信息
     * 1.参数校验
     *   客户名称  name
     *      非空 且唯一
     *   公司法人
     *      非空
     *   手机号码
     *      非空 且格式正确
     * 2.设置默认值
     *    是否有效
     *    创建时间
     *    修改时间
     *    客户流失状态  state
     *    0=正常客户  1=流失客户
     *    客户编号  (uuid | 时间戳 | 年月日时分秒 | 雪花算法)
     * 3.执行添加操作，判断受影响的行数
     * @param customer
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        /* 参数校验 */
        //  客户名称  name  公司法人  手机号码 非空
        //  手机号码  格式正确
        checkCustomer(customer.getName(),customer.getFr(),customer.getPhone());
        Customer temp = customerMapper.selectByCustomerName(customer.getName());
        AssertUtil.isTrue(temp != null,"该公司名称已存在！");
        /* 设置默认值 */
        //   是否有效
        customer.setIsValid(1);
        //   创建时间
        customer.setCreateDate(new Date());
        //   修改时间
        customer.setUpdateDate(new Date());
        //    客户编号  (uuid | 时间戳 | 年月日时分秒 | 雪花算法)
        // 客户流失状态  state 默认为0
        customer.setState(0);
        // 使用时间戳
        String khno = "KH"+System.currentTimeMillis();
        customer.setKhno(khno);
        //    3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.insertSelective(customer)<1,"公司添加失败！");
    }

    /**
     * 修改客户信息
     * 1.参数校验
     *   客户id
     *      非空 唯一 数据是否存在
     *   客户名称  name
     *      非空 且唯一
     *   公司法人
     *      非空
     *   手机号码
     *      非空 且格式正确
     * 2.设置默认值
     *    修改时间
     * 3.执行添加操作，判断受影响的行数
     * @param customer
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer){
        /* 参数校验 */
        // 客户id非空校验
        AssertUtil.isTrue(null == customer.getId(),"客户记录不存在");
        // 根据id查询当前记录  判断数据是否存在
        Customer temp = customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(null == temp,"待修改记录不存在");
        checkCustomer(customer.getName(),customer.getFr(),customer.getPhone());
        // 若没有修改公司名称，则当前公司名称可以不改变
        AssertUtil.isTrue(temp.getId()!= customer.getId() && temp.getName().equals(customer.getName()),"客户名称已存在！");
        /* 设置默认值 */
        customer.setUpdateDate(new Date());
        /* 执行添加操作 */
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)<1,"待修改客户信息失败");

    }

    /**
     * 删除客户信息
     * 1.参数校验
     * // id  非空  数据是否存在
     * 2.设置默认值
     * 3.执行删除操作，判断受影响的行数
     * @param id
     */
    public void deleteCustomer(Integer id) {
        /* 参数校验 */
        AssertUtil.isTrue(null == id  ,"待删除客户信息记录不存在");
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customer == null,"待删除客户信息不存在");
        /* 设置默认值 */
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());
        /* 执行删除操作 */
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)<1,"待删除客户信息失败");
    }

    /**
     * 参数校验
     * 公司名称   ---- 非空
     * 公司法人   ---- 非空
     * 电话号码   ---- 非空 且格式正确
     * @param name
     * @param fr
     * @param phone
     */
    private void checkCustomer(String name, String fr, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(name),"公司名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人代表不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确");
    }


}
