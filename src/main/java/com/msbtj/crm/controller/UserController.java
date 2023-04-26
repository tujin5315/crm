package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.exceptions.ParamsException;
import com.msbtj.crm.model.UserModel;
import com.msbtj.crm.service.UserService;
import com.msbtj.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 登录功能
     *   通过形参接收客户端传递的参数
     *   调用业务逻辑层的登录方法，得到登录的结果
     *   响应数据给客户端
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        // 调用service层方法进行登录
        UserModel userModel = userService.userLogin(userName, userPwd);
        // 设置resultInfo的result值，返回用户信息
        resultInfo.setResult(userModel);
        // 通过try catch 抓住service层抛出的异常，若有异常，则登录失败，若无异常，则登录成功
        /*try{
            // 调用service层方法进行登录
            UserModel userModel = userService.userLogin(userName, userPwd);
            // 设置resultInfo的result值，返回用户信息
            resultInfo.setResult(userModel);
        }
        catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(p.getCode());
            p.printStackTrace();
        }
        catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登录失败");
        }*/

        return resultInfo;
    }

    /**
     *  1.通过形参接收前端传递的参数 （原始密码，新密码 确认Miami）
     *  2.通过request对象，获取设置在cookie中的用户id
     *  3.调用service层修改密码的功能，得到resultInfo对象
     *  4.返回resultinfo对象
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @ResponseBody
    @PostMapping("updatePwd")
    public ResultInfo updateUserPwd(HttpServletRequest request,
            String oldPassword,String newPassword,String repeatPassword){
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用service层的修改密码方法
        userService.updateUserPwdByUserId(userId,oldPassword,newPassword,repeatPassword);
        /*try {
            // 通过工具类中的方法获取userId
            // 通过cookie获取对象的id
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            // 调用service层的修改密码方法
            userService.updateUserPwdByUserId(userId,oldPassword,newPassword,repeatPassword);
        }
        catch (ParamsException p){
            resultInfo.setMsg(p.getMsg());
            resultInfo.setCode(p.getCode());
            p.printStackTrace();
        }
        catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败");
            e.printStackTrace();
        }*/
        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }
    /**
     * 查询所有的销售人员
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }
}
