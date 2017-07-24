package com.lidroid.xutils.view;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Author: wyouflf
 * Date: 13-9-9
 * Time: 下午12:26
 */
public interface ViewCustomEventListener {
    void setEventListener(Object handler, ViewFinder finder, Annotation annotation, Method method);
}
