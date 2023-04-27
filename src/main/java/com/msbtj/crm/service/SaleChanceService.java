package com.msbtj.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.SaleChanceMapper;
import com.msbtj.crm.enums.DevResult;
import com.msbtj.crm.enums.StateStatus;
import com.msbtj.crm.query.SaleChanceQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.utils.PhoneUtil;
import com.msbtj.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会(返回的数据格式必须满足leiui中要求的数据格式)
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        // 得到对应分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }
    /**
     * 添加营销机会
     *    1、参数校验
     *             customerName 客户名称       非空
     *             linkMan 联系人              非空
     *             linkPhone 联系号码          非空，手机号码格式正确
     *         2、设置相关参数的默认值
     *             createMan 创建人           当前登录用户名
     *             assingMan 指派人
     *                 若未设置指派人(默认)
     *                     state分配状态   (0=未分配，1=已分配)
     *                         0=未分配
     *                     assignTime 指派时间
     *                         设置为null
     *                     derResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)
     *                         0=未开发
     *                 若设置了指派人
     *                     state分配状态   (0=未分配，1=已分配)
     *                         1=已分配
     *                     assignTime 指派时间
     *                         系统当前时间
     *                     derResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)
     *                         1=开发中
     *             isValid 是否有效
     *                 设置为有效 1=有效 0=无效
     *             createTime 创建时间
     *                 默认系统当前时间
     *             updateTime 修改时间
     *                 默认系统当前时间
     *         3、执行添加操作，判断受影响的行数
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        /* 1. 参数校验 */
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        /* 2. 设置相关参数的默认值 */
        // isValid是否有效 (0=无效，1=有效) 设置为有效
        saleChance.setIsValid(1);
        // createTime 创建时间 默认系统当前时间
        saleChance.setCreateDate(new Date());
        // updateTime 修改时间 默认系统当前时间
        saleChance.setUpdateDate(new Date());
        // 判断是否设置了指派人
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            // 若为空，则表示未设置指派人
            // state分配状态   (0=未分配，1=已分配)   设置为未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
            // assignTime 指派时间   设置为null
            saleChance.setAssignTime(null);
            // derResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败)  0=未开发
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }
        else {
            // 如果不为空，表示设置了指派人
            // state分配状态   (0=未分配，1=已分配)  1=已分配
            saleChance.setState(StateStatus.STATED.getType());
            // assignTime 指派时间 系统当前时间
            saleChance.setAssignTime(new Date());
            // derResult 开发状态 (0=未开发，1=开发中，2=开发成功，3=开发失败) 1=开发中
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        /* 3.执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加营销机会失败");
    }



    /**
      更新营销机会
     1、参数校验
         营销机会id                  非空，数据中对应的记录存在
         customerName客户名称        非空
         linkMan 联系人              非空
         linkPhone 联系号码          非空，手机号码格式正确
     2、设置相关参数的默认值
         updateDate 更新时间         设置为系统默认时间
         assignMan  指派人
             原始数据未设置
                 修改后未设置         不需要操作
                 修改后已设置
                     assignTime 指派时间 设置为系统当前时间
                     分配状态        1=已分配
                     开发状态        1=开发中
             原始数据已设置
                 修改后未设置
                     assignTime 指派时间 设置为null
                     分配状态        0=未分配
                     开发状态        0=未开发
                 修改后已设置
                     判断修改前后是否是同一个指派人
                         如果是，不需要操作
                         如果不是，则需要更新
                             assignTime 指派时间 设置为系统默认时间
     3、 执行更新操作，判断受影响的行数
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        // 参数校验
        /* 营销机会id    非空 */
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        /* 营销机会id    数据中对应的记录存在 */
        AssertUtil.isTrue(null == temp,"待更新记录不存在");
        // 检验另外的参数
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        /* 设置相关参数的默认值 */
        // updateDate 更新时间   设置为系统默认时间
        saleChance.setUpdateDate(new Date());
        // assignMan  指派人
        // 判断原始指派人是否存在
        if(StringUtils.isBlank(temp.getAssignMan())){ // 不存在
            // 判断修改后的值是否设置
            if(!StringUtils.isBlank(saleChance.getAssignMan())){
                // 修改后有值，需要去设置默认值
                //assignTime 指派时间 设置为系统当前时间
                saleChance.setAssignTime(new Date());
                // 分配状态        1=已分配
                saleChance.setState(StateStatus.STATED.getType());
                // 开发状态        1=开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
            else { // 存在
                // 判断修改后的值是否存在
                if(StringUtils.isBlank(saleChance.getAssignMan())){
                    // 修改后不存在
                    // assignTime 指派时间 设置为null
                    saleChance.setAssignTime(null);
                    // 分配状态        0=未分配
                    saleChance.setState(StateStatus.UNSTATE.getType());
                    // 开发状态        0=未开发
                    saleChance.setDevResult(DevResult.UNDEV.getStatus());
                }
                else { //修改后的值存在
                    // 判断修改前后是否是同一个指派人
                    if(!saleChance.getAssignMan().equals(temp.getAssignMan())){
                        //    如果是，不需要操作
                        //    如果不是，则需要更新
                        saleChance.setAssignTime(new Date());
                    }
                }
            }
        }
        /* 执行更新操作，判断受影响的行数 */
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"更新操作失败");
    }
    /**
     customerName 客户名称       非空
     linkMan 联系人              非空
     linkPhone 联系号码          非空，手机号码格式正确
     *
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        // customerName 客户名称       非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        // linkMan 联系人              非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        // linkPhone 联系号码          非空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系号码不能为空");
        // linkPhone 联系号码          手机号码格式正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号码格式不正确");
    }

    /**
     * 删除营销机会
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        // 非空校验
        AssertUtil.isTrue(ids==null || ids.length<1,"待删除记录不存在");
        // 执行删除(更新)操作
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"待删除记录失败！");
    }
}
