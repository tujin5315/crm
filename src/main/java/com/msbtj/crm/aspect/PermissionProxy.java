package com.msbtj.crm.aspect;

import com.msbtj.crm.annotation.RequiredPermission;
import com.msbtj.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;

    /**
     * 切面拦截注解  判断角色拥有的权限
     * 拦截@annotation(com.msbtj.crm.annotation.RequiredPermission)
     * @param pjp
     * @return
     */
    @Around(value = "@annotation(com.msbtj.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        // 得到当前登录用户拥有的权限
        List<String> permission = (List<String>) session.getAttribute("permission");
        // 判断用户是否拥有权限
        if(null == permission || permission.size()<1){
            // 抛出异常
            throw new AuthException();
        }
        // 得到对应的目标
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 得到方法上的注解
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // 判断注解上对应的状态码
        if(!(permission.contains(requiredPermission.code()))){
            // 若权限中不包含当前方法上注解指定的权限码，则抛出异常
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }
}
