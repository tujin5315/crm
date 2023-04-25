package com.msbtj.crm.config;

import com.msbtj.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean // 返回的是方法的实例对象
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 需要一个实现了拦截器功能的实现类对象
        registry.addInterceptor(noLoginInterceptor())
                // 设置拦截路径
                .addPathPatterns("/**")
                // 设置放行的资源路径
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");

    }
}
