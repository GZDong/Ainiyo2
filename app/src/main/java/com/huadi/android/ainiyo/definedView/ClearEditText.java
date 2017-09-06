package com.huadi.android.ainiyo.definedView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.huadi.android.ainiyo.R;

/**
 * Created by zhidong on 2017/8/8.
 * 继承自EditText的自定义view
 * 主要完成2件事情：1.输入内容时，显示清除图标
 *                  2.当点击了清除图标时，清除EditText里的内容
 */
public class ClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher { 
    private Drawable mClearDrawable;
 
    public ClearEditText(Context context) { 
    	this(context, null); 
    } 
 
    public ClearEditText(Context context, AttributeSet attrs) { 
    	this(context, attrs, android.R.attr.editTextStyle);
    } 
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    
    private void init() {

        //初始化：首先，加载清除图标（不加载）；其次，绘制editText的区域；设置监听接口
        //获得EditText的右边图像，没有就加载进来
    	mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) { 
        	mClearDrawable = getResources() 
                    .getDrawable(R.drawable.emotionstore_progresscancelbtn);
        }
        //getIntrinsicWidth()获得固定大小
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false); //初始化的时候先不显示
        setOnFocusChangeListener(this); 
        addTextChangedListener(this); 
    } 

    @Override 
    public boolean onTouchEvent(MotionEvent event) {
        //以up事件为标志，判断点击事件的落点，然后清除内容
        if (getCompoundDrawables()[2] != null) { 
            if (event.getAction() == MotionEvent.ACTION_UP) { 
            	boolean touchable = event.getX() > (getWidth() 
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) 
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) { 
                    this.setText(""); 
                } 
            } 
        } 
 
        return super.onTouchEvent(event); 
    } 
    //重写聚焦监听接口OnFocusChangeListener的方法onFocusChange
    @Override
    public void onFocusChange(View v, boolean hasFocus) { 
        if (hasFocus) {
            //输入的文字大于0就出现图像
            setClearIconVisible(getText().length() > 0);
        } else { 
            setClearIconVisible(false); 
        } 
    } 
 
    //使用setCompoundDrawables设置右边的清除图标
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null; 
        setCompoundDrawables(getCompoundDrawables()[0], 
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
    } 
     
    
    @Override
    public void onTextChanged(CharSequence s, int start, int count, 
            int after) { 
        setClearIconVisible(s.length() > 0);  //text内容改变不一定就要显示，必须大于0
    } 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {}
}
