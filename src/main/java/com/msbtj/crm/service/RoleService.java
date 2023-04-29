package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.RoleMapper;
import com.msbtj.crm.vo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有的角色信息
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }
}
