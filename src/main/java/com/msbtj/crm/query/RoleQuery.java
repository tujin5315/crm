package com.msbtj.crm.query;

import com.msbtj.crm.base.BaseQuery;



public class RoleQuery extends BaseQuery {
    private String roleName; //角色名称  用来多条件查询的

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

