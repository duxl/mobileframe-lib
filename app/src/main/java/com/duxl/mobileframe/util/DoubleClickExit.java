package com.duxl.mobileframe.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.duxl.mobileframe.R;
import com.duxl.mobileframe.view.Toast;

/**
 * 双击退出应用确认提示
 * @author duxl（20150916）
 *
 */
public class DoubleClickExit implements Runnable {

	private Activity mActivity;
	private Handler handler = new Handler();
	private boolean exit;
	
	public DoubleClickExit(Activity activity) {
		mActivity = activity;
	}
	
	public void exit(boolean toDesktop) {
		if(exit) {
			if(mActivity != null) {
				if(toDesktop) {
					Intent setIntent = new Intent(Intent.ACTION_MAIN);
					setIntent.addCategory(Intent.CATEGORY_HOME);
					setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mActivity.startActivity(setIntent);
				} else {
					mActivity.finish();
				}
			}
			
		} else {
			exit = true;
			Toast.makeText(mActivity, R.string.duxl_mobileframe_double_click_exit, Toast.LENGTH_SHORT).show();
			handler.postDelayed(this, 2000);
		}
	}
	
	@Override
	public void run() {
		exit = false;
	}
}
