package com.duxl.mobileframe.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.duxl.mobileframe.R;
import com.duxl.mobileframe.adapter.AdvAdapter;
import com.duxl.mobileframe.mobile.AdvInfo;

import java.util.List;

/**
 * 显示滚动广告工具类
 * @author duxl
 *
 */
public class AdvShowUtil implements OnPageChangeListener, View.OnTouchListener {

	private Context mContext;
	private List<AdvInfo> mListAdvData; // 广告位数据
	private ViewPager mAdViewPager; // 焦点广告
	private RadioGroup mAdPointGroup;
	private int mPointResid = R.drawable.adv_point_selector;
	private int mPointIndex = 0;
	private static final long SCORLL_DELAY = 3000; // 广告位自动切换广告间隔时间
	private static final int mHanderWhat = 30303;
	private AdvAdapter.OnAdvItemClickListener onItemClickListener;

	public void setOnItemClickListener(AdvAdapter.OnAdvItemClickListener listener) {
		this.onItemClickListener = listener;
	}

	public AdvShowUtil(Context context, ViewPager pager, RadioGroup group) {
		mContext = context;
		mAdViewPager = pager;
		mAdPointGroup = group;
		mAdViewPager.setOnPageChangeListener(this);
	}

	public void setPointResid(int resid) {
		this.mPointResid = resid;
	}

	/**
	 * 显示广告位数据
	 * @param data
	 */
	public void showAdvData(List<AdvInfo> data) {
		mPointIndex = 0;
		mListAdvData = data;
		if (null == mListAdvData) {
			return;
		}

		// 显示图片
		AdvAdapter focusAdvAdapter = new AdvAdapter(mListAdvData, mContext);
		focusAdvAdapter.setOnItemClickListener(onItemClickListener);
		mAdViewPager.setAdapter(focusAdvAdapter);

		// 显示底部焦点
		mAdPointGroup.removeAllViews();
		int height = mContext.getResources().getDrawable(mPointResid).getIntrinsicHeight();
		for (int i=0; i< mListAdvData.size(); i++) {
			RadioButton radioBtn = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.adv_point_layout, null);
			radioBtn.setButtonDrawable(mPointResid);
			radioBtn.setClickable(false);
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(height, height);
			params.leftMargin = height;
			params.rightMargin = height;
			mAdPointGroup.addView(radioBtn, params);
		}
		if (mListAdvData.size() <= 1) {
			mAdPointGroup.setVisibility(View.GONE);
		} else {
			mAdPointGroup.setVisibility(View.VISIBLE);
			//((RadioButton) mAdPointGroup.getChildAt(0)).setChecked(true);
		}

		int adCount = mAdPointGroup.getChildCount();
		if (adCount > 1) {
			if (mPointIndex > adCount - 1) {
				mPointIndex = adCount - 1;
			}
			RadioButton radioBtn = (RadioButton) mAdPointGroup.getChildAt(mPointIndex);
			radioBtn.setChecked(true);
			purgeTimer();
		}
	}

	/**
	 * 开始定时切换广告
	 */
	private void purgeTimer() {
		handler.removeMessages(mHanderWhat);
		handler.sendEmptyMessageDelayed(mHanderWhat, SCORLL_DELAY);
	}

	/**
	 * 停止广告滚动，当页面销毁时需要调用此API，避免多次使用造成OOM问题
	 */
	public void recycle() {
		handler.removeMessages(mHanderWhat);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == mHanderWhat) {
				mPointIndex++;
				if (mPointIndex >= mAdPointGroup.getChildCount()) {
					mPointIndex = 0;
				}

				RadioButton radioBtn = (RadioButton) mAdPointGroup.getChildAt(mPointIndex);
				if(radioBtn != null){
					radioBtn.setChecked(true);
					mAdViewPager.setCurrentItem(mPointIndex, true);
				}

				Message msg2 = new Message();
				msg2.what = mHanderWhat;
				handler.sendEmptyMessageDelayed(30303, SCORLL_DELAY);
			}
		};
	};



	@Override
	public void onPageSelected(int arg0) {
		mPointIndex = arg0;
		if(mAdPointGroup.getChildAt(arg0) != null){
			((RadioButton) mAdPointGroup.getChildAt(arg0)).setChecked(true);
		}
		//purgeTimer();
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	private long mTouchDownTime;
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			recycle();
			mTouchDownTime = System.currentTimeMillis();

		} else if(motionEvent.getAction() == MotionEvent.ACTION_UP
				|| motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
			purgeTimer();

			// 触摸时间超过1秒，将事件处理掉，避免触摸结束后进入了点击事件
			if(System.currentTimeMillis() - mTouchDownTime > 1000) {
				return true;
			}
		}
		return false;
	}
}