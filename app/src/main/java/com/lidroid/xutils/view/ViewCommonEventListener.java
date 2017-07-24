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

package com.lidroid.xutils.view;

import android.preference.Preference;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TabHost;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.core.DoubleKeyValueMap;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnGroupClick;
import com.lidroid.xutils.view.annotation.event.OnGroupCollapse;
import com.lidroid.xutils.view.annotation.event.OnGroupExpand;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;
import com.lidroid.xutils.view.annotation.event.OnItemSelected;
import com.lidroid.xutils.view.annotation.event.OnKey;
import com.lidroid.xutils.view.annotation.event.OnLongClick;
import com.lidroid.xutils.view.annotation.event.OnNothingSelected;
import com.lidroid.xutils.view.annotation.event.OnPreferenceChange;
import com.lidroid.xutils.view.annotation.event.OnPreferenceClick;
import com.lidroid.xutils.view.annotation.event.OnProgressChanged;
import com.lidroid.xutils.view.annotation.event.OnScroll;
import com.lidroid.xutils.view.annotation.event.OnScrollChanged;
import com.lidroid.xutils.view.annotation.event.OnScrollStateChanged;
import com.lidroid.xutils.view.annotation.event.OnStartTrackingTouch;
import com.lidroid.xutils.view.annotation.event.OnStopTrackingTouch;
import com.lidroid.xutils.view.annotation.event.OnTabChange;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ViewCommonEventListener implements
        OnClickListener,
        OnLongClickListener,
        OnFocusChangeListener,
        OnKeyListener,
        OnTouchListener,
        OnItemClickListener,
        OnItemLongClickListener,
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnGroupCollapseListener,
        ExpandableListView.OnGroupExpandListener,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener,
        Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener,
        TabHost.OnTabChangeListener,
        ViewTreeObserver.OnScrollChangedListener,
        AbsListView.OnScrollListener,
        OnItemSelectedListener,
        SeekBar.OnSeekBarChangeListener {

    private final Object handler;
    private final Method[] methods;

    public ViewCommonEventListener(Object handler, Method... methods) {
        this.handler = handler;
        this.methods = methods;
    }

    @Override
    public void onClick(View v) {
        try {
            methods[0].invoke(handler, v);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        try {
            return (Boolean) methods[0].invoke(handler, v);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        try {
            methods[0].invoke(handler, view, b);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        try {
            return (Boolean) methods[0].invoke(handler, view, i, keyEvent);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            return (Boolean) methods[0].invoke(handler, view, motionEvent);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            methods[0].invoke(handler, parent, view, position, id);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            return (Boolean) methods[0].invoke(handler, parent, view, position, id);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
        try {
            return (Boolean) methods[0].invoke(handler, expandableListView, view, i, i2, l);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        try {
            return (Boolean) methods[0].invoke(handler, expandableListView, view, i, l);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void onGroupCollapse(int i) {
        try {
            methods[0].invoke(handler, i);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onGroupExpand(int i) {
        try {
            methods[0].invoke(handler, i);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        try {
            methods[0].invoke(handler, group, checkedId);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            methods[0].invoke(handler, buttonView, isChecked);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        try {
            return (Boolean) methods[0].invoke(handler, preference);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        try {
            return (Boolean) methods[0].invoke(handler, preference, newValue);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void onTabChanged(String tabId) {
        try {
            methods[0].invoke(handler, tabId);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onScrollChanged() {
        try {
            methods[0].invoke(handler);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    // #region AbsListView.OnScrollListener
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        try {
            methods[0].invoke(handler, absListView, i);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (methods.length < 2 || methods[1] == null) {
            LogUtils.w("onScroll not implement");
            return;
        }
        try {
            methods[1].invoke(handler, absListView, i, i2, i3);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
    // #endregion AbsListView.OnScrollListener

    // #region OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            methods[0].invoke(handler, parent, view, position, id);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (methods.length < 2 || methods[1] == null) {
            LogUtils.w("onNothingSelected not implement");
            return;
        }
        try {
            methods[1].invoke(handler, parent);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
    // #endregion OnItemSelectedListener


    // #region SeekBar.OnSeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {
            methods[0].invoke(handler, seekBar, progress, fromUser);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (methods.length < 2 || methods[1] == null) {
            LogUtils.w("onStartTrackingTouch not implement");
            return;
        }
        try {
            methods[1].invoke(handler, seekBar);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (methods.length < 3 || methods[2] == null) {
            LogUtils.w("onStopTrackingTouch not implement");
            return;
        }
        try {
            methods[2].invoke(handler, seekBar);
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
    // #endregion SeekBar.OnSeekBarChangeListener

    @SuppressWarnings("ConstantConditions")
    public static void setAllEventListeners(
            Object handler,
            ViewFinder finder,
            DoubleKeyValueMap<ViewInjectInfo, Annotation, Method> info_annotation_method_map) {

        for (ViewInjectInfo info : info_annotation_method_map.getFirstKeys()) {
            ConcurrentHashMap<Annotation, Method> annotation_method_map = info_annotation_method_map.get(info);
            for (Annotation annotation : annotation_method_map.keySet()) {
                try {
                    Method method = annotation_method_map.get(annotation);
                    if (annotation instanceof OnClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.setOnClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnClickListener", OnClickListener.class, listener);
                    } else if (annotation instanceof OnLongClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.setOnLongClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnLongClickListener", OnLongClickListener.class, listener);
                    } else if (annotation instanceof OnFocusChange) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.setOnFocusChangeListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnFocusChangeListener", OnFocusChangeListener.class, listener);
                    } else if (annotation instanceof OnKey) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.setOnKeyListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnKeyListener", OnKeyListener.class, listener);
                    } else if (annotation instanceof OnTouch) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.setOnTouchListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnTouchListener", OnTouchListener.class, listener);
                    } else if (annotation instanceof OnItemClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((AdapterView<?>) view).setOnItemClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnItemClickListener", OnItemClickListener.class, listener);
                    } else if (annotation instanceof OnItemLongClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((AdapterView<?>) view).setOnItemLongClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnItemLongClickListener", OnItemLongClickListener.class, listener);
                    } else if (annotation instanceof OnChildClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((ExpandableListView) view).setOnChildClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnChildClickListener", ExpandableListView.OnChildClickListener.class, listener);
                    } else if (annotation instanceof OnGroupClick) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((ExpandableListView) view).setOnGroupClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnGroupClickListener", ExpandableListView.OnGroupClickListener.class, listener);
                    } else if (annotation instanceof OnGroupCollapse) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((ExpandableListView) view).setOnGroupCollapseListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnGroupCollapseListener", ExpandableListView.OnGroupCollapseListener.class, listener);
                    } else if (annotation instanceof OnGroupExpand) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((ExpandableListView) view).setOnGroupExpandListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnGroupExpandListener", ExpandableListView.OnGroupExpandListener.class, listener);
                    } else if (annotation instanceof OnCheckedChange) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        if (view instanceof RadioGroup) {
                            //((RadioGroup) view).setOnCheckedChangeListener(new ViewCommonEventListener(handler, method));
                            ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                            setEventListener(view, "setOnCheckedChangeListener", RadioGroup.OnCheckedChangeListener.class, listener);
                        } else if (view instanceof CompoundButton) {
                            //((CompoundButton) view).setOnCheckedChangeListener(new ViewCommonEventListener(handler, method));
                            ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                            setEventListener(view, "setOnCheckedChangeListener", CompoundButton.OnCheckedChangeListener.class, listener);
                        }
                    } else if (annotation instanceof OnPreferenceClick) {
                        Preference preference = finder.findPreference(info.value.toString());
                        if (preference == null) break;
                        //preference.setOnPreferenceClickListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(preference, "setOnPreferenceClickListener", Preference.OnPreferenceClickListener.class, listener);
                    } else if (annotation instanceof OnPreferenceChange) {
                        Preference preference = finder.findPreference(info.value.toString());
                        if (preference == null) break;
                        //preference.setOnPreferenceChangeListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(preference, "setOnPreferenceChangeListener", Preference.OnPreferenceChangeListener.class, listener);
                    } else if (annotation instanceof OnTabChange) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //((TabHost) view).setOnTabChangedListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "setOnTabChangedListener", TabHost.OnTabChangeListener.class, listener);
                    } else if (annotation instanceof OnScrollChanged) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        //view.getViewTreeObserver().addOnScrollChangedListener(new ViewCommonEventListener(handler, method));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method);
                        setEventListener(view, "addOnScrollChangedListener", ViewTreeObserver.OnScrollChangedListener.class, listener);
                    } else if (annotation instanceof OnScrollStateChanged) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        Method method0 = null, method1 = null;
                        ConcurrentHashMap<Annotation, Method> a_m_map = info_annotation_method_map.get(info);
                        for (Annotation a : a_m_map.keySet()) {
                            if (a instanceof OnScrollStateChanged) {
                                method0 = a_m_map.get(a);
                            } else if (a instanceof OnScroll) {
                                method1 = a_m_map.get(a);
                            }
                        }
                        //((AbsListView) view).setOnScrollListener(new ViewCommonEventListener(handler, method0, method1));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method0, method1);
                        setEventListener(view, "setOnScrollListener", AbsListView.OnScrollListener.class, listener);
                    } else if (annotation instanceof OnItemSelected) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        Method method0 = null, method1 = null;
                        ConcurrentHashMap<Annotation, Method> a_m_map = info_annotation_method_map.get(info);
                        for (Annotation a : a_m_map.keySet()) {
                            if (a instanceof OnItemSelected) {
                                method0 = a_m_map.get(a);
                            } else if (a instanceof OnNothingSelected) {
                                method1 = a_m_map.get(a);
                            }
                        }
                        //((AdapterView<?>) view).setOnItemSelectedListener(new ViewCommonEventListener(handler, method0, method1));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method0, method1);
                        setEventListener(view, "setOnItemSelectedListener", AbsListView.OnItemSelectedListener.class, listener);
                    } else if (annotation instanceof OnProgressChanged) {
                        View view = finder.findViewByInfo(info);
                        if (view == null) break;
                        Method method0 = null, method1 = null, method2 = null;
                        ConcurrentHashMap<Annotation, Method> a_m_map = info_annotation_method_map.get(info);
                        for (Annotation a : a_m_map.keySet()) {
                            if (a instanceof OnProgressChanged) {
                                method0 = a_m_map.get(a);
                            } else if (a instanceof OnStartTrackingTouch) {
                                method1 = a_m_map.get(a);
                            } else if (a instanceof OnStopTrackingTouch) {
                                method2 = a_m_map.get(a);
                            }
                        }
                        //((SeekBar) view).setOnSeekBarChangeListener(new ViewCommonEventListener(handler, method0, method1, method2));
                        ViewCommonEventListener listener = new ViewCommonEventListener(handler, method0, method1, method2);
                        setEventListener(view, "setOnSeekBarChangeListener", SeekBar.OnSeekBarChangeListener.class, listener);
                    }
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
            }
        }
    }

    private static void setEventListener(Object view,
                                         String setEventListenerMethod,
                                         Class<?> eventListenerType,
                                         ViewCommonEventListener listener) {
        try {
            Method setMethod = view.getClass().getMethod(setEventListenerMethod, eventListenerType);
            if (setMethod != null) {
                setMethod.invoke(view, listener);
            }
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
}
