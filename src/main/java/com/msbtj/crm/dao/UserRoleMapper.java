package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.UserRole;



public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    // 根据用户id查询角色信息
    public Integer countRoleByUserId(Integer id);
    // 根据用户id删除角色信息
    public Integer delUserByCountUser(Integer id);
}