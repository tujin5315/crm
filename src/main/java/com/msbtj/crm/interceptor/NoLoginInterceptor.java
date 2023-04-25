package com.msbtj.crm.interceptor;

import com.msbtj.crm.dao.UserMapper;
import com.msbtj.crm.exceptions.NoLoginException;
import com.msbtj.crm.utils.LoginUserUtil;
import com.msbtj.crm.vo.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    // 注入mapper
    @Resource
    private UserMapper userMapper;
    /**
     * 拦截非法登录页面
     *   preHandle方法 返回值为boolean
     *      若返回值为true 则代表可以允许方法
     *      若返回值为false，则代表阻止此方法执行
     *
     *   判断用户登录信息
     *    1.通过cookie查询用户登录信息
     *    2.若cookie中的id与数据库的id信息匹配，则允许登录，否则拒绝登录
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户的id 通过cookie来获取信息
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        /*if(null==userId){
            throw new NoLoginException();
        }
        // 利用userMapper id查询用户信息 有可能报错 ---- 报错原因  id为null
        User user = userMapper.selectByPrimaryKey(userId);
        // 判断用户信息，若用户信息存在，则返回true，允许登录。否则，阻止访问，并跳转到登录界面
        if(user == null){
            throw new NoLoginException();
        }*/
        // 判断用户id以及根据id查询的用户是否存在
        if(userId == 0 || null == userMapper.selectByPrimaryKey(userId)){
            throw new NoLoginException();
        }
        return true;
    }
}
