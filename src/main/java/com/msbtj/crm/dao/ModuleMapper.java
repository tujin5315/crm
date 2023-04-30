package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.model.TreeModel;
import com.msbtj.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 查询所有的资源列表
    public List<TreeModel> queryAllModules();

}