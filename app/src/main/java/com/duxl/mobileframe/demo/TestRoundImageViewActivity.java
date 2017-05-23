package com.duxl.mobileframe.demo;

import android.os.Bundle;
import android.widget.ImageView;

import com.duxl.mobileframe.BaseActivity;
import com.duxl.mobileframe.R;

/**
 * 测试圆角ImageView控件
 * Created by Administrator on 2016/08/24.
 */
public class TestRoundImageViewActivity extends BaseActivity {

    ImageView mIvImg;
    ImageView mIvImg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_roundimageview);

        mIvImg = (ImageView) findViewById(R.id.ivRoundView_activity_test_roundimageview);
        mIvImg2 = (ImageView) findViewById(R.id.ivRoundView2_activity_test_roundimageview);

        String imgUrl = "http://img4.duitang.com/uploads/item/201409/19/20140919184028_U2ZYK.jpeg";
        //new AQuery(this).id(mIvImg).image(imgUrl, true, true);

        //new AQuery(this).id(mIvImg2).image(imgUrl, true, true);
    }
}
