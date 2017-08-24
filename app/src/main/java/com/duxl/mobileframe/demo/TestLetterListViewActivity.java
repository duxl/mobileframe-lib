package com.duxl.mobileframe.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.duxl.mobileframe.R;
import com.duxl.mobileframe.view.LetterListView;


/**
 * 作者：Created by duxl on 2017/08/24.
 * 公司：重庆赛博丁科技发展有限公司
 * 类描述：测试LetterListView
 */

public class TestLetterListViewActivity extends Activity implements LetterListView.OnTouchingLetterChangedListener {

    private LetterListView mLetterListView;
    private TextView mTvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_letter_listview);

        mTvInfo = (TextView) findViewById(R.id.tvInfo_activity_test_letter_listview);

        mLetterListView = (LetterListView) findViewById(R.id.letterListView_activity_test_letter_listview);
        mLetterListView.setOnTouchingLetterChangedListener(this); // 触摸字母回调
        mLetterListView.showOverlay(true); // 显示触摸字母弹框
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        mTvInfo.setText("测试LetterListView");
        mTvInfo.append("\n");
        mTvInfo.append("\n");
        mTvInfo.append(s);
    }
}
