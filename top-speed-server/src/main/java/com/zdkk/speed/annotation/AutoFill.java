package com.zdkk.speed.annotation;

import com.zdkk.speed.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自动填充 创建和更新时间 以及创建和更新人信息的注解
 * 用于标识需要自动填充的实体类方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
