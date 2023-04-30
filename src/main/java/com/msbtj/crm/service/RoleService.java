package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.ModuleMapper;
import com.msbtj.crm.dao.PermissionMapper;
import com.msbtj.crm.dao.RoleMapper;
import com.msbtj.crm.query.RoleQuery;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.vo.Permission;
import com.msbtj.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色信息
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }


    /**
     * 添加角色
     * 1、参数校验
     *  角色名     非空，唯一
     * 2、设置参数的默认值
     *  是否有效
     *  创建时间
     *  更新时间
     * 3、执行添加语句操作
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        // 参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        // 唯一性判定
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"角色名已存在");
        // 设置参数的默认值
        // 设置有效值
        role.setIsValid(1);
        // 创建时间 系统当前时间
        role.setCreateDate(new Date());
        // 更新时间 系统当前时间
        role.setUpdateDate(new Date());
        // 执行添加语句操作
        AssertUtil.isTrue(roleMapper.insertSelective(role)<1,"角色添加失败");

    }

    /**
     * 修改角色信息
     * 1.参数校验
     * 2.修改信息
     * 3.执行更新操作，判断受影响行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        /* 参数校验 */
        // 角色id不能为空
        AssertUtil.isTrue(null == role.getId(),"角色id不能为空");
        // 通过角色id查询该条信息 是否存在
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在");
        // 角色名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        // 通过角色名称查询角色记录
        temp = roleMapper.selectByRoleName(role.getRoleName());
        // 判断角色记录是否存在(若不存在，表示可使用；若存在，且角色ID与当前更新的角色ID不一致，表示角色名称不可用)
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getId())),"角色名称不可用");
        /* 修改信息 */
        role.setUpdateDate(new Date());
        /* 执行更新操作，判断受影响行数 */
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)!=1,"角色更新失败");
    }

    /**
     * 删除角色信息
     * 1、参数校验
     * 2、设置默认值
     * 3、执行删除操作
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        /* 参数校验 id的参数以及查询校验 */
        AssertUtil.isTrue(null == roleId,"待删除角色id存在异常，请重试");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == role,"待删除角色信息不存在");
        /* 设置默认值 1、是否有效  2、更新时间*/
        role.setUpdateDate(new Date());
        role.setIsValid(0);
        /* 执行删除(更新操作) */
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)!=1,"删除角色失败");
    }

    /**
     * 角色授权
     *
     *  将对应的角色ID与资源ID，添加到对应的权限表中
     *      直接添加权限不合适  因为会出现重复的权限数据(执行修改权限操作后删除权限操作时)
     *      推荐使用：
     *          先将已有的权限记录删除，再将需要设置的权限记录添加
     *          1.通过角色ID查询对应的权限记录
     *          2.如果权限记录存在，则删除对应的角色拥有的权限记录
     *          3.如果有权限记录，则添加权限记录
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        // 参数校验
        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp==null || null == roleId,"待授权角色不存在");
        // 添加授权信息
        // 1.通过角色ID查询对应的权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        // 2.如果权限记录存在，则删除对应的角色拥有的权限记录
        if(count>0){
            // 删除权限记录
            AssertUtil.isTrue(count>permissionMapper.deletePermissionByRoleId(roleId),"待授权角色失败！");

        }
        // 3.如果有权限记录，则添加权限记录
        if(mIds != null && mIds.length>0){
            // 定义Permission集合
            List<Permission> permissionList = new ArrayList<>();

            // 遍历资源ID数组
            for(Integer mId:mIds){
                Permission permission = new Permission();
                // 设置值
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                // 将对象设置到集合中
                permissionList.add(permission);
            }
            // 执行批量添加操作，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!=permissionList.size(),"角色授权失败");
        }

    }
}
