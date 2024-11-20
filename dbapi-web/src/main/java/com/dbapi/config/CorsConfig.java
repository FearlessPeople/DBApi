package com.dbapi.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 */
// @Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowedOrigins("*") // 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许跨域请求的方法
                .maxAge(168000) // 预检间隔时间
                .allowCredentials(true); // 是否发送cookie
    }
}
