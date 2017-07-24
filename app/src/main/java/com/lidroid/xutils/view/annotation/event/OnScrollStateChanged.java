package com.lidroid.xutils.view.annotation.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wyouflf
 * Date: 13-9-12
 * Time: 下午11:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase
public @interface OnScrollStateChanged {
    int[] value();

    int[] parentId() default 0;
}
