package com.dbapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录注解：用在方法上，主要用于实现该方法必须登录后才可访问
 */
@Target(ElementType.METHOD) // 用于方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时保留
public @interface RequireLogin {
}
