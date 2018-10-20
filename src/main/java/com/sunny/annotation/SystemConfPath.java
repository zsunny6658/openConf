package com.sunny.annotation;

import java.lang.annotation.*;

//TODO 获取系统配置项内容
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemConfPath {

    public String value() default "";

}
