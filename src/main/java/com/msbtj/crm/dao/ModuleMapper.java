package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.model.TreeModel;
import com.msbtj.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 查询所有的资源列表
    public List<TreeModel> queryAllModules();
    // 查找所有的资源信息  放置在资源页面
    public List<Module> queryModules();
    // 通过菜单名称查找同层级下的资源信息
    Module queryModuleByGradeAndModuleName(@Param("grade") int grade, @Param("moduleName") String moduleName);
    // 通过url地址查找同层级下的资源信息
    Module queryModuleByGradeAndUrl(@Param("grade")int grade, @Param("url")String url);
    // 通过权限码查找资源信息
    Module queryModuleByOptValue(String optValue);
    // 通过父id查询拥有的子资源数量
    Integer queryModuleByParentId(Integer parentId);
}