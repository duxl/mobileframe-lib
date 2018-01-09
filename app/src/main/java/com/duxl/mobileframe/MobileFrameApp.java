package com.duxl.mobileframe;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Iterator;

public class MobileFrameApp extends Application {

	public static MobileFrameApp mInstance;
	private HashMap<String, Activity> mActivityMap = new HashMap<String, Activity>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
	
	public static MobileFrameApp getInstance() {
		return mInstance;
	}
	
	/**
	 * 添加Activity到容器中
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivityMap.put(activity.toString(), activity);
	}
	
	/**
	 * 从容器中删除activity
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		mActivityMap.remove(activity.toString());
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public void exit(boolean exit) {
		Iterator<Activity> it = mActivityMap.values().iterator();
		while(it.hasNext()) {
			it.next().finish();
		}
		
		if(exit) {
			System.exit(0);
		}
	}

	/**
	 * 清空activity容器
	 */
	public void clearActivityMap() {
		mActivityMap.clear();
	}

	/**
	 * 获取activity容器
	 * @return
	 */
	public HashMap<String, Activity> getActivityMap() {
		return mActivityMap;
	}

	/***
	 * 获取当前版本信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取"渠道号"等在manifest内定义的meta值
	 * @param key
	 * @return
	 */
	public String getMetaData(String key) {
		try {
			ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 跳转到手机桌面（相当于点击Home键）
	 */
	public void toDesktop() {
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

	/**
	 * 调用系统键盘的del删除键，删除编辑框为editText的单个字符
	 * @param editText
	 */
	public void callDelkey(EditText editText) {
		int keyCode = KeyEvent.KEYCODE_DEL;
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		editText.onKeyDown(keyCode, event);
	}


}
