package com.duxl.mobileframe.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * Created by duxl on 2016/3/8.
 */
public class SoftKeyboardUtil {

    public Context mContext;

    public SoftKeyboardUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 隐藏软键盘
     * @param v
     */
    public void hide(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 如果软键盘已显示那么就关闭，反之显示软键盘
     */
    public void toggle() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
