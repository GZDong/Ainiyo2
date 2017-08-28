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
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.huadi.android.ainiyo.R;

/**
 * Created by zhidong on 2017/8/8.
 * 继承自EditText的自定义view
 * 主要完成2件事情：1.当输入内容不为0时，显示清除图标
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
        //获得EditText的右边图像，没有就加载进来
    	mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) { 
        	mClearDrawable = getResources() 
                    .getDrawable(R.drawable.emotionstore_progresscancelbtn);
        } 
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight()); 
        setClearIconVisible(false); //初始化的时候先不显示
        setOnFocusChangeListener(this); 
        addTextChangedListener(this); 
    } 

    @Override 
    public boolean onTouchEvent(MotionEvent event) {
        //只要是在清楚图像的区域内就清除内容，以UP事件为标志
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
            setClearIconVisible(getText().length() > 0); //输入的文字大于0就出现图像
        } else { 
            setClearIconVisible(false); 
        } 
    } 
 
 
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
    public void beforeTextChanged(CharSequence s, int start, int count, 
            int after) { 
         
    } 
 
    @Override 
    public void afterTextChanged(Editable s) { 
         
    } 
    
   
    public void setShakeAnimation(){
    	this.setAnimation(shakeAnimation(5));
    }
    
    
    public static Animation shakeAnimation(int counts){
    	Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
    	translateAnimation.setInterpolator(new CycleInterpolator(counts));
    	translateAnimation.setDuration(1000);
    	return translateAnimation;
    }
 
 
}
