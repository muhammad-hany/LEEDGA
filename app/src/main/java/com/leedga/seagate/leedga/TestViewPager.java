package com.leedga.seagate.leedga;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Muhammad Workstation on 20/09/2016.
 */
public class TestViewPager extends ViewPager {

    boolean enableSwiping;
    public TestViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (enableSwiping) {
            return super.onTouchEvent(ev);
        }else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (enableSwiping) {
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }
    }

    public void setSwippingEnabled(boolean answer){
        enableSwiping=answer;
    }
}
