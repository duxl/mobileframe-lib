package com.duxl.mobileframe.util;

import android.content.Context;
import android.view.View;

/**
 * 控件控制类
 * @author duxl
 *
 */
public class ViewClickDelayUtil {

	/**
	 * 延迟控件的点击响应（避免快速连续点击）
	 * @param context
	 * @param v
	 */
	public static void clickDelay(Context context, final View v) {
		clickDelay(context, v, 1000);
	}

	/**
	 * 延迟控件的点击响应（避免快速连续点击）
	 * @param context
	 * @param v
	 * @param delayMillis 延迟时间
	 */
	public static void clickDelay(Context context, final View v, long delayMillis) {
		if(v != null && v.isClickable()) {
			v.setClickable(false);
			v.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(v != null) {
						v.setClickable(true);
					}
				}
			}, delayMillis);
		}
	}
}
