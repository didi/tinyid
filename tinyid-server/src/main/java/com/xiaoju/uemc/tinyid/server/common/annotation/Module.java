package com.xiaoju.uemc.tinyid.server.common.annotation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(ModuleCondition.class)
public @interface Module {

    /**
     * 前缀
     */
    String prefix() default "";

    /**
     * 匹配的值
     */
    String[] value();
}
