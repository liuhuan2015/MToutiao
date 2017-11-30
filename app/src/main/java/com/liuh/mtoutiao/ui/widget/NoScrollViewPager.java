package com.liuh.mtoutiao.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author:liuh
 * Date: 2017/11/30 13:39
 * Description:
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //不拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //不处理事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
