/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.core.DoubleKeyValueMap;
import com.lidroid.xutils.view.ResLoader;
import com.lidroid.xutils.view.ViewCommonEventListener;
import com.lidroid.xutils.view.ViewCustomEventListener;
import com.lidroid.xutils.view.ViewFinder;
import com.lidroid.xutils.view.ViewInjectInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.PreferenceInject;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.EventBase;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ViewUtils {

    private ViewUtils() {
    }

    private static ConcurrentHashMap<Class<? extends Annotation>, ViewCustomEventListener> annotationType_viewCustomEventListener_map;

    /**
     * register Custom Annotation
     *
     * @param annotationType The type of custom annotation must be annotated by @EventBase.
     * @param listener
     */
    public static void registerCustomAnnotation(Class<? extends Annotation> annotationType, ViewCustomEventListener listener) {
        if (annotationType_viewCustomEventListener_map == null) {
            annotationType_viewCustomEventListener_map = new ConcurrentHashMap<Class<? extends Annotation>, ViewCustomEventListener>();
        }
        annotationType_viewCustomEventListener_map.put(annotationType, listener);
    }

    public static void inject(View view) {
        injectObject(view, new ViewFinder(view));
    }

    public static void inject(Activity activity) {
        injectObject(activity, new ViewFinder(activity));
    }

    public static void inject(PreferenceActivity preferenceActivity) {
        injectObject(preferenceActivity, new ViewFinder(preferenceActivity));
    }

    public static void inject(Object handler, View view) {
        injectObject(handler, new ViewFinder(view));
    }

    public static void inject(Object handler, Activity activity) {
        injectObject(handler, new ViewFinder(activity));
    }

    public static void inject(Object handler, PreferenceGroup preferenceGroup) {
        injectObject(handler, new ViewFinder(preferenceGroup));
    }

    public static void inject(Object handler, PreferenceActivity preferenceActivity) {
        injectObject(handler, new ViewFinder(preferenceActivity));
    }


    @SuppressWarnings("ConstantConditions")
    private static void injectObject(Object handler, ViewFinder finder) {

        Class<?> handlerType = handler.getClass();

        // inject ContentView
        if (Activity.class.isAssignableFrom(handlerType)) {
            ContentView contentView = handlerType.getAnnotation(ContentView.class);
            if (contentView != null) {
                ((Activity) handler).setContentView(contentView.value());
            }
        }

        // inject view
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if (viewInject != null) {
                    try {
                        View view = finder.findViewById(viewInject.value(), viewInject.parentId());
                        if (view != null) {
                            field.setAccessible(true);
                            field.set(handler, view);
                        }
                    } catch (Throwable e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                } else {
                    ResInject resInject = field.getAnnotation(ResInject.class);
                    if (resInject != null) {
                        try {
                            Object res = ResLoader.loadRes(
                                    resInject.type(), finder.getContext(), resInject.id());
                            if (res != null) {
                                field.setAccessible(true);
                                field.set(handler, res);
                            }
                        } catch (Throwable e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                    } else {
                        PreferenceInject preferenceInject = field.getAnnotation(PreferenceInject.class);
                        if (preferenceInject != null) {
                            try {
                                Preference preference = finder.findPreference(preferenceInject.value());
                                if (preference != null) {
                                    field.setAccessible(true);
                                    field.set(handler, preference);
                                }
                            } catch (Throwable e) {
                                LogUtils.e(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }

        // inject event
        Method[] methods = handlerType.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            String eventName = OnClick.class.getCanonicalName();
            String prefix = eventName.substring(0, eventName.lastIndexOf('.'));
            DoubleKeyValueMap<ViewInjectInfo, Annotation, Method> info_annotation_method_map = new DoubleKeyValueMap<ViewInjectInfo, Annotation, Method>();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        Class<?> annType = annotation.annotationType();
                        if (annType.getAnnotation(EventBase.class) != null) {
                            if (annType.getCanonicalName().startsWith(prefix)) {
                                try {
                                    // ProGuard：-keep class * extends java.lang.annotation.Annotation { *; }
                                    Method valueMethod = annType.getDeclaredMethod("value");
                                    Method parentIdMethod = null;
                                    try {
                                        parentIdMethod = annType.getDeclaredMethod("parentId");
                                    } catch (Throwable e) {
                                    }
                                    Object values = valueMethod.invoke(annotation);
                                    Object parentIds = parentIdMethod == null ? null : parentIdMethod.invoke(annotation);
                                    int parentIdsLen = parentIds == null ? 0 : Array.getLength(parentIds);
                                    int len = Array.getLength(values);
                                    for (int i = 0; i < len; i++) {
                                        ViewInjectInfo info = new ViewInjectInfo();
                                        info.value = Array.get(values, i);
                                        info.parentId = parentIdsLen > i ? (Integer) Array.get(parentIds, i) : 0;
                                        info_annotation_method_map.put(info, annotation, method);
                                    }
                                } catch (Throwable e) {
                                    LogUtils.e(e.getMessage(), e);
                                }
                            } else {
                                ViewCustomEventListener listener = annotationType_viewCustomEventListener_map.get(annType);
                                if (listener != null) {
                                    listener.setEventListener(handler, finder, annotation, method);
                                }
                            }
                        }
                    }
                }
            }
            ViewCommonEventListener.setAllEventListeners(handler, finder, info_annotation_method_map);
        }
    }

}
