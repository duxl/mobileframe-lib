package com.duxl.mobileframe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.duxl.mobileframe.R;

/**
 * Created by Administrator on 2016/05/31.
 */
public class LetterListView extends ListView {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] b = {"#","A","B","C","D","E","F","G","H","I","J","K","L"
            ,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private TextView mTvOverlay;
    private boolean mShowOverLay = true;

    /**
     * 设置是否显示选中的字母弹框
     * @param show
     */
    public void showOverlay(boolean show) {
        mShowOverLay = show;
    }

    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LetterListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(showBkg){
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for(int i=0;i<b.length;i++){
            paint.setColor(Color.BLACK);
            //paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.text_size_small));
            if(i == choose){
                paint.setColor(Color.parseColor("#FF0000"));
            }
            float xPos = width/2f  - paint.measureText(b[i])/2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y/getHeight()*b.length);

        logicOverView(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if(oldChoose != c && listener != null){
                    if(c >= 0 && c< b.length){
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if(oldChoose != c && listener != null){
                    if(c >= 0 && c< b.length){
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 处理显示按住的字母弹框
     * @param event
     */
    private void logicOverView(MotionEvent event) {
        if(mShowOverLay && (choose >=0 && choose < b.length)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_MOVE) {
                if(mTvOverlay == null) {
                    mTvOverlay = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.letterlistview_overlay, null);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
                    WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    windowManager.addView(mTvOverlay, lp);
                }
                mTvOverlay.setText(b[choose]);

            }  else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if(mTvOverlay != null) {
                    WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    windowManager.removeView(mTvOverlay);
                    mTvOverlay = null;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener{
        public void onTouchingLetterChanged(String s);
    }
}
