package com.sunny.annotation;

import java.lang.annotation.*;

/**
 * @Author zsunny
 * @Date 2019/4/18 11:23
 * @Mail zsunny@yeah.net
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dynamic {
}
