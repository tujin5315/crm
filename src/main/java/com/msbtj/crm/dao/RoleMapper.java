package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper {
    // 只需要id与roleName
    public List<Map<String,Object>> queryAllRoles(Integer userId);
}