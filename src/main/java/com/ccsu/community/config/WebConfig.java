package com.ccsu.community.config;

import com.ccsu.community.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 华华
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean<HttpPutFormContentFilter> testFilterRegistration2() {
        FilterRegistrationBean<HttpPutFormContentFilter> registration = new FilterRegistrationBean<HttpPutFormContentFilter>();
        //添加过滤器
        registration.setFilter(new HttpPutFormContentFilter());
        //设置过滤路径，/*所有路径
        registration.addUrlPatterns("/*");
        registration.setName("HttpPutFormContentFilter");
        //设置优先级
        registration.setOrder(2);
        return registration;
    }

}
