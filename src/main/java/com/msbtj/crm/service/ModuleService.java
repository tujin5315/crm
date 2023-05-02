package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.ModuleMapper;
import com.msbtj.crm.dao.PermissionMapper;
import com.msbtj.crm.model.TreeModel;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询所有资源 ---- 展示在资源页面
     */
    public Map<String,Object> queryModules(){
        Map<String,Object> moduleMap = new HashMap<>();
        // 查询资源列表
        List<Module> modules = moduleMapper.queryModules();
        moduleMap.put("code",0);
        moduleMap.put("msg","");
        moduleMap.put("count",modules.size());
        moduleMap.put("data",modules);
        return moduleMap;
    }


    /**
     * 添加资源操作
     1.参数校验
        资源名称    moduleName
            非空，同层级下唯一
        地址  url
            二级菜单    非空且同层级菜单下不可重复
        权限码     optvalue
            非空，不可重复
        父级菜单    parentId
            若是一级菜单，则没有父级
            若是二级或三级菜单，则非空，且必须存在
        层级  grade
            非空，且值为 0|1|2
     2.设置默认值
        是否有效    isValid
        创建时间    createDate
        更新时间    updateDate
     3.执行添加操作，判断受影响的行数
      * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModules(Module module){
        /* 参数校验 */
        Integer grade = module.getGrade();
        // 层级  grade
        // 层级  非空
        AssertUtil.isTrue(null == grade,"层级不能为空，请选择某一层级");
        // 层级   值为 0|1|2
        AssertUtil.isTrue(!(grade == 0 || grade==1 || grade == 2),"层级的选择不正确，请重新选择");
        // 资源名称    moduleName
        // moduleName  非空
        AssertUtil.isTrue(null == module.getModuleName(),"请为添加的功能命名");
        // moduleName  同层级下唯一
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName()),"菜单名称必须唯一");
        // 地址  url
        // 二级菜单 grade==1    非空且同层级菜单下不可重复
        if(grade==1){
            // 二级菜单  非空
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"菜单地址不能为空");
            // 二级菜单  同层级菜单下不可重复
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl()),"同层级菜单下不可重复");
        }
        // 父级菜单    parentId
        // 若是一级菜单，则没有父级
        // 设置父目录
        if(grade==0){
            module.setParentId(-1);
        }
        if(grade != 0){
            // 若是二级或三级菜单，非空
            AssertUtil.isTrue(null == module.getParentId(),"父级菜单不能为空");
            // 若是二级或三级菜单，必须存在
            AssertUtil.isTrue(null ==moduleMapper.selectByPrimaryKey(module.getParentId()),"请选择正确的父级菜单");
        }
        // 权限码     optvalue
        // 权限码  非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        // 权限码  不可重复
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByOptValue(module.getOptValue()),"权限码已存在！");
        /* 设置默认值 */
        // 是否有效    isValid  默认有效  1
        module.setIsValid((byte) 1);
        // 创建时间    createDate   系统当前时间  new Date
        module.setCreateDate(new Date());
        // 更新时间    updateDate   系统当前时间  new Date
        module.setUpdateDate(new Date());
        /* 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(moduleMapper.insertSelective(module) < 1,"添加角色功能失败！,请重试");

    }

    /**
     * 修改资源
     * 1.参数校验
       id
           非空，存在
       层级
           非空 值为 0、1、2
       模块名称
           非空，同层唯一  (不包含本身名字)
       地址
           二级菜单 非空同层不可重复(不包含当前修改本身)
       权限码
           非空，不可重复(不包含本身)
     * 2.设置参数的默认值
     *  修改更新时间
     * 3.执行更新操作，判断受影响的行数
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        /* 参数校验 */
        // id  非空
        AssertUtil.isTrue(null == module.getId(),"待更新记录不存在");
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        // id  存在
        AssertUtil.isTrue(temp == null,"待更新记录对象不存在");
        // 层级 非空   值为 0、1、2
        Integer grade = module.getGrade();
        AssertUtil.isTrue( null == grade || !(grade ==0 || grade == 1 || grade ==2),"待更新更记录层级合法");
        // 模块名称 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"待更新模块名称不能为空");
        //  同层唯一  (不包含本身名字)
        // 通过模块名称与层级查询资源
        Module module1 = moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName());
        if(module1!=null){
            AssertUtil.isTrue(!(module1.getId().equals(temp.getId())),"该层级下菜单名已存在");
        }
        // 地址  二级菜单(grade == 1) 非空同层不可重复(不包含当前修改本身)
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"菜单url不能为空");
            // 通过层级与菜单url查询资源对象
            temp = moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            // 判断是否存在
            if(temp!=null){
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下菜单url已存在");
            }
        }
        //权限码  非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        //权限码  不可重复(不包含本身)
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        //判断是否为空
        if(temp!=null){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"权限码已存在");
        }
        /* 设置参数的默认值 */
        module.setUpdateDate(new Date());
        /* 执行更新操作，判断受影响的行数 */
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)!=1,"待更新记录失败！");
    }

    /**
     * 删除资源功能
     * 1.参数校验
          查询删除数据是否拥有子记录
          查询删除资源是否有权限
          删除对应id的资源权限
       2.设置默认值
       3.执行删除操作，判断受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        /* 参数校验 */
        // id的非空校验
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        // 通过id查询资源
        Module temp = moduleMapper.selectByPrimaryKey(id);
        // 非空校验查询对象
        /* 查询删除数据是否拥有子记录 */
        AssertUtil.isTrue(null == temp,"待删除记录对象不存在");
        // 查询该删除资源是否拥有子资源(把当前id当作是父id去进行查询)
        Integer count = moduleMapper.queryModuleByParentId(id);
        // 若存在子资源，则无法删除
        AssertUtil.isTrue(count>0,"待删除记录存在子资源，无法删除");
        /* 查询删除资源是否有权限(是否存在数据) */
        count = permissionMapper.countPermissionByModuleId(id);
        // 判断是否存在，存在则进行删除
        if(count>0){
            permissionMapper.deletePermissionByModuleId(id);
        }
        /* 设置默认值 */
        // 设置记录无效
        temp.setIsValid((byte) 0);
        // 设置更新时间
        temp.setUpdateDate(new Date());
        /* 执行删除操作，判断受影响的行数 */
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp)<1,"待删除资源失败");
    }
}
