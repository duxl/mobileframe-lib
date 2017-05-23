package com.duxl.mobileframe.view;

import android.content.Context;

/**
 * Created by duxl on 2016/08/23.
 */
public class Toast {

    private android.widget.Toast mToast;

    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast toast = new Toast();
        toast.mToast = android.widget.Toast.makeText(context, text, duration);
        return toast;
    }

    public static Toast makeText(Context context, int textid, int duration) {
        return makeText(context, context.getString(textid), duration);
    }

    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;

    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;

    public void show() {
        if(mToast != null) {
            mToast.show();
        }
    }
}
