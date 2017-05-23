package com.duxl.mobileframe.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.duxl.mobileframe.R;
import com.duxl.mobileframe.mobile.AdvInfo;

import java.util.List;

/**
 * 广告位适配器（create by duxl 20150911）
 * @author duxl
 *
 */
public class AdvAdapter extends PagerAdapter {

	private Context mContext;
	private List<AdvInfo> mlistData;
	private AQuery mAQuery;
	private OnAdvItemClickListener mOnItemClickListener;
	
	public void setOnItemClickListener(OnAdvItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
	
	public AdvAdapter(List<AdvInfo> data, Context context) {
		mlistData = data;
		mContext = context;
		mAQuery = new AQuery(mContext);
	}
	
	@Override
	public int getCount() {
		return mlistData == null ? 0 : mlistData.size();
	}

	public AdvInfo getItem(int position) {
		return mlistData.get(position);
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		return v == obj;
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	public Object instantiateItem(ViewGroup container, final int position) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.adv_item_layout, null);
		ImageView ivImg = (ImageView) v.findViewById(R.id.ivImg_adv_item_layout);
		mAQuery.id(ivImg).image(getItem(position).getImgUrl(), true, true, 0, 0);
		container.addView(v);
		
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onAdvItemClick(position);
				}
			}
		});
		
		return v;
	}
	
	public interface OnAdvItemClickListener {
		/**
		 * 广告位点击
		 * @param position
		 */
		public void onAdvItemClick(int position);
	}
}
