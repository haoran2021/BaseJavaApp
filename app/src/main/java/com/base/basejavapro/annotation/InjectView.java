package com.base.basejavapro.annotation;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <li>Package: com.base.basejavapro.annotation</li>
 * <li>Author: lihaoran</li>
 * <li>Date:  9/15/21</li>
 * <li>Description: </li>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectView {
    @IdRes int value();
}
