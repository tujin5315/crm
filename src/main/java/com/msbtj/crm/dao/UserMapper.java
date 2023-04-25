package com.msbtj.crm.dao;

import com.msbtj.crm.base.BaseMapper;
import com.msbtj.crm.vo.User;

public interface UserMapper extends BaseMapper<User,Integer> {
    public User queryUserByName(String userName);
}