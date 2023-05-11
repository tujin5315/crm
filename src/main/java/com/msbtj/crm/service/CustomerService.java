package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.dao.CustomerLossMapper;
import com.msbtj.crm.dao.CustomerMapper;
import com.msbtj.crm.dao.CustomerOrderMapper;
import com.msbtj.crm.query.CustomerQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.utils.PhoneUtil;
import com.msbtj.crm.vo.Customer;
import com.msbtj.crm.vo.CustomerLoss;
import com.msbtj.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;
    @Resource
    private CustomerOrderMapper customerOrderMapper;


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


    /**
      更新客户的流失状态
         1.查询待流失的客户数据
         2.将流失客户数据批量添加到流失客户表中
         3.批量更新客户的流失状态   0=正常客户  1=流失客户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerState(){
         /* 查询待流失的客户数据 */
        List<Customer> lossCustomerList = customerMapper.queryLossCustomer();
         /* 将流失客户数据批量添加到流失客户表中 */
        // 判断流失客户数据是否存在 有流失客户才做以下操作
        if(null != lossCustomerList && lossCustomerList.size()>0){
            // 定义一个集合，用来接收流失客户的id(需要使用到该id去更新流失客户的流失的状态)
            List<Integer> lossCustomerIds = new ArrayList<>();
            // 定义一个流失客户的列表  添加时是批量添加
            List<CustomerLoss> customerLossList = new ArrayList<>();
            // 遍历查询流失客户数据(使用lambad 调用forEach方法进行遍历查询)
            lossCustomerList.forEach(customer -> {
                // 定义流失客户对象
                CustomerLoss customerLoss = new CustomerLoss();
                // 设置流失客户对象数据
                // 创建时间 系统当前时间
                customerLoss.setCreateDate(new Date());
                // 更新时间 系统当前时间
                customerLoss.setUpdateDate(new Date());
                // 设置客户经理
                customerLoss.setCusManager(customer.getCusManager());
                // 设置客户名称
                customerLoss.setCusName(customer.getName());
                // 设置客户编号
                customerLoss.setCusNo(customer.getKhno());
                // 设置当前客户的有效性
                customerLoss.setIsValid(1);
                // 设置客户的流失状态 0=暂缓流失  1=确认流失
                customerLoss.setState(0);
                // 怎样判断流失 通过客户下单时间判定  需要查询订单记录 ---- 通过客户id 查询订单记录
                CustomerOrder customerOrder = customerOrderMapper.queryCustomerOrderByCustomerId(customer.getId());
                // 判断客户订单是否存在，若存在，则设置下单时间
                if( null != customerOrder){
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                // 将流失客户对象设置到对应的集合中
                customerLossList.add(customerLoss);
                // 将流失客户的id设置到对应的集合中
                lossCustomerIds.add(customer.getId());
            });
            // 批量添加流失客户数据
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList)!=customerLossList.size(),"客户流失数据更新失败");
            /* 批量更新客户的流失状态 */
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCustomerIds)!=lossCustomerIds.size(),"客户流失数据更新失败");
        }
    }

}
