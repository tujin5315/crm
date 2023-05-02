package com.msbtj.crm;

import com.alibaba.fastjson.JSON;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.exceptions.AuthException;
import com.msbtj.crm.exceptions.NoLoginException;
import com.msbtj.crm.exceptions.ParamsException;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     *
     * @param request request请求对象
     * @param response response响应对象
     * @param handler 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /**
         * 非法拦截请求
         *    先判断异常是否为拦截异常
         *    然后重定向到登录页面
         */
        if(e instanceof NoLoginException){
            // 重定向跳转
            ModelAndView mv = new ModelAndView("redirect:/index");
            // 返回
            return mv;
        }
        /**
         * 设置默认值
         */
        ModelAndView mv = new ModelAndView();
        // 报错视图名
        mv.setViewName("error");
        mv.addObject("code",500);
        mv.addObject("msg","系统异常，请重试...");

        // 判断handkerMethod
        if(handler instanceof HandlerMethod){
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上声明的@ResponseBody注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断ResponseBody对象是否为空 如果为空，返回视图 如果不为空 返回json
            if(responseBody == null){
                /**
                 * 返回视图
                 */
                // 判断是否为自定义异常
                if(e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    // 设置异常信息
                    mv.addObject("code",p.getCode());
                    mv.addObject("msg",p.getMsg());
                }
                // 判断是否为权限认证异常
                else if (e instanceof AuthException) {
                    AuthException a = (AuthException) e;
                    // 设置异常信息
                    mv.addObject("code",a.getCode());
                    mv.addObject("msg",a.getMsg());
                }
                return mv;
            }else {
                /**
                 * 返回数据
                 */
                // 设置默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试");
                // 判断异常是否为自定义异常
                if(e instanceof ParamsException){
                    ParamsException p = (ParamsException) e;
                    resultInfo.setMsg(p.getMsg());
                    resultInfo.setCode(p.getCode());
                } else if (e instanceof AuthException) {
                    AuthException a = (AuthException) e;
                    resultInfo.setMsg(a.getMsg());
                    resultInfo.setCode(a.getCode());

                }
                // 设置响应类型以及编码格式
                response.setContentType("application/json;charset=UTF-8");
                // 得到一个字符输出流
                PrintWriter writer = null;
                try{
                    // 得到输出流
                    writer = response.getWriter();
                    // 将需要返回的对象转换成json格式的字符
                    String json = JSON.toJSONString(resultInfo);
                    // 输出数据
                    writer.write(json);
                }catch (IOException ioe){
                    e.printStackTrace();
                }
                finally {
                    // 若对象不为空，则关闭  对象不为空，说明得到了writer对象，并且进行了数据输出。
                    if(writer != null){
                        writer.close();
                    }
                }
                return null;
            }
        }
        return mv;
    }
}
