package com.bindada.syscourse.config;

import com.bindada.syscourse.interceptors.JwtInterceptor;
import com.bindada.syscourse.util.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/user/**","/student/**","/course/**","/class/**")         //拦截所有接口
                .excludePathPatterns("/user/login")                                        //其他接口放行
                .excludePathPatterns("/course/getCommit/**");
    }
}
