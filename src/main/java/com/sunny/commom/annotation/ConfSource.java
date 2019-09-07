package com.sunny.commom.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfSource {
    String value() default "configer.properties";
    boolean fixed() default false; // 锁定配置文件，只读取该配置
}
