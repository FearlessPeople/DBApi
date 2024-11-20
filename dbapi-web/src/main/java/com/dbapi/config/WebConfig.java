package com.dbapi.config;

import com.dbapi.interceptor.AuthenticationInterceptor;
import com.dbapi.interceptor.LoginInterceptor;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PermissionsManager permissionsManager;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 添加静态资源映射：使文件目录中的文件可以通过URL访问。
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 处理静态资源
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 如果有上传目录的文件映射
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadDir);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 自动为所有 API 加上 `/api` 前缀
        configurer.addPathPrefix("api", c -> c.isAnnotationPresent(RestController.class));
    }

    // @Override
    // public void addViewControllers(ViewControllerRegistry registry) {
    //     // 让所有未知请求都转发到 index.html
    //     registry.addViewController("/{spring:^(?!api).+}").setViewName("forward:/index.html");
    //     registry.addViewController("/**/{spring:^(?!api).+}").setViewName("forward:/index.html");
    // }


    /**
     * 增加拦截器到Springboot中
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor(permissionsManager));
        // 权限拦截器
        registry.addInterceptor(new AuthenticationInterceptor(permissionsManager, sysUserService));

        // 拦截所有/user开头的请求
        // registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**");

    }
}
