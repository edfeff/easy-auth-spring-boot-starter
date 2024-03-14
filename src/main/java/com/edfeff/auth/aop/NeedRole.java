package com.edfeff.auth.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 要求用户登录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedRole {
    /**
     * 要求的角色集合
     */
    String[] value() default {};

    /**
     * all=true时，当前用户必须包含所有角色
     * all=false时，当前用户拥有任一角色即可
     */
    boolean all() default true;
}
