package com.duxl.mobileframe.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.duxl.mobileframe.R;

public abstract class CompatAdapter extends BaseAdapter implements SlideView.OnSlideListener {

	private Context mContext;
	private SlideView mLastSlideViewWithStatusOn;
	private SlideView mCurrentTouchSlideView;
	private OnClickCompatListener mOnClickCompatListener;
	
	public CompatAdapter(Context act) {
		super();
		this.mContext = act;
	}
	
	public void setOnClickCompatListener(OnClickCompatListener listener) {
		this.mOnClickCompatListener = listener;
	}
	
	public SlideView getCurrentTouchSlideView() {
		return mCurrentTouchSlideView;
	}

	@Override
	public abstract int getCount();

	@Override
	public abstract Object getItem(int position);

	@Override
	public abstract long getItemId(int position);
	
	public abstract int getItemViewResid(int position);
	
	public abstract int getCompatViewResid(int position);
	
	public abstract void setView(int position, View convertView);

	public abstract int getMenuViewWidth(int position);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
        	slideView = new SlideView(mContext);
			slideView.setSlideWidth(getMenuViewWidth(position));
        	slideView.setContentView(LayoutInflater.from(mContext).inflate(getItemViewResid(position), null));
        	slideView.setCompatView(LayoutInflater.from(mContext).inflate(getCompatViewResid(position), null));
            slideView.setOnSlideListener(this);
        }
        
        slideView.shrink();
        setView(position, slideView.findViewById(R.id.view_content_slide_view_merge));
        View compatView = slideView.findViewById(R.id.view_compat_slide_view_merge);
        compatView.setTag(position);
        compatView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnClickCompatListener != null) {
					mOnClickCompatListener.onClickCompat(Integer.parseInt(v.getTag().toString()));
				}
			}
		});
        
        slideView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mCurrentTouchSlideView = (SlideView) v;
				return false;
			}
		});
        
		return slideView;
	}
	
	@Override
	public void onSlide(View view, int status) {
		// 把之前的SlideView关闭掉
		if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
	}
	
	public static interface OnClickCompatListener {
		void onClickCompat(int position);
	}
}
