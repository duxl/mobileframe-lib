package com.duxl.mobileframe;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 所有Activity的基类
 */
public class BaseActivity extends FragmentActivity {

    private final String TAG = getClass().getSimpleName();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MobileFrameApp.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        MobileFrameApp.getInstance().removeActivity(this);
        super.onDestroy();
    }

    public void showProgressDialog(CharSequence message) {
        showProgressDialog(null, message);
    }

    public void showProgressDialog(String title, CharSequence message) {
        dismissProgressDialog();
        mProgressDialog = ProgressDialog.show(this, title, message);
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
