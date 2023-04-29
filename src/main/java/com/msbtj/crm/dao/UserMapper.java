package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    /**
     * 通过用户名查询用户信息
     * @param userName
     * @return
     */
    public User queryUserByName(String userName);
    /**
     * 连表查询所有的销售人员
     */
    public List<Map<String,Object>> queryAllSales();

    public int deleteByIds(Integer[] ids);

}