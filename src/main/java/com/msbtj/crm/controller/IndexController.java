package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.service.UserService;
import com.msbtj.crm.utils.LoginUserUtil;
import com.msbtj.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController{
    @Resource
    private UserService userService;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        // 获取cookie中的id 通过LoginUserUtil
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 查询用户对象 设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        // 把user对象设置到session中
        request.getSession().setAttribute("user",user);
        return "main";
    }

}
