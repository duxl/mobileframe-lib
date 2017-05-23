package com.duxl.mobileframe.util;

import com.duxl.mobileframe.common.Global;

public class Log {

	public static void d(String tag, String msg) {
		if(Global.DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if(Global.DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}
}