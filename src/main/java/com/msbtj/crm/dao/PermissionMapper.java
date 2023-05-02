package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {


    /**
     * 通过角色ID查询对应的权限记录
     * @param roleId
     * @return
     */
    Integer countPermissionByRoleId(Integer roleId);


    /**
     * 通过角色ID删除对应的权限记录
     * @param roleId
     */
    Integer deletePermissionByRoleId(Integer roleId);


    /**
     * 查询角色拥有的所有资源id集合
     * @param roleId
     * @return
     */
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    /**
     * 通过用户id，查询角色id；通过角色id，查询资源列表
     * @param userId
     * @return
     */
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    /**
     * 通过资源id查询资源权限中的数据
     * @param ModuleId
     * @return
     */
    Integer countPermissionByModuleId(Integer ModuleId);

    /**
     * 通过资源id删除资源权限中的数据
     * @param ModuleId
     */
    Integer deletePermissionByModuleId(Integer ModuleId);
}