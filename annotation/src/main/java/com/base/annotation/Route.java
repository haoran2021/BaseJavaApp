package com.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <li>Package: com.base.compiler.annotation</li>
 * <li>Author: lihaoran</li>
 * <li>Date:  7/16/21</li>
 * <li>Description: </li>
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Route {
    /**
     *路由的路径，标识一个路由节点
     */
    String path();
    /**
     * 将路由节点进行分组，可以实现按组动态加载
     */
    String group() default "";
}
