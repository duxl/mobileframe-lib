package com.duxl.mobileframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 简化的数据适配器，封装了常规的BaseAdapter写法<br />
 * 继承该类只需要实现如下三个方法：<br />
 * 1、布局文件资源 {@link #getLayoutResid(int)} <br />
 * 2、查找HolderView控件 {@link #findHolderView(View, int)} <br />
 * 3、设置HolderView每一项的数据 {@link #setHolderView(Object, Object, int)} <br />
 * 
 * @author duxl
 * 
 * @param <T> 列表数据类
 * @param <H> 列表控件HolderView类
 */
public abstract class SimpleBaseAdapter<T, H> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mListData;

	public SimpleBaseAdapter(Context cxt, List<T> listData) {
		this.mContext = cxt;
		this.mListData = listData;
	}

	@Override
	public int getCount() {
		return mListData == null ? 0 : mListData.size();
	}

	@Override
	public T getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		H holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(getLayoutResid(position), null);
			holderView = findHolderView(convertView, position);
			convertView.setTag(holderView);
		} else {
			holderView = (H) convertView.getTag();
		}

		setHolderView(holderView, getItem(position), position);
		return convertView;
	}

	/**
	 * 查找HolderView控件
	 * 
	 * @param inflater
	 * @param position
	 * @return
	 */
	public abstract H findHolderView(View convertView, int position);

	/**
	 * 设置HolderView每一项的数据
	 * 
	 * @param holderView
	 * @param position
	 */
	public abstract void setHolderView(H holderView, T objItem, int position);

	/**
	 * 布局文件资源
	 * 
	 * @param position
	 * @return
	 */
	public abstract int getLayoutResid(int position);

}