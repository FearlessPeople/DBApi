package com.dbapi.config;

import com.dbapi.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    // 创建Docket Bean，用于配置Swagger的核心组件
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // 指定Swagger的文档类型为OpenAPI 3.0
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fz.controller")) // 设置要扫描的包，生成API文档
                .paths(PathSelectors.any()) // 设置路径筛选器，这里表示对所有路径生成文档
                .build()
                .apiInfo(apiInfo()) // 设置API文档的基本信息
                .securityContexts(Collections.singletonList(securityContext())) // 设置全局安全上下文，用于身份认证
                .securitySchemes(Collections.singletonList(apiKey())); // 设置API的安全方案，比如JWT
    }

    // 配置API文档的基本信息，比如标题、描述、版本等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SimpleAdmin接口文档") // 文档标题
                .description("SimpleAdmin接口文档") // 文档描述
                .version("1.0.0") // 文档版本
                .build();
    }

    // 配置API Key，用于在Swagger UI中添加Authorization头信息
    private ApiKey apiKey() {
        return new ApiKey("JWT", Constants.Authorization, "header"); // 参数分别为名称、key和传递方式，这里表示在请求头中传递JWT
    }

    // 配置全局的安全上下文，指定哪些接口需要身份认证
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth()) // 关联默认的安全参考，即JWT
                .build();
    }

    // 配置默认的安全参考，用于在安全上下文中声明认证方式和作用域
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); // 定义授权范围，这里表示全局作用域
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; // 创建授权范围数组
        authorizationScopes[0] = authorizationScope; // 将授权范围放入数组中
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes)); // 返回包含JWT安全参考的列表
    }
}
