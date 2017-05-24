package com.duxl.mobileframe.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.duxl.mobileframe.BaseActivity;
import com.duxl.mobileframe.R;
import com.duxl.mobileframe.view.CompatAdapter;
import com.duxl.mobileframe.view.ListViewCompat;
import com.duxl.mobileframe.view.SlideView;
import com.duxl.mobileframe.view.Toast;

/**
 * 作者：Created by duxl on 2017/05/24.
 * 公司：重庆赛博丁科技发展有限公司
 * 类描述：xxx
 */

public class TestSlideViewActivity extends BaseActivity {

    private FrameLayout mFlContainer;
    private ListViewCompat mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_slide_view);
        slideOne();
        slidList();

    }

    /**
     * 列表侧滑
     */
    private void slidList() {
        mListView = (ListViewCompat) findViewById(R.id.listView_activity_test_slide_view);
        CompatAdapter adapter = new CompatAdapter(this) {
            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public String getItem(int position) {
                return "item（"+position+"）";
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewResid(int position) {
                return R.layout.test_itemview_layout;
            }

            @Override
            public int getCompatViewResid(int position) {
                return R.layout.test_compatview_layout;
            }

            @Override
            public void setView(int position, View convertView) {
                TextView tvItem = (TextView)convertView.findViewById(R.id.tvItem_teset_itemview_layout);
                tvItem.setText("Item（"+ position +"）");
            }

            @Override
            public int getMenuViewWidth(int position) {
                int dpWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                return dpWidth;
            }
        };
        adapter.setOnClickCompatListener(new CompatAdapter.OnClickCompatListener() {
            @Override
            public void onClickCompat(int position) {
                Toast.makeText(TestSlideViewActivity.this, "clickDel=" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TestSlideViewActivity.this, "clickItem=" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 单个侧滑
     */
    private void slideOne() {
        mFlContainer = (FrameLayout) findViewById(R.id.flContainer_activity_test_slide_view);

        final SlideView slideView = new SlideView(this);
        slideView.setSlideWidth(200);

        TextView contextView = new TextView(this);
        contextView.setBackgroundColor(Color.GREEN);
        contextView.setTextColor(Color.BLUE);
        contextView.setText("这是显示的内容（可以侧滑）");
        contextView.setPadding(10, 50, 10, 50);
        slideView.setContentView(contextView);

        TextView compatView = new TextView(this);
        compatView.setBackgroundColor(Color.BLUE);
        compatView.setTextColor(Color.WHITE);
        compatView.setText("DEL");
        compatView.setPadding(50, 50, 50, 50);
        compatView.setGravity(Gravity.CENTER);
        slideView.setCompatView(compatView);

        slideView.shrink();

        mFlContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slideView.onRequireTouchEvent(event);
                return true;
            }
        });

        mFlContainer.addView(slideView);
    }
}
