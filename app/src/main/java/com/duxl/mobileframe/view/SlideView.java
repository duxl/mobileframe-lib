package com.duxl.mobileframe.view;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.duxl.mobileframe.R;

public class SlideView extends LinearLayout {

	private Context mContext;
	private LinearLayout mViewContent;
	private FrameLayout mViewCompat;
	private Scroller mScroller;
	private OnSlideListener mOnSlideListener;

	private int mHolderWidth = 120;

	private int mLastX = 0;
	private int mLastY = 0;
	private static final int TAN = 2;
	private boolean mFlag;

	public boolean getFlag() {
		return mFlag;
	}

	public interface OnSlideListener {
		public static final int SLIDE_STATUS_OFF = 0;
		public static final int SLIDE_STATUS_START_SCROLL = 1;
		public static final int SLIDE_STATUS_ON = 2;

		/**
		 * @param view
		 *            current SlideView
		 * @param status
		 *            SLIDE_STATUS_ON or SLIDE_STATUS_OFF
		 */
		public void onSlide(View view, int status);
	}

	public SlideView(Context context) {
		super(context);
		initView();
	}

	public void setSlideWidth(int width) {
		mHolderWidth = width;
		LayoutParams params = (LayoutParams) mViewCompat.getLayoutParams();
		params.width = mHolderWidth;
		mViewCompat.setLayoutParams(params);
	}

	private void initView() {
		setGravity(Gravity.CENTER_VERTICAL);
		setOrientation(LinearLayout.HORIZONTAL);

		mContext = getContext();
		mScroller = new Scroller(mContext);

		View.inflate(mContext, R.layout.slide_view_merge, this);
		mViewContent = (LinearLayout) findViewById(R.id.view_content_slide_view_merge);
		mViewCompat = (FrameLayout) findViewById(R.id.view_compat_slide_view_merge);
	}

	public void setContentView(View view) {
		mViewContent.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	public void setCompatView(View view) {
		mViewCompat.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public void setOnSlideListener(OnSlideListener onSlideListener) {
		mOnSlideListener = onSlideListener;
	}

	public void shrink() {
		if (getScrollX() != 0) {
			this.smoothScrollTo(0, 0);
		}
	}

	public void onRequireTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int scrollX = getScrollX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			mFlag = false;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
				break;
			}

			int newScrollX = scrollX - deltaX;
			if (deltaX != 0) {
				if (newScrollX < 0) {
					newScrollX = 0;
				} else if (newScrollX > mHolderWidth) {
					newScrollX = mHolderWidth;
				}
				if (Math.abs(deltaX) > 3) {
					mFlag = true;
				}
				this.scrollTo(newScrollX, 0);
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			int newScrollX = 0;
			if (scrollX - mHolderWidth * 0.75 > 0) {
				newScrollX = mHolderWidth;
			}
			this.smoothScrollTo(newScrollX, 0);
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this,
						newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
								: OnSlideListener.SLIDE_STATUS_ON);
			}
			break;
		}
		default:
			break;
		}

		mLastX = x;
		mLastY = y;
	}

	private void smoothScrollTo(int destX, int destY) {
		// 缓慢滚动到指定位置
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

}
