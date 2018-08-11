package com.sunny.conf;

import java.lang.annotation.*;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfPath {

    public String value() default "";

}
