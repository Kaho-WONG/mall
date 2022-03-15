package com.mars.mall;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 拦截器配置类，需要实现WebMvcConfigurer接口
 * @author: Mars
 * @create: 2021-09-29 21:30
 **/
@Configuration //@Configuration用于定义配置类，可替换xml配置文件
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * addInterceptors方法可以使用registry参数添加拦截器并设置拦截的url
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //注册UserLoginInterceptor拦截器，默认拦截除"/user/register","/user/login"以外的所有url请求
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/register","/user/login","/categories","/products",
                        "/products/*","/error");
    }
}
