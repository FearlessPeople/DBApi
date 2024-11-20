package com.dbapi.annotation;

import java.lang.annotation.*;

/**
 * 权限注解：用在方法上，主要用于实现调用该方法的用户必须拥有某权限才能继续
 */
@Documented
@Retention(RetentionPolicy.RUNTIME) // 运行时保留
@Target({ElementType.METHOD}) // 用于方法上
@Inherited
public @interface Permissions {
    //注解的name属性
    String value() default "";
}
