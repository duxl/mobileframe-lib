package com.duxl.mobileframe.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.duxl.mobileframe.view.XListView;
import com.duxl.mobileframe.view.XListView.LoadMore;

/**
 * 缓存数据工具类(create by duxl 20150911)
 * @author duxl
 *
 */
public class SharedPreferencesUtil {

	private Context mContext;
	private SharedPreferences mSp;
	private final String fileName = "cacheData";
	private final String loadMoreKey = "List_Load_More_Type";
	
	public SharedPreferencesUtil(Context context) {
		mContext = context;
		mSp = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}
	
	/**
	 * 缓存数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean cacheString(String key, String value) {
		return mSp.edit().putString(key, value).commit();
	}

	/**
	 * 缓存数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean cacheInt(String key, int value) {
		return mSp.edit().putInt(key, value).commit();
	}

	/**
	 * 缓存数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean cacheFloat(String key, float value) {
		return mSp.edit().putFloat(key, value).commit();
	}

	/**
	 * 缓存数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean cacheBoolean(String key, boolean value) {
		return mSp.edit().putBoolean(key, value).commit();
	}

	public String getCacheString(String key) {
		return mSp.getString(key, null);
	}

	public int getCacheInt(String key, int defaultValue) {
		return mSp.getInt(key, defaultValue);
	}

	public float getCacheFloat(String key, float defaultValue) {
		return mSp.getFloat(key, defaultValue);
	}

	public boolean getCacheBoolean(String key, boolean defaultValue) {
		return mSp.getBoolean(key, defaultValue);
	}
	
	/**
	 * 删除缓存
	 * @param key
	 * @return
	 */
	public boolean removeCacheData(String key) {
		return mSp.edit().remove(key).commit();
	}
	
	/**
	 * 清空缓存
	 * @return
	 */
	public boolean clearCacheData() {
		return mSp.edit().clear().commit();
	}

	
	/**
	 * 获取列表加载更多的方式（默认手动）
	 * @return
	 */
	public LoadMore getLoadMore() {
		String data = mSp.getString(loadMoreKey, null);
		if(LoadMore.AUTOMATIC.name().equals(data)) {
			return XListView.LoadMore.AUTOMATIC;
		}
		return LoadMore.MANUAL;
	}
	
	/**
	 * 保存列表加载更多的方式
	 * @param loadMore
	 * @return
	 */
	public boolean setLoadMore(LoadMore loadMore) {
		return mSp.edit().putString(loadMoreKey, loadMore.name()).commit();
	}
}
