package com.edfeff.auth.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 要求具有某个权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPerm {
    /**
     * 要求的权限集合
     */
    String[] value() default {};

    /**
     * all=true时，当前用户必须包含所有权限
     * all=false时，当前用户拥有任一权限即可
     */
    boolean all() default true;

}
