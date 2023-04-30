package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.ModuleMapper;
import com.msbtj.crm.dao.PermissionMapper;
import com.msbtj.crm.model.TreeModel;
import com.msbtj.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource

    private PermissionMapper permissionMapper;

    /**
     * 查询所有的资源列表
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        // 查询所有的资源列表
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        // 查询指定角色已经授权过的资源列表(查询角色拥有的资源ID)(通过角色id查询授权)
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        // 判断角色是否拥有资源id
        if(permissionIds != null && permissionIds.size()>0){
            // 循环所有的资源列表，判断用户拥有的资源id中是否有匹配的，如果有，则设置checked属性为true
            treeModelList.forEach(treeModel ->{
                // 判断角色拥有的资源id中是否有当前遍历的资源id
                if(permissionIds.contains(treeModel.getId())){
                    // 如果包含，则说明角色授权郭，设置为true
                    treeModel.setChecked(true);
                }
            } );
        }
        return treeModelList;
    }
}
